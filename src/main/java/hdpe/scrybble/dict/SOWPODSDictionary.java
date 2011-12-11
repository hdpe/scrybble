package hdpe.scrybble.dict;

/**
 * @author Ryan Pickett
 *
 */
public class SOWPODSDictionary extends AbstractResourceLoadingDictionary {

	/**
	 * 
	 */
	public SOWPODSDictionary() {
		super(SOWPODSDictionary.class.getResource("sowpods.unix.txt"), false);
	}
}
