import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;


class Searcher {

	private InvertedIndex invIndex;
  	private int numbSearchResults;
	
	public Searcher(InvertedIndex invertedIn, int topSearchResults)
	{
		invIndex = invertedIn;
		numbSearchResults = topSearchResults;
	}
	
	public void searchByCosineSim(String[] queryTerms)
	{
		//Tokenize the raw query
		String queryAsLine = "";
		for (String query : queryTerms)
		{
			queryAsLine = queryAsLine + " " + query;
		}
		queryAsLine = queryAsLine.trim();
		ArrayList<String> lineForTokenizer = new ArrayList<String>();
		lineForTokenizer.add(queryAsLine);
		Tokenizer tokenizer = new Tokenizer();
		HashMap<String, Integer> queryTFs = tokenizer.tokenize(lineForTokenizer);
		ArrayList<String> tokenizedQuery = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : queryTFs.entrySet())
		{
			String token = entry.getKey();
			int termFreq = entry.getValue();
			tokenizedQuery.add(token);
		}
		
		if (queryTFs.size() > 0)
		{
			double queryVectorNorm = 0.0;
			int numberCorpusDocs = invIndex.corpusDocs.size();
			
			// Now calculate all the cosine similarities for all documents that contain at least one query term
			// We can build up the dot-product query term by query term and then divide through by vector norms
			HashMap<String, Double> queryDocumentDotProducts = new HashMap<String, Double>();
			for (String query : tokenizedQuery)
			{
				int queryTF = queryTFs.get(query);
				
				//Get all the documents and their term frequencies for this query term
				if (invIndex.invertedIndexTFs.containsKey(query))
				{
					HashMap<String, Integer> docTFs = invIndex.invertedIndexTFs.get(query);
					double idf = invIndex.termIDFs.get(query);
					double queryTfIdf = 1.0*queryTF*idf;
					
					//iterate through each document, make tf-idf, take dot product 
					//and add to previous value for document or initialise the dotProducts HashMap
					for (Map.Entry<String, Integer> entry : docTFs.entrySet())
					{
						
						String docName = entry.getKey();
						int termFreq = entry.getValue();
						
						double tfIDF = termFreq*idf;
						
						double termDotProduct = tfIDF * queryTfIdf;
						
						if (queryDocumentDotProducts.containsKey(docName))
						{
							queryDocumentDotProducts.put(docName, queryDocumentDotProducts.get(docName) + termDotProduct);
						} else{
							queryDocumentDotProducts.put(docName, termDotProduct);
						}
					}
					
					//add to query vector norm
					queryVectorNorm += queryTfIdf*queryTfIdf;
					
				} else {
					queryVectorNorm += Math.log(numberCorpusDocs)*queryTF*Math.log(numberCorpusDocs)*queryTF;
				}
			}
		
		
			// Now build up the cosine similarity scores for each document
			// Use a PriorityQueue to automatically store documents in max order
			PriorityQueue<String> queryDocCosineSimSorted = new PriorityQueue<String>(queryDocumentDotProducts.size(), new DoubleInStringComparator());
			for (Map.Entry<String, Double> entry : queryDocumentDotProducts.entrySet())
			{
				String docName = entry.getKey();
				double dotProduct = entry.getValue();	
				double cosineSimilarity = dotProduct / (Math.sqrt(invIndex.docVectorNormsSquared.get(docName))*Math.sqrt(queryVectorNorm));
				
				String outputString = docName+","+String.format("%.3f",cosineSimilarity);
				
				queryDocCosineSimSorted.add(outputString);
			}
			//Print out all the documents in order of cosine similarity
			for (int i = 0; i < queryDocumentDotProducts.size() && i < numbSearchResults; i++)
			{
				System.out.println(queryDocCosineSimSorted.poll());
			}
		} else {
			System.out.println("Your search produced no results.");
		}

	}
}

