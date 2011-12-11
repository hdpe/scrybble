package hdpe.scrybble.dict;

/**
 * @author Ryan Pickett
 *
 */
public class LinuxDictionary extends AbstractResourceLoadingDictionary {

	/**
	 * 
	 */
	public LinuxDictionary() {
		super(LinuxDictionary.class.getResource("words"), false);
	}
}