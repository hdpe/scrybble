package hdpe.scrybble.game;

import hdpe.scrybble.config.Configuration;
import hdpe.scrybble.game.event.GameListener;
import hdpe.scrybble.persist.GameIO;
import hdpe.scrybble.persist.SavedGame;
import hdpe.scrybble.persist.SavedPlayerRack;
import hdpe.scrybble.persist.SavedPlayerTurn;
import hdpe.scrybble.persist.SavedTile;
import hdpe.scrybble.ui.HumanStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Ryan Pickett
 *
 */
public class GameContext {

	private Configuration configuration;
	private Board board;
	private List<GameListener> gameListeners = new ArrayList<GameListener>();
	private List<Player> players = Arrays.asList(
			(Player)new PlayerImpl("Player 1", new HumanStrategy()),
			(Player)new PlayerImpl("Player 2", new HumanStrategy())
			);
	
	/**
	 * 
	 */
	public GameContext() {
		this(Configuration.getDefaultConfiguration());
	}
	
	/**
	 * @param configuration
	 */
	public GameContext(Configuration configuration) {
		setConfiguration(configuration);
	}
	
	/**
	 * @param listener
	 */
	public void addGameListener(GameListener listener) {
		gameListeners.add(listener);
	}
	
	/**
	 * @param listener
	 */
	public void removeGameListener(GameListener listener) {
		gameListeners.remove(listener);
	}
	
	/**
	 * @return
	 */
	public Game newGame() {
		Game game = new Game(this, GameMode.PLAY, players);
		for (GameListener listener : gameListeners) {
			game.addGameListener(listener);
		}
		return game;
	}
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public Game loadGame(File file) throws IOException, InstantiationException, 
			IllegalAccessException, ClassNotFoundException {
		
		SavedGame save = GameIO.load(file);
		
		configuration = save.getSavedConfiguration().toConfiguration();
		
		players = new ArrayList<Player>();
		
		Map<String, Player> playersMap = new HashMap<String, Player>();
		
		for (SavedPlayerRack rack : save.getRacks()) {
			Player player = new PlayerImpl(rack.getPlayerName(), (PlayerStrategy)
					Class.forName(rack.getPlayerStrategy()).newInstance());
			players.add(player); 
			playersMap.put(player.getName(), player);
		}
		
		final Game game = newGame();
		
		for (final SavedPlayerRack rack : save.getRacks()) {
			game.addRack(playersMap.get(rack.getPlayerName()), new LoadedRack() {				
				public Collection<Tile> getTiles(Game game) {
					return SavedTile.removeTiles(rack.getTiles(), game.getTileBag().getTiles());
				}
			});
		}
		
		for (final SavedPlayerTurn turn : save.getTurns()) {
			game.addMove(new LoadedMove(playersMap.get(turn.getPlayerName()),  
					turn.getMove().toStartMove(game, playersMap.get(turn.getPlayerName())),
					new FuturePickup() {						
						public Collection<Tile> getTiles() {
							return SavedTile.removeTiles(turn.getPickup(), game.getTileBag().getTiles());
						}
					},
					turn.getElapsed()));
		}

		game.setFirstPlayer(players.get(0));
		
		return game;
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
	public Configuration getConfiguration() {
		return configuration;
	}
	
	/**
	 * @return
	 */
	public Dictionary getDictionary() {
		Dictionary dict = configuration.getDictionary();
		if (!dict.isLoaded()) {
			dict.init();
		}
		return dict;
	}

	/**
	 * @param configuration
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		this.board = new Board(configuration.getBoardLayout());
	}

	/**
	 * @return
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
}