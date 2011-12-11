package hdpe.scrybble.game;

import hdpe.scrybble.game.event.GameDrawStartTileEvent;
import hdpe.scrybble.game.event.GameFirstPlayerDeterminedEvent;
import hdpe.scrybble.game.event.GameIllegalMoveEvent;
import hdpe.scrybble.game.event.GameListener;
import hdpe.scrybble.game.event.GameMoveEvent;
import hdpe.scrybble.game.event.GameResultEvent;
import hdpe.scrybble.game.event.GameStoppedEvent;
import hdpe.scrybble.game.event.GameTurnEndEvent;
import hdpe.scrybble.game.event.GameTurnStartEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;


/**
 * @author Ryan Pickett
 *
 */
public class Game {

	private GameContext gameContext;
	private GameState state = new GameStateImpl();	
	
	private EventListenerList listeners = new EventListenerList();
	private RackStockingProvider rackStockingProvider;
	
	private GameMode mode;
	private Board board;
	private TileBag tileBag;		
	private int turn = 1;
	private int playerTurn = 1;
	private GameHistory history = new GameHistory();
	private List<Player> players;
	private Player nextPlayer;
	private Map<Player, Collection<Tile>> initialRacks = new HashMap<Player, Collection<Tile>>(); 
	private GameResult result;
	
	private Map<Player, LoadedRack> loadedRacks = new HashMap<Player, LoadedRack>();
	private List<LoadedMove> loadedMoves = new ArrayList<LoadedMove>();
	
	private boolean running;
	private volatile boolean stopping;

	Game(GameContext gameContext, GameMode mode, List<Player> players) {
		this.gameContext = gameContext;
		this.mode = mode;
		this.players = players;
		
		board = gameContext.getBoard();
		board.clear();
		tileBag = gameContext.getConfiguration().getLetterDistributions().newTileBag();
	}
	
	/**
	 * @param mode
	 */
	public void setMode(GameMode mode) {
		this.mode = mode;
	}
	
	/**
	 * @param rackStockingProvider
	 */
	public void setRackStockingProvider(RackStockingProvider rackStockingProvider) {
		this.rackStockingProvider = rackStockingProvider;
	}
	
	/**
	 * @param listener
	 */
	public void addGameListener(GameListener listener) {
		listeners.add(GameListener.class, listener);
	}

	/**
	 * @param listener
	 */
	public void removeGameListener(GameListener listener) {
		listeners.remove(GameListener.class, listener);
	}

	protected void initialiseRacks() {
		for (Player player : getPlayerOrder()) {
			Collection<Tile> tiles;
			
			if (mode == GameMode.PLAY) {
				tiles = tileBag.getTiles(state.getRackSize());
			} else {
				tiles = rackStockingProvider.getTiles(player.getState(), 
						state.getRackSize(), tileBag);
				tileBag.getTiles().removeAll(tiles);
			}
			
			initialRacks.put(player, tiles);
			player.startGame(this, tiles);
		}		
	}
	
	protected void initialiseClocks() {
		for (Player player : players) {
			player.getClock().setTimeRemaining(gameContext.getConfiguration()
					.getTimePerPlayerSeconds() * 1000);
		}
	}
	
	/**
	 * @return
	 * @throws IllegalMoveException
	 */
	public GameResult load() throws IllegalMoveException {
		
		for (Player player : players) {
			Collection<Tile> tiles = loadedRacks.get(player).getTiles(this);
			initialRacks.put(player, tiles);
			player.startGame(this, tiles);
		}
	
		initialiseClocks();
		
		for (LoadedMove loadedMove : loadedMoves) {
			
			Player player = loadedMove.getPlayer();
			fireTurnStart(player);
			
			PlayerMove move = loadedMove.getMove().getPlayerMove(this);
			move.apply(player, this);
			fireMove(player, move);
				
			Collection<Tile> newTiles = loadedMove.getPickup().getTiles();
			player.getRack().addAll(newTiles);
			tileBag.getTiles().removeAll(newTiles);
			
			history.recordTurn(player, move, newTiles, loadedMove.getElapsed());
			
			player.getClock().decrement(loadedMove.getElapsed());
			
			if (isFinished()) {
				return doEndGame(player);
			}
			
			fireTurnEnd(player);
			
			playerTurn ++;			
			
			if (playerTurn % players.size() == 0) {
				turn ++;
			}
		}
		
		nextPlayer = players.get((playerTurn - 1) % players.size());
				
		return getResult();
	}

	
	/**
	 * @return
	 */
	public GameResult start() {
		
		stopping = false;
		running = true;
		
		if (nextPlayer == null) {
			startDetermineFirstPlayer();
			
			if (stopping || nextPlayer == null) {
				fireStopped();
				running = false;
				return null;
			}
			
			initialiseRacks();
			initialiseClocks();
		}

		List<Player> players = getPlayerOrder();

		TURN_LOOP: while (!stopping) {
			for (Player player : players) {
			
				nextPlayer = player;
				
				player.getClock().start();
				
				fireTurnStart(player);
				
				PlayerMove move = null;
				Collection<Tile> newTiles = null;
				
				while (!stopping) {
					try {
						move = player.getNextMove(state, gameContext.getDictionary());
						
						if (stopping || move == null) {
							break TURN_LOOP;
						}
						
						move.apply(player, this);

						fireMove(player, move);
							
						newTiles = restockRack(player);
						
						if (newTiles == null) {
							break TURN_LOOP;
						}
						
						break;
						
					} catch (IllegalMoveException e) {
						fireIllegalMove(player, move, e);
					}
				}
				
				if (stopping) {
					break TURN_LOOP;
				}
				
				long elapsed = player.getClock().stop();
				history.recordTurn(player, move, newTiles, elapsed);
				
				if (isFinished()) {
					return doEndGame(player);
				}
				
				fireTurnEnd(player);
				
				playerTurn ++;
			}
			
			turn ++;
		}

		fireStopped();
		running = false;
		return null;
	}
	
	
	/**
	 * 
	 */
	public void stop() {
		stopping = true;
		if (nextPlayer != null) {
			nextPlayer.getStrategy().cancel();
		}
	}	

