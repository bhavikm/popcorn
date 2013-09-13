//A Very Simple Example

class popcorn {

  public static void main(String[] args){
	if (args.length > 0 && (args[0].equals("index") || args[0].equals("search")))
	{
		if (args[0].equals("index"))
		{
			if ((args.length == 2) || (args.length == 3))
			{
				String collection_dir = args[1];
				String index_dir = args[2];
				Indexer index = new Indexer();
				index.makeIndex(collection_dir, index_dir);
				
			} else {
				System.out.println("Invalid number of arguments, need to provide collection_dir, index_dir and optionally a stopwords.txt"); 
			}
		} else {
			if (args.length >= 4)
			{
				//valid search
				String index_dir = args[1];
				int num_docs = Integer.parseInt(args[2].trim());
				String[] query_terms = new String[args.length - 3];
				for (int i = 3; i < args.length; i++)
				{
					query_terms[i-3] = args[i].trim();
				}
				
				InvertedIndex invIndex = new InvertedIndex();
				invIndex.constructInvertedIndexFromFile(index_dir);
				
			} else {
				System.out.println("Invalid number of arguments, need to provide index_dir, num_docs and at least 1 keyword"); 
			}
		}
		
	} else {
		System.out.println("to index use: popcorn index collection_dir index_dir [stopwords.txt]\n");
		System.out.println("to search use: popcorn search index_dir num_docs keyword1 [keyword2 keyword3 ...]\n");
	}
  }
  
}