package hdpe.scrybble.util;

/**
 * @author Ryan Pickett
 *
 */
public class InvalidCharacterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private char c;
	private String str;
	
	/**
	 * @param c
	 */
	public InvalidCharacterException(char c) {
		this.c = c;
	}
	
	public String getMessage() {
		return "Invalid character '" + c + "' in string '" + str + "'";
	}
	
	/**
	 * @param str
	 */
	public void setTestString(String str) {
		this.str = str;
	}

}