	protected void startDetermineFirstPlayer() {

		Tile bestTile;
		Map<Player, Tile> bestTiles = new LinkedHashMap<Player, Tile>();
		List<Player> players = new ArrayList<Player>(this.players);
		
		nextPlayer = null;
		
		do {						
			bestTile = null;
			tileBag.putTiles(bestTiles.values());			
			bestTiles.clear();			
			
			for (Player player : players) {
			
				Tile tile;
				
				if (mode == GameMode.PLAY) {
					tile = tileBag.getTiles(1).iterator().next();
				} else {
					Collection<Tile> tiles = rackStockingProvider.getTiles(
							player.getState(), 1, tileBag);
					
					if (stopping || tiles == null) {
						return;
					}
					
					tile = tiles.iterator().next();
					tileBag.getTiles().remove(tile);
				}				
				
				fireDrawStartTile(player, tile);
				
				if (bestTile == null || (tile.isBlank() && !bestTile.isBlank()) || 
						tile.getLetter() < bestTile.getLetter()) {
					
					bestTile = tile;
					tileBag.putTiles(bestTiles.values());
					bestTiles.clear();
					bestTiles.put(player, tile);
					
				} else if ((tile.isBlank() && bestTile.isBlank()) || 
						tile.getLetter() == bestTile.getLetter()) {
					
					bestTiles.put(player, tile);		
				}
			}	

			players = new ArrayList<Player>(bestTiles.keySet());			
			
		} while (bestTiles.size() > 1);
		
		nextPlayer = players.get(0);
		
		fireFirstPlayerDetermined(nextPlayer);
	}	
	
	List<Player> getPlayerOrder() {		
		int firstPlayerIdx = players.indexOf(nextPlayer);
		List<Player> result = new ArrayList<Player>(players.subList(firstPlayerIdx, 
				players.size()));
		result.addAll(players.subList(0, firstPlayerIdx));
		return result;
	}
	
	GameResult doEndGame(Player lastPlayer) {
		int extra = 0;
		for (Player p : players) {
			if (lastPlayer.hasResigned() || !lastPlayer.equals(p)) {							
				int playerTilesValue = p.getRack().getValue();																
				extra += playerTilesValue;
				p.incrementScore( - playerTilesValue);
			}
		}
		
		if (lastPlayer.getRack().isEmpty()) {						
			lastPlayer.incrementScore(extra);						
		}
		
		result = new GameResult(players);
		fireEnd(result);				
		running = false;
		return result;
	}

	/**
	 * @return
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * @return
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @return
	 */
	public Dictionary getDictionary() {
		return gameContext.getDictionary();
	}

