/**
 * 
 */
package ch.hszt.connectfour.io;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.aspectj.weaver.tools.ISupportsMessageContext;

/**
 * Base class for serializers. 
 * With support of this class instances implementing the {@link Serial} interface can be saved persistently.
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 */
public abstract class Serializer
{
	//protected Map<Integer, Pair<Serial, Map<Integer,Serial>>> objectTree;
	protected SerialObjectTree objectTree;
	
	protected Serializer(Serial serial) throws Exception
	{
		objectTree = bulidObjectTree(serial);
	}	
	
	protected SerialObjectTree bulidObjectTree(Serial serial) throws Exception
	{
		SerialObjectTree tree = new SerialObjectTree();
		
		int serialId = 0;
		
		findSerials(serial, serialId, tree);
		
		return tree;
	}
	
	private void findSerials(Serial serial, int currentId, SerialObjectTree tree) throws Exception
	{
		tree.add(currentId, serial);
		
		currentId++;
		
		for (Field field : serial.getClass().getDeclaredFields())
		{
			if (isSerial(field))
			{
				field.setAccessible(true);
				
				Serial childSerial = (Serial) field.get(serial);
				
				if (childSerial != null)
				{
					findSerials(childSerial, currentId, tree);
				}
			}
		}
	}
	
	private boolean isSerial(Field field)
	{
		if (isSerializableField(field))
		{
			for (Class<?> c : field.getType().getInterfaces())
			{
				if (c.getName().equals(Serial.class.getName()))
				{
					return true;
				}
			}
		}	
		
		return false;
	}
	
	private boolean isSerializableField(Field field)
	{
		int modifiers = field.getModifiers();
		return !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers);
	}
//	
//	protected SerialObject createSerialObject(Serial serial)
//	{
//		
//	}
	
	class SerialObjectTree implements Iterable<SerialObjectTreeEntry>
	{
		private Map<Integer, SerialObjectTreeEntry> treeEntries;
		
		SerialObjectTree()
		{
			treeEntries = new TreeMap<Integer, SerialObjectTreeEntry>();
		}
		
		SerialObjectTreeEntry add(int id, Serial serial)
		{
			SerialObjectTreeEntry entry = null;
			
			if (!treeEntries.containsKey(id))
			{
				entry = new SerialObjectTreeEntry(id, serial);
				treeEntries.put(id, entry);
			}
			
			return entry;
		}

		public Iterator<SerialObjectTreeEntry> iterator()
		{
			return treeEntries.values().iterator();
		}
	}
	
	class SerialObjectTreeEntry extends SerialObjectTree
	{
		private final int id;
		private final Serial serial;
		private Map<Integer, SerialObjectTreeEntry> childEntries;
		
		SerialObjectTreeEntry(int id, Serial serial)
		{
			this.id = id;
			this.serial = serial;
			childEntries = new TreeMap<Integer, SerialObjectTreeEntry>();
		}
		
		SerialObjectTreeEntry add(int id, Serial serial)
		{
			SerialObjectTreeEntry entry = null;
			
			if (!childEntries.containsKey(id))
			{
				entry = new SerialObjectTreeChildEntry(id, serial, this);
				childEntries.put(id, entry);
			}
			
			return entry;
		}
		
		int getId()
		{
			return id;
		}
		
		Serial getSerial()
		{
			return serial;
		}
		
		boolean hasChildren()
		{
			return childEntries.size() > 0;
		}
		
		Collection<SerialObjectTreeEntry> getChildren()
		{
			return childEntries.values();
		}
	}
	
	class SerialObjectTreeChildEntry extends SerialObjectTreeEntry
	{
		private SerialObjectTreeEntry parent;
		
		SerialObjectTreeChildEntry(int id, Serial serial, SerialObjectTreeEntry parent)
		{
			super(id, serial);
			this.parent = parent;
		}
	}
}
