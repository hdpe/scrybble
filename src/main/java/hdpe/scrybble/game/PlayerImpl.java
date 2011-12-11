package hdpe.scrybble.game;

import hdpe.scrybble.ai.simple.SimpleAIStrategy;
import hdpe.scrybble.ui.HumanStrategy;

import java.util.Collection;


/**
 * @author Ryan Pickett
 *
 */
public class PlayerImpl implements Player {

	private String name;
	private Rack rack = new Rack();
	private int score;
	private boolean resigned;
	private PlayerStrategy strategy;
	private PlayerState state = new PlayerStateImpl();
	
	private Clock clock = new Clock();
	
	/**
	 * @param name
	 */
	public PlayerImpl(String name) {
		this(name, new SimpleAIStrategy());
	}

	/**
	 * @param name
	 * @param strategy
	 */
	public PlayerImpl(String name, PlayerStrategy strategy) {
		this.name = name;
		this.strategy = strategy;
	}

	public Clock getClock() {
		return clock;
	}

	public String getName() {
		return name;
	}

	public PlayerMove getNextMove(GameState game, Dictionary dictionary) {		
		return strategy.getNextMove(game, getRack().getState(), dictionary);				
	}

	public Rack getRack() {
		return rack;
	}

	public int getScore() {
		return score;
	}

	public PlayerState getState() {
		return state;
	}

	public PlayerStrategy getStrategy() {
		return strategy;
	}

	public boolean hasResigned() {
		return resigned;
	}

	public void incrementScore(int score) {
		this.score += score;
	}

	public void resign() {
		this.resigned = true;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStrategy(PlayerStrategy strategy) {
		this.strategy = strategy;
	}

	public void startGame(Game game, Collection<Tile> initialTiles) {
		score = 0;
		resigned = false;
		rack.clear();
		rack.addAll(initialTiles);
		strategy.initGame(game.getState(), state);
	}

	public String toString() {
		return name;
	}
	

	private class PlayerStateImpl implements PlayerState {
		public String getName() {
			return PlayerImpl.this.name;
		}

		public boolean isHumanControlled() {
			return strategy instanceof HumanStrategy;
		}		
	}	
}