import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.io.*;
import java.util.Scanner;
import java.util.*;

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

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
			double queryVectorNorm = 0.0;
			int numberCorpusDocs = invIndex.termIDFs.size();
			
			// Now calculate all the cosine similarities for all documents that contain at least one query term
			// We can build up the dot-product query term by query term and then divide through by vector norms
			HashMap<String, Double> queryDocumentDotProducts = new HashMap<String, Double>();
			for (String query : queryTerms)
			{
				//Get all the documents and their term frequencies for this query term
				if (invIndex.invertedIndexTFs.containsKey(query))
				{
					HashMap<String, Integer> docTFs = invIndex.invertedIndexTFs.get(query);
					double idf = invIndex.termIDFs.get(query);
					
					//iterate through each document, make tf-idf, take dot product 
					//and add to previous value for document or initialise the dotProducts HashMap
					for (Map.Entry<String, Integer> entry : docTFs.entrySet())
					{
					    
						String docName = entry.getKey();
						
						int termFreq = entry.getValue();
						double tfIDF = termFreq*idf;
						
						double queryTfIdf = 1.0*idf;
						
						double termDotProduct = tfIDF * queryTfIdf;
						
						if (queryDocumentDotProducts.containsKey(docName))
						{
							queryDocumentDotProducts.put(docName, queryDocumentDotProducts.get(docName) + termDotProduct);
						} else{
							queryDocumentDotProducts.put(docName, termDotProduct);
						}
					}
					
					//add to query vector norm
					queryVectorNorm += idf*idf;
					
				} else {
					queryVectorNorm += Math.log(numberCorpusDocs)*Math.log(numberCorpusDocs);
				}
			}
			
			HashMap<String, Double> queryDocumentCosineSim = new HashMap<String, Double>();
			
			for (Map.Entry<String, Double> entry : queryDocumentDotProducts.entrySet())
			{
				String docName = entry.getKey();
				double dotProduct = entry.getValue();	
				
				double cosineSimilarity = dotProduct / (Math.sqrt(invIndex.docVectorNormsSquared.get(docName))*Math.sqrt(queryVectorNorm));
				
				queryDocumentCosineSim.put(docName, cosineSimilarity);
			}
			
			System.out.println(queryDocumentCosineSim);
			
			//Sort descending cosine sim
			ValueComparator bvc =  new ValueComparator(queryDocumentCosineSim);
			TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
			//sorted_map.putAll(queryDocumentCosineSim);
			System.out.println(sorted_map);
		
		} else {
			System.out.println("Your search produced no results!");
		}
		
	}
}