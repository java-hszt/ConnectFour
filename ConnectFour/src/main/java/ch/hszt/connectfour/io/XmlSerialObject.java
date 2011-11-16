/**
 * 
 */
package ch.hszt.connectfour.io;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

/**
 * Represents an XML-based serialized object.
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 *
 */
public class XmlSerialObject implements SerialObject
{
	private Element element;
	
	XmlSerialObject(Serial serial, int version)
	{
		element = new Element(serial.getClass().getSimpleName());
		element.setAttribute("type", serial.getClass().getName());
		element.setAttribute("version", Integer.toString(version));
	}
	
	public Element getElement()
	{
		return element;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveString(java.lang.String, java.lang.String)
	 */
	public void saveString(String value, String member)
	{
		Element childElement = createMemberElement(value, member);
		childElement.setAttribute("value", value);
		
		element.addContent(childElement);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveBoolean(java.lang.Boolean, java.lang.String)
	 */
	public void saveBoolean(Boolean value, String member)
	{
		Element childElement = createMemberElement(value, member);
		childElement.setAttribute("value", Boolean.toString(value).toLowerCase());
		
		element.addContent(childElement);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveByte(java.lang.Byte, java.lang.String)
	 */
	public void saveByte(Byte value, String member)
	{
		Element childElement = createMemberElement(value, member);
		childElement.setAttribute("value", value.toString());

		element.addContent(childElement);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveShort(java.lang.Short, java.lang.String)
	 */
	public void saveShort(Short value, String member)
	{
		Element childElement = createMemberElement(value, member);
		childElement.setAttribute("value", value.toString());

		element.addContent(childElement);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveInt(java.lang.Integer, java.lang.String)
	 */
	public void saveInt(Integer value, String member)
	{
		Element childElement = createMemberElement(value, member);
		childElement.setAttribute("value", value.toString());

		element.addContent(childElement);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveLong(java.lang.Long, java.lang.String)
	 */
	public void saveLong(Long value, String member)
	{
		Element childElement = createMemberElement(value, member);
		childElement.setAttribute("value", value.toString());

		element.addContent(childElement);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveFloat(java.lang.Float, java.lang.String)
	 */
	public void saveFloat(Float value, String member)
	{
		Element childElement = createMemberElement(value, member);
		childElement.setAttribute("value", value.toString());

		element.addContent(childElement);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveDouble(java.lang.Double, java.lang.String)
	 */
	public void saveDouble(Double value, String member)
	{
		Element childElement = createMemberElement(value, member);
		childElement.setAttribute("value", value.toString());

		element.addContent(childElement);
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveDate(java.util.Date, java.lang.String)
	 */
	public void saveDate(Date value, String member)
	{
		saveLong(Long.valueOf(value.getTime()), member);
	}	

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveSerial(ch.hszt.connectfour.io.Serial, java.lang.String)
	 */
	public <T extends Serial> void saveSerial(T serial)
	{
		serial.save(new XmlSerialObject(serial, 0));		
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#saveList(java.util.List, java.lang.String)
	 */
	public <T extends Serial> void saveList(List<T> serialList)
	{
		for (Serial serial : serialList)
		{
			saveSerial(serial);
		}		
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadString(java.lang.String)
	 */
	public String loadString(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadBoolean(java.lang.String)
	 */
	public Boolean loadBoolean(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadByte(java.lang.String)
	 */
	public Byte loadByte(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadShort(java.lang.String)
	 */
	public Short loadShort(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadInt(java.lang.String)
	 */
	public Integer loadInt(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadLong(java.lang.String)
	 */
	public Long loadLong(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadFloat(java.lang.String)
	 */
	public Float loadFloat(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadDouble(java.lang.String)
	 */
	public Double loadDouble(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadDate(java.lang.String)
	 */
	public Date loadDate(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadSerial(java.lang.String)
	 */
	public Serial loadSerial(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.hszt.connectfour.io.SerialObject#loadList(java.lang.String)
	 */
	public <T extends Serial> List<T> loadList(String member)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	private Element createMemberElement(Object obj, String member)
	{
		Element memberElement = new Element("member");
		memberElement.setAttribute("name", member);
		memberElement.setAttribute("type", obj.getClass().getName());
		
		return memberElement;
	}
}
