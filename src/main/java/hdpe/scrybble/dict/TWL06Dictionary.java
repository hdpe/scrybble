package hdpe.scrybble.dict;

/**
 * @author Ryan Pickett
 *
 */
public class TWL06Dictionary extends AbstractResourceLoadingDictionary {

	/**
	 * 
	 */
	public TWL06Dictionary() {
		super(TWL06Dictionary.class.getResource("Lexicon.txt"), true);
	}
}