/**
 * 
 */
package ch.hszt.connectfour.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import ch.hszt.connectfour.model.game.GameStatistic;
import ch.hszt.connectfour.util.DateHelper;

/**
 * Saves objects persistent in a XML file.
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public class XmlSerializer extends Serializer
{
	private Serial serial;
	private Document document;
	
	public XmlSerializer(Serial serial) throws Exception
	{
		super(serial);
		
		this.serial = serial;
		document = createXml();
	}
	
	public void save(String filePath) throws IOException
	{
		XMLOutputter writer = new XMLOutputter();
		File file = new File(filePath);
		
		file = appendFileTime(file);
		
		if (!file.exists())
		{
			file.createNewFile();
		}
		
		OutputStream stream = new FileOutputStream(file);
		
		writer.output(document, stream);		
		stream.close();
	}
	
	public Serial load(String filePath) throws IOException
	{
		//TODO: implement loading of objects
		return null;
	}
	
	private File appendFileTime(File file)
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd_HHmmss");
		
		String orgName = file.getName();
		String fileTime = String.format("_%s.xml", f.format(DateHelper.now()));
		String newName = orgName.replaceAll(".xml", "").concat(fileTime);
		String newPath = file.getParent() + File.separator + newName;
		
		return new File(newPath);
	}
	
	private Document createXml()
	{
		Document doc = new Document(new Element("ConnectFourGame"));
		
		for (SerialObjectTreeEntry entry : objectTree)
		{
			doc.addContent(addSimpleObjectElement(entry));
		}
		
		return doc;
	}
	
	private Element addSimpleObjectElement(SerialObjectTreeEntry entry)
	{
		Element element = new Element("Object");
		element.setAttribute("id", Integer.toString(entry.getId()));
		
		if (entry.hasChildren())
		{			
			for (SerialObjectTreeEntry chlidEntry : entry.getChildren())
			{
				element.addContent(addSimpleObjectElement(chlidEntry));
			}
		}
		
		return element;
	}
}
