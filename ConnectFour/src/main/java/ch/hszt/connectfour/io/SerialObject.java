/**
 * 
 */
package ch.hszt.connectfour.io;

import java.util.Date;
import java.util.List;

/**
 * Transforms any {@link Serial} instance into serialized instance.
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public interface SerialObject
{
	void saveString(String value, String member);
	void saveBoolean(Boolean value, String member);
	void saveByte(Byte value, String member);
	void saveShort(Short value, String member);
	void saveInt(Integer value, String member);
	void saveLong(Long value, String member);
	void saveFloat(Float value, String member);
	void saveDouble(Double value, String member);
	void saveDate(Date date, String member);
	<T extends Serial> void saveSerial(T serial);
	<T extends Serial> void saveList(List<T> serialList);	
	
	String loadString(String member);
	Boolean loadBoolean(String member);
	Byte loadByte(String member);
	Short loadShort(String member);
	Integer loadInt(String member);
	Long loadLong(String member);
	Float loadFloat(String member);
	Double loadDouble(String member);
	Date loadDate(String member);
	Serial loadSerial(String member);
	<T extends Serial> List<T> loadList(String member);
}
