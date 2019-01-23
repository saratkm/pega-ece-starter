package managers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class CsvParser
{
	HashMap<String, HashMap<String, String>> table = new HashMap<String, HashMap<String, String>>();
	HashMap<String, String> row = new HashMap<String, String>();
	public CsvParser(String filePath)
	{		
		try
		{
			CSVReader reader = new CSVReader(new FileReader(filePath), ',' );
			List<String[]> csvEntries = reader.readAll();
			Iterator<String[]> Iter = csvEntries.iterator();

			String [] headerArray;
			String [] entryArray;

			Iter.hasNext();
			headerArray = (String[])Iter.next();

			while ( Iter.hasNext())
			{
				HashMap<String, String> columnEntry = new HashMap<String, String>();
				
				entryArray = (String[])Iter.next();               
				for ( int i = 0; i < entryArray.length; i++ )
				{
					columnEntry.put(headerArray[i], entryArray[i]);
				}

				table.put(entryArray[1], columnEntry);
			}			
		}
		catch ( FileNotFoundException fnfe )
		{
			fnfe.printStackTrace();
		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace();
		}

	}
	
	public CsvParser(String filePath, String rowKey)
	{		
		try
		{
			CSVReader reader = new CSVReader(new FileReader(filePath), ',' );
			List<String[]> csvEntries = reader.readAll();
			Iterator<String[]> Iter = csvEntries.iterator();

			String [] headerArray;
			String [] entryArray;

			Iter.hasNext();
			headerArray = (String[])Iter.next();

			while ( Iter.hasNext())
			{
				HashMap<String, String> columnEntry = new HashMap<String, String>();
				
				entryArray = (String[])Iter.next();               
				for ( int i = 0; i < entryArray.length; i++ )
				{
					columnEntry.put(headerArray[i], entryArray[i]);
				}

				table.put(entryArray[1], columnEntry);
			}			
		}
		catch ( FileNotFoundException fnfe )
		{
			fnfe.printStackTrace();
		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace();
		}
		
		row = table.get(rowKey);

	}
	
	public String getEntry(String rowKey, String columnKey) {
		return table.get(rowKey).get(columnKey);
	}
	
	public String getEntry(String columnKey) {
		return row.get(columnKey);
	}
	
	public void loadRow(String rowKey) {
		row = table.get(rowKey);
	}
	
	public void displayTable() {
	    Iterator it = table.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }	
	}
	
	public void displayRow() {
	    Iterator it = row.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }	
	}
	
	public HashMap<String, HashMap<String, String>> getTable() {
		return table;
	}
	
	public HashMap<String, String> getRow(String rowKey) {
		return table.get(rowKey);
	}
	
	public boolean isColumnKeyExists(String rowKey, String columnKey) {
		return table.get(rowKey).containsKey(columnKey);
	}
	
	public boolean isColumnKeyExists(String columnKey) {
		return row.containsKey(columnKey);
	}
}