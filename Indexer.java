import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

class Indexer {

	private static String indexFileName = "index.txt";
	private Map<String, Integer> docFrequencyOfTerm;

	//constructor
	public Indexer()
	{
		docFrequencyOfTerm = new HashMap<String, Integer>();
	}
	
	private ArrayList<String> readFile(File file)
    {
		ArrayList<String> fileLines = new ArrayList<String>(); 
		
        try 
        {
            FileReader fr = new FileReader(file);
            
            try 
            {
                Scanner scan = new Scanner(fr);
				
                int lineNumber = 0; //for debug
        
                while (scan.hasNextLine()) 
                {
                    lineNumber++; //for debug
                    String line = scan.nextLine();    // Read one line of the text file into a string
                    fileLines.add(line.trim());
                    //String[] parts = line.split(" ");  // Split the line by space into a String array
                    //System.out.println(parts[1]+" "+parts[2]);
                }
            }
            finally
            {
               fr.close();
            }
            
        } 
        catch (FileNotFoundException e) 
        {
            System.out.print("File not found\n");
        }
        catch (IOException e)
        {
            System.out.print("Unexpected I/O exception\n");
        }
		
		return fileLines;
    }
	
	private ArrayList<File> textFilesInDirectory(String directoryPath)
	{
		File folder = new File(directoryPath);
		File[] files = folder.listFiles();
		ArrayList<File> textFiles = new ArrayList<File>(); 
		
		for (int i = 0; i < files.length; i++) 
		{
			if (files[i].isFile()) 
			{
				String fileName = files[i].getName();
				if (fileName.endsWith(".txt") || fileName.endsWith(".TXT"))
				{
					System.out.println(fileName);
					textFiles.add(files[i]);
				}
			}
		}
		
		return textFiles;
	}
	
	public void makeIndex(String collection_dir, String index_dir)
	{
		ArrayList<File> corpusTextFiles = textFilesInDirectory(collection_dir);
		Tokenizer tokenizer1 = new Tokenizer();
		int numbDocuments = 0;
		
		Iterator<File> it = corpusTextFiles.iterator();
		while (it.hasNext())
		{
			File textFile = it.next();
			ArrayList<String> fileLines = readFile(textFile);
			if (fileLines.size() > 0)
			{
				numbDocuments += 1;
				ArrayList<String> tokens = tokenizer1.tokenize(fileLines);
			}
		}
	}
  
}