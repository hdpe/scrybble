package hdpe.scrybble.persist;

import hdpe.scrybble.game.Game;
import hdpe.scrybble.game.GameContext;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author Ryan Pickett
 *
 */
public abstract class GameIO {

	private GameIO() {
		
	}
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static SavedGame load(File file) throws IOException {
		try {
			Unmarshaller u = JAXBContext.newInstance(SavedGame.class).createUnmarshaller();
			return (SavedGame)u.unmarshal(file);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * @param gameContext
	 * @param game
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static SavedGame save(GameContext gameContext, Game game, File file) throws IOException {
		SavedGame saveGame = new SavedGame(gameContext, game);
		try {
			Marshaller m = JAXBContext.newInstance(SavedGame.class).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(saveGame, file);
			return saveGame;
		} catch (Exception e) {
			throw new IOException(e);
		}
	} 
}
