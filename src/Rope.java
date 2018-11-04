/***
 * Rope object, represents a rope with a length
 * @author Joseph Whitten
 * @version 2
 * @since 14/12/15
 */

public class Rope {

	//Store the length of the rope
	private int length;
	
	//Constructor 
	public Rope(int length) {
		setLength(length);
	}

	/***
	 * Return the length of this rope
	 * @return length
	 */
	public int getLength() {
		return length;
	}

	/***
	 * Set the length of the rope
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}
}
