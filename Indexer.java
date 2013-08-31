import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
import java.util.Scanner;

class Indexer {

	//constructor
	public Indexer()
	{

	}
	
	private void readFile(File file)
    {
        try 
        {
            FileReader fr = new FileReader(file);
            
            try 
            {
                Scanner scan = new Scanner(fr);
                
                int lineNumber = 0;
        
                while (scan.hasNextLine()) 
                {
                    lineNumber++;
                    String line = scan.nextLine();    // Read one line of the text file into a string
                    
                    String[] parts = line.split(" ");  // Split the line by space into a String array
                    System.out.println(parts[1]+" "+parts[2]);
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
		
		Iterator<File> it = corpusTextFiles.iterator();
		while (it.hasNext())
		{
			File textFile = it.next();
			readFile(textFile);
		}
	}
  
}