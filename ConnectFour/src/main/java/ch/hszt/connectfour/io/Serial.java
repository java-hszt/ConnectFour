/**
 * 
 */
package ch.hszt.connectfour.io;

/**
 * Implementing classes are flagged as serializable and can be made persistent using 
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public interface Serial
{
	/**
	 * Saves the current {@link Serial}.
	 * @param obj - The {@link SerialObject} to be serialized.
	 */
	void save(SerialObject obj);
	
	/**
	 * Reconstructs the {@link Serial} from specified {@link SerialObject}
	 * @param obj - The {@link SerialObject} to be deserialized.
	 * @return - The reconstructed object as {@link Serial}.
	 */
	Serial load(SerialObject obj);
}
