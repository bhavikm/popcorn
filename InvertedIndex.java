import java.util.Map;
import java.util.HashMap;

class InvertedIndex {

	// term -> {doc1 -> term frequency ,doc2 -> term frequency , ...}
	public HashMap<String, HashMap<String, Integer>> invertedIndexTFs;
	// term -> inverse-document-frequency value
	public HashMap<String, Double> termIDFs;
	// docName -> vectorNorm value (pre-computed to use in cosine similarity calculations)
	public HashMap<String, Double> docVectorNorms;
	
	private static String indexFileName = "index.txt";
  	
	public InvertedIndex()
	{
		invertedIndexTFs = new HashMap<String, HashMap<String, Integer>>();
		termIDFs = new HashMap<String, Double>();
		docVectorNorms =  new HashMap<String, Double>();
	}
	
	// Expects file with structure:
	/*  term,doc1,doc1-term-freq,doc2,doc2-term-freq,...,term-inverse-doc-frequency
	 *  term,doc1,doc1-term-freq,doc2,doc2-term-freq,...,term-inverse-doc-frequency
	 *  term,doc1,doc1-term-freq,doc2,doc2-term-freq,...,term-inverse-doc-frequency
	 *  ...
	 *
	 * Converts this file into the class data structures:
	 *     - an inverted index with document term frequencies
	 *     - a lookup for term inverse document frequencies (IDFs)
	 *     - pre-computed document vector norms:
	 *          (sum the squares of all tf-idf values for all terms in a doc and take the square root)
	 */
	public constructInvertedIndexFromFile(String indexDirectory)
	{
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
	}
}