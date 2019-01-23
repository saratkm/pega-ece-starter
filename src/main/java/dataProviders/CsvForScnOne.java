package dataProviders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvForScnOne {
	List<List<String>> csvTable = new ArrayList<List<String>>();
	public CsvForScnOne(String filePath) {
        BufferedReader br = null;
        String line = "";
        String delimiter = ",";

        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
            	List<String> row = new ArrayList<String>();
                String[] input = line.split(delimiter);
                for (int i = 0; i < input.length; i++) {
                	row.add(input[i]);
                }
                csvTable.add(row);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	public List<List<String>> getCsvTable() {
		return csvTable;
	}
	
	public void displayCsvTable() {
		for (int i = 0;i < csvTable.size();i++) {
        	for (int k = 0; k < csvTable.get(i).size()-1; k++) {
        		System.out.print(csvTable.get(i).get(k) + ", ");
        	}
        	System.out.println("");
        }
	}
	
	static public void displayCsvTable(List<List<String>> table) {
		for (int i = 0;i < table.size();i++) {
        	for (int k = 0; k < table.get(i).size()-1; k++) {
        		System.out.print(table.get(i).get(k) + ", ");
        	}
        	System.out.println("");
        }
	}
	
	static public List<String> getRow(List<List<String>> table, String identifier) {
		List<String> row = new ArrayList<String>();
		for (int i = 0; i < table.size();i++) {
			if (table.get(i).get(1).equals(identifier)){
				for (int k = 2; k < table.get(i).size(); k++) {
					row.add(table.get(i).get(k));
				}
				break;
			}
		}
		return row;	
	}
}
