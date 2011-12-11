package hdpe.scrybble.ui;

import hdpe.scrybble.ai.simple.SimpleAIStrategy;
import hdpe.scrybble.game.Dictionary;
import hdpe.scrybble.game.GameState;
import hdpe.scrybble.game.PassGoMove;
import hdpe.scrybble.game.PlaceTilesMove;
import hdpe.scrybble.game.PlayerMove;
import hdpe.scrybble.game.PlayerState;
import hdpe.scrybble.game.PlayerStrategy;
import hdpe.scrybble.game.RackState;
import hdpe.scrybble.game.SwapTilesMove;
import hdpe.scrybble.game.TileState;
import hdpe.scrybble.util.TilePlacement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * @author Ryan Pickett
 *
 */
public class HumanStrategy implements PlayerStrategy {

	private Object lock = new Object();
	private boolean stopping;
	
	private PlayerMove nextMove;
	private int lastTurn;
	
	private PlayerState player;
	
	private GameFrame gameFrame;
	private RackPanel rackPanel;
	
	public void initGame(GameState game, PlayerState player) {
		this.player = player;
		gameFrame = GameFrameHolder.getGameFrame();
		rackPanel = gameFrame.getRackPanel();
	}
	
	public PlayerMove getNextMove(final GameState game, final RackState rack,
			final Dictionary dictionary) {
		
		final List<TilePlacement> placements = new ArrayList<TilePlacement>();
		
		if (lastTurn == game.getTurn()){
			SwingUtilities.invokeLater(new Runnable() {
				
				public void run() {
					for (TilePlacement placement : placements) {
						gameFrame.removeTile(placement);
					}
				}
			});
		}
		
		stopping = false;
		nextMove = null;
		
		RackPanelListener rfl = new RackPanelListener() {
			
			public void onGo() {
				
				for (TilePlacement p : placements) {
					gameFrame.removeTile(p);
				}
				
				synchronized (lock) {
					nextMove = new PlaceTilesMove(placements);
					lock.notifyAll();
				}
			}

			public void onSwap(Collection<TileState> tiles) {
				
				for (TileState t : tiles) {
					rackPanel.removeTile(t);
				}
				
				synchronized (lock) {
					nextMove = new SwapTilesMove(tiles);
					lock.notifyAll();
				}
			}

			public void onPass() {
				synchronized (lock) {
					nextMove = new PassGoMove();
					lock.notifyAll();
				}
			}

			public void onCheat() {
				synchronized (lock) {
					nextMove = new SimpleAIStrategy().getNextMove(game, rack, 
							dictionary);
					lock.notifyAll();
				}
			}
		};
		
		GameFrameListener bfl = new GameFrameAdapter() {
			
			public void onSquareClick(SquarePanel square) {
				
				if (square.getSquare().getTile() == null) {
					
					Set<RackTilePanel> selected = rackPanel.getSelectedTiles();
					
					if (square.getTilePlacement() != null) {
											
						TilePlacement tp = square.getTilePlacement();
						
						synchronized (lock) {
							placements.remove(tp);								
						}
						
						gameFrame.removeTile(tp);
						rackPanel.selectTile(null, false);
						rackPanel.addTile(tp.getTile());
						
					} else if (selected.size() == 1) {
					
						RackTilePanel t = selected.iterator().next();
						
						char letter = t.getTile().getLetter();
						
						if (t.getTile().isBlank()) {
							while (true) {
								try {
									letter = JOptionPane.showInputDialog(
											"Which letter?").toUpperCase()
											.charAt(0);	
									if (letter >= 'A' && letter <= 'Z') {
										break;
									}
								} catch (Exception e) {
									
								}
							}							
						}
						
						TilePlacement tp = new TilePlacement(t.getTile(), 
								square.getSquare().getState(), letter);
						
						synchronized (lock) {
							placements.add(tp);								
						}
						
						gameFrame.applyTile(tp);
						rackPanel.selectTile(null, false);
						rackPanel.removeTile(t.getTile());					
					} 
				}
			}
		};
		
		rackPanel.addRackFrameListener(rfl);
		gameFrame.addGameFrameListener(bfl);
		
		SwingUtilities.invokeLater(new Runnable() {			
			public void run() {
				rackPanel.setRack(player.getName(), rack, game);				
				rackPanel.setVisible(true);
			}
		});
		
		synchronized (lock) {			
			while (!stopping && nextMove == null) {
				try {
					lock.wait();
				} catch (InterruptedException e) {}
			}	
		};		
		
		rackPanel.removeRackFrameListener(rfl);
		gameFrame.removeGameFrameListener(bfl);
	
		lastTurn = game.getTurn();
		
		return nextMove;
	}

	public void cancel() {
		synchronized (lock) {
			stopping = true;
			lock.notifyAll();
		}
	}
}