	/**
	 * @return
	 */
	public GameHistory getHistory() {
		return history;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	/**
	 * @return
	 */
	public boolean isFinished() {
		int playing = players.size();
		for (Player player : players) {
			if (player.getRack().isEmpty()) {
				return true;
			}
			if (player.hasResigned()) {
				playing --;
			}
		}
		return playing < 2;
	}

	/**
	 * @return
	 */
	public int getPlayersRemaining() {
		int n = 0;
		for (Player player : players) {
			if (!player.hasResigned()) {
				n ++;
			}
		}
		return n;
	}

	/**
	 * @return
	 */
	public int getTilesRemaining() {
		return tileBag.size();
	}
	
	/**
	 * @return
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * @return
	 */
	public boolean isFirstMove() {
		return board.getCentreSquare().getTile() == null;
	}

	void addRack(Player player, LoadedRack startRack) {
		loadedRacks.put(player, startRack);
	}
	
	void addMove(LoadedMove move) {
		loadedMoves.add(move);
	}
	
	TileBag getTileBag() {
		return tileBag;
	}

	void placeTile(Tile tile, Square square, char letter) {
		board.placeTile(tile, board.getSquare(square.getX(), square.getY()), 
				letter);
	}
	
	protected void fireDrawStartTile(Player player, Tile tile) {
		GameDrawStartTileEvent evt = new GameDrawStartTileEvent(this, player, tile);
		for (GameListener listener : listeners.getListeners(GameListener.class)) {
			listener.onDrawStartTile(evt);
		}	
	}
	
	protected void fireFirstPlayerDetermined(Player player) {
		GameFirstPlayerDeterminedEvent evt = new GameFirstPlayerDeterminedEvent(this, player);
		for (GameListener listener : listeners.getListeners(GameListener.class)) {
			listener.onFirstPlayerDetermined(evt);
		}		
	}
	
	protected void fireTurnStart(Player player) {
		GameTurnStartEvent evt = new GameTurnStartEvent(this, turn, player);
		for (GameListener listener : listeners.getListeners(GameListener.class)) {
			listener.onTurnStart(evt);
		}
	}
	
	protected void fireTurnEnd(Player player) {
		GameTurnEndEvent evt = new GameTurnEndEvent(this, turn, player);
		for (GameListener listener : listeners.getListeners(GameListener.class)) {
			listener.onTurnEnd(evt);
		}
	}
	
	protected void fireMove(Player player, PlayerMove move) {
		GameMoveEvent evt = new GameMoveEvent(this, turn, player, move);
		for (GameListener listener : listeners.getListeners(GameListener.class)) {
			listener.onMove(evt);
		}
	}
	
	protected void fireIllegalMove(Player player, PlayerMove move,
			IllegalMoveException e) {
		GameIllegalMoveEvent evt = new GameIllegalMoveEvent(this, turn, player, move, e);
		for (GameListener listener : listeners.getListeners(GameListener.class)) {
			listener.onIllegalMove(evt);
		}
	}
	
	protected void fireStopped() {
		GameStoppedEvent evt = new GameStoppedEvent(this);
		for (GameListener listener : listeners.getListeners(GameListener.class)) {
			listener.onStopped(evt);
		}
	}
	
	protected void fireEnd(GameResult result) {
		GameResultEvent evt = new GameResultEvent(this, result);
		for (GameListener listener : listeners.getListeners(GameListener.class)) {
			listener.onEnd(evt);
		}
	}

	protected Collection<Tile> restockRack(Player player) {
		Rack rack = player.getRack();

		Collection<Tile> tiles = new ArrayList<Tile>();
		
		if (rack.size() < state.getRackSize() && tileBag.hasTiles()) {
			
			int number = state.getRackSize() - rack.size();
			
			if (mode == GameMode.PLAY) {
				tiles = tileBag.getTiles(number);
			} else {
				tiles = rackStockingProvider.getTiles(player.getState(), 
						number, tileBag);
				
				if (tiles == null) {
					return null;
				}
				
				tileBag.getTiles().removeAll(tiles);
			}
		}		
		rack.addAll(tiles);
		return tiles;
	}

	/**
	 * @param player
	 * @return
	 */
	public Collection<Tile> getInitialRack(Player player) {
		return initialRacks.get(player);
	}

	/**
	 * @return
	 */
	public Player getFirstPlayer() {
		return nextPlayer;
	}

	/**
	 * @return
	 */
	public GameResult getResult() {
		return result;
	}

	/**
	 * @param player
	 */
	public void setFirstPlayer(Player player) {
		this.nextPlayer = player;
	}

	/**
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}
	
	
	private class GameStateImpl implements GameState {

		public BoardState getBoard() {
			return Game.this.board.getState();
		}

		public int getPlayersRemaining() {
			return Game.this.getPlayersRemaining();
		}

		public int getTilesRemaining() {
			return Game.this.getTilesRemaining();
		}

		public boolean isFinished() {
			return Game.this.isFinished();
		}

		public int getTurn() {
			return Game.this.turn;
		}

		public int getRackSize() {
			return 7;
		}

		public GameHistory getTurnHistory() {
			return Game.this.history;
		}

		public List<PlayerState> getPlayers() {
			List<PlayerState> players = new ArrayList<PlayerState>();
			for (Player p : Game.this.players) {
				players.add(p.getState());
			}
			return players;
		}		
	}	
}