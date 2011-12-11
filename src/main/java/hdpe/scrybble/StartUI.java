package hdpe.scrybble;

import hdpe.scrybble.config.Configuration;
import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.GameContext;
import hdpe.scrybble.game.GameMode;
import hdpe.scrybble.game.GameResult;
import hdpe.scrybble.game.GameResultType;
import hdpe.scrybble.game.Player;
import hdpe.scrybble.game.PlayerImpl;
import hdpe.scrybble.game.PlayerStrategy;
import hdpe.scrybble.game.event.GameAdapter;
import hdpe.scrybble.game.event.GameDrawStartTileEvent;
import hdpe.scrybble.game.event.GameFirstPlayerDeterminedEvent;
import hdpe.scrybble.game.event.GameIllegalMoveEvent;
import hdpe.scrybble.game.event.GameMoveEvent;
import hdpe.scrybble.game.event.GameResultEvent;
import hdpe.scrybble.game.event.GameStoppedEvent;
import hdpe.scrybble.game.event.GameTurnEndEvent;
import hdpe.scrybble.game.event.GameTurnStartEvent;
import hdpe.scrybble.persist.GameIO;
import hdpe.scrybble.ui.GameFrame;
import hdpe.scrybble.ui.GameFrameAdapter;
import hdpe.scrybble.ui.RackStockingPromptFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * @author Ryan Pickett
 *
 */
public class StartUI {
	
	private GameContext gameContext;
	private Game game; 
	private GameFrame frame;
	
	public static void main(String[] args) throws Exception {
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());		
		new StartUI().start();
	}
	
	public StartUI() {
		
		gameContext = new GameContext();
		addGameListener();
		
		frame = new GameFrame(gameContext);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);		
		frame.addGameFrameListener(new GameFrameAdapter() {

			public void onDoConfigurationChange(Configuration configuration) {
				gameContext.setConfiguration(configuration);
				frame.setGameContext(gameContext);
			}
			
			public void onDoNew() {
				stopGameAndExecute(new Runnable() {					
					public void run() {
						frame.clear();
						frame.setConfigControlsEnabled(true);		
						frame.setGoControlEnabled(true);
						frame.setStopControlEnabled(false);
					}
				});
			}

			public void onDoOpen(final File file) {		
				stopGameAndExecute(new Runnable() {					
					public void run() {
						frame.clear();
						frame.setFile(file);
						
						new Thread(new Runnable() {					
							public void run() {
								try {
									game = gameContext.loadGame(file);
									updateUI(new Runnable() {							
										public void run() {
											frame.setGameContext(gameContext);
											frame.setConfigControlsEnabled(false);
											frame.setGoControlEnabled(true);
											frame.setStopControlEnabled(false);
										}
									});
									game.load();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				});				
			}

			public void onDoSave(File file) {
				try {
					GameIO.save(gameContext, game, file);
					frame.setFile(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void onDoStart(final GameMode mode) {
				frame.setConfigControlsEnabled(false);
				frame.setGoControlEnabled(false);
				frame.setStopControlEnabled(true);
				
				new Thread(new Runnable() {					
					public void run() {
						if (game == null || game.isFinished()) {
							game = gameContext.newGame();
						}
						game.setMode(mode);
						frame.setMode(mode);
						startGame();
					}
				}).start();					
			}

			public void onDoStop() {
				game.stop();
			}

			public void onPlayerAdded(String playerName, PlayerStrategy strategy) {
				gameContext.getPlayers().add(new PlayerImpl(playerName, strategy));
			}

			public void onPlayerChanged(int idx, String playerName,
					PlayerStrategy strategy) {
				Player player = gameContext.getPlayers().get(idx);
				player.setName(playerName);
				player.setStrategy(strategy);
			}

			public void onPlayerRemoved(int idx) {
				gameContext.getPlayers().remove(idx);
			}
		});	
	}	

	public void start() {
		updateUI(new Runnable() {
			public void run() {
				frame.setVisible(true);	
			}
		});
	}
	
	public void addGameListener() {
		gameContext.addGameListener(new GameAdapter() {

			public void onTurnStart(final GameTurnStartEvent event) {
				updateUI(new Runnable() {					
					public void run() {
						frame.setCurrentPlayer(event.getPlayer());
					}
				});
			}
			
			public void onTurnEnd(final GameTurnEndEvent event) {
				updateUI(new Runnable() {					
					public void run() {
						frame.setCurrentPlayer(event.getPlayer());						
					}
				});
			}
			
			public void onEnd(GameResultEvent event) {
				updateUI(new Runnable() {					
					public void run() {
						frame.setCurrentPlayer(null);
						frame.setConfigControlsEnabled(false);
						frame.setGoControlEnabled(false);
						frame.setStopControlEnabled(false);						
					}
				});
			}

			public void onStopped(GameStoppedEvent evt) {
				updateUI(new Runnable() {					
					public void run() {
						frame.setCurrentPlayer(null);
//						frame.setConfigControlsEnabled(false);
						frame.setGoControlEnabled(true);
						frame.setStopControlEnabled(false);						
					}
				});
			}

			public void onDrawStartTile(final GameDrawStartTileEvent evt) {
				updateUI(new Runnable() {					
					public void run() {
						frame.setDirty();
						frame.log(evt.getPlayer() + ": Draw " + evt.getTile(), evt.getPlayer());
					}
				});
			}

			public void onFirstPlayerDetermined(final GameFirstPlayerDeterminedEvent evt) {
				updateUI(new Runnable() {					
					public void run() {
						frame.log("First player: " + evt.getPlayer().getName());
					}
				});
			}

			public void onMove(final GameMoveEvent event) {				
				updateUI(new Runnable() {					
					public void run() {
						frame.setDirty();
						frame.applyMove(event.getMove());
						frame.log(event.getMove(), event.getPlayer());
					}
				});
			}

			public void onIllegalMove(final GameIllegalMoveEvent event) {
				updateUI(new Runnable() {					
					public void run() {
						frame.log(event.getException().getMessage(), 
								event.getPlayer());
					}
				});
			}			
		});		
	}
	
	protected void startGame() {
		
		final RackStockingPromptFrame promptFrame = new RackStockingPromptFrame(frame);		
		promptFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showConfirmDialog(frame, "Continuing will stop the current game.", 
						"Please Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null);
				
				if (confirm == JOptionPane.OK_OPTION) {
					promptFrame.dispose();
					game.stop();
				}
			}			
		});
		
		game.setRackStockingProvider(promptFrame);			
		
		updateUI(new Runnable() {
			public void run() {
				frame.setConfigControlsEnabled(false);	
			}
		});
		
		List<GameResult> results = new ArrayList<GameResult>();
		
		final GameResult result = game.start();
		
		if (result != null) {
			results.add(result);			
			logGameResult(result);
		}
	}
	
	protected void logGameResult(final GameResult result) {
		updateUI(new Runnable() {
			public void run() {
				for (Entry<Player, Integer> e : result.getPlayerScores()
						.entrySet()) {
					frame.log(e.getKey().toString() + ": " + e.getValue());
				}
				
				frame.log(result.getType() + " " + result
						.getWinningScorePlayers() + " (" + 
						result.getWinningScore() + ")");
			}
		});
	}

	protected void logGameResults(List<GameResult> results) {
		final Map<Player, Integer> playerWins = new HashMap<Player, Integer>();
		
		for (GameResult result : results) {
			if (result.getType() == GameResultType.WIN) {
				Player winner = result.getWinningScorePlayers().iterator().next();
				Integer n = playerWins.get(winner);
				if (n == null) {
					n = 0;
				}
				playerWins.put(winner, n + 1);
			}
		}
		
		updateUI(new Runnable() {
			public void run() {
				frame.log(playerWins.toString());
			}
		});
	}
	
	protected void stopGameAndExecute(final Runnable runnable) {
		
		if (game == null || !game.isRunning()) {
			
			game = null;
			runnable.run();
			
		} else if (JOptionPane.showConfirmDialog(frame, "Are you sure you " +
				"want to quit this game?", "Please Confirm", 
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
			
			game.addGameListener(new GameAdapter() {

				public void onEnd(GameResultEvent event) {
					stopAndExecute();
				}

				public void onStopped(GameStoppedEvent evt) {
					stopAndExecute();
				}
				
				private void stopAndExecute() {
					game.removeGameListener(this);	
					game = null;
					runnable.run();										
				}				
			});
			
			game.stop();
		}
	}	

	private static void updateUI(Runnable runnable) {
		updateUI(runnable, false);
	}
	
	private static void updateUI(Runnable runnable, boolean async) {
		if (async) {
			SwingUtilities.invokeLater(runnable);
		} else {
			try {
				SwingUtilities.invokeAndWait(runnable);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}