package com.exercise.jsonparser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MergeJson {
	@SuppressWarnings({ "resource", "unchecked" })
	public static void main(String[] args) throws IOException, ParseException {
		
		//Get Inputs
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Folder Path where all the JSON files are stored");
		//  C:/Users/Harikumar G/workspace/JsonMerge/resources/files/
		String path = sc.nextLine();
		System.out.println("Enter the Input File Base Name");
		String data = sc.nextLine();
		System.out.println("Enter the Output File Base name");
		String out = sc.nextLine();
		System.out.println("Enter the maximum file size that each merged file is allowed");
		double size = sc.nextDouble();
		
		//For input file counter
		int i = 1; 
		// For Output file counter
		int j = 1; 
		HashMap<Object, JSONArray> mp = new HashMap<>();
		
		//Reading the files till file not found occurs
		while (true) 
		{	
			try
			{
				FileReader f = new FileReader(path+data+i+".json"); 
				JSONParser parser = new JSONParser();
				
				//Parsing the JSON file
				Object obj = parser.parse(f); 
				JSONObject jsonObject = (JSONObject) obj;
			
				//For all the root keys
				for (Object key : jsonObject.keySet()) 
				{
					//If already exist in the map then merge the array
					if(mp.containsKey(key))
					{
						JSONArray k = (JSONArray)jsonObject.get(key);
						JSONArray k1 = mp.get(key);
						for(int m = 0; m < k.size(); m++)
						{
							k1.add(k.get(m));
						}
						mp.put(key,k1);
					}
					 //Else create a new key and put the JSON array as value
					else
					{
						mp.put(key,(JSONArray) jsonObject.get(key));
					}
				}
				i++;
			}
			catch(FileNotFoundException e)
			{
				System.out.println("Read all input files till "+data+(i-1)+".json");
				break;
			}
		}
		//For each key create a new file and write the JSON array
		for(HashMap.Entry<Object,JSONArray> entry : mp.entrySet())
		{
			File f = new File(path+out+j+".json");
			FileWriter fw = new FileWriter(f);
			fw.write("{\""+entry.getKey()+"\":"+entry.getValue()+"}");
			fw.close();
			//Check the file length is less than the max file size
			double fileSize = f.length();
			if(fileSize <= size) 
			{
				j++;
			}
			// If not then delete the file since it is not allowed
			else 
			{
				System.out.println("Merged file size "+fileSize+" bytes exceeded the max size "+size+" bytes");
				f.delete();
			}
		}		
		System.out.println("Json merge is done successfully");
	}

}
