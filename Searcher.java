import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;

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
		// First check if the query terms are in the inverted list to begin with,
		// if its not in the corpus no results will be found and can just stop
		boolean queryInIndex = false;
		for (String query : queryTerms)
		{
			if (invIndex.termIDFs.containsKey(query))
			{
				queryInIndex = true;
				// can break out of the loop as we have to proceed with cosine sim calculations now
				break;
			}
		}
		
		if (queryInIndex)
		{
			// Now calculate all the cosine similarities for all documents that contain at least one query term
			// We can build up the dot-product query term by query term and then divide through by vector norms
			HashMap<String, Double> queryDocumentDotProducts = new HashMap<String, Double>();
			for (String query : queryTerms)
			{
				//Get all the documents and their term frequencies for this query term
				if (invIndex.invertedIndexTFs.containsKey(query))
				{
					HashMap<String, Integer> docTFs = invIndex.invertedIndexTFs.get(query);
					
					//iterate through each document, make tf-idf, take dot product 
					//and add to previous value for document or initialise the dotProducts HashMap
					for (Map.Entry<String, Integer> entry : docTFs.entrySet())
					{
					    
						String docName = entry.getKey();
						int termFreq = entry.getValue();
						/*
						double tfIDF = termFreq*invIndex.termIDFs.get(query);
						//calculate query term tf*idf
						
						
						double termDotProduct = tfIDF * queryTfIdf;
						*/
						System.out.println(docName);
					}
				}
			}
			
		
		} else {
			System.out.println("Your search produced no results!");
		}
		
	}
}