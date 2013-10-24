popcorn
=======
Assignment for FIT5166 - Information Retrieval Systems at Monash University

A light weight search engine which indexes a collection of text documents as an inverted index. 
The index can then be searched by keyword at the command line, the results are a descending list of
cosine similarities between the query vector and document vectors.

To use:

Compile: 

javac *.java

Index a collection of text documents: 

java popcorn index collection_dir index_dir stopwords.txt

Search over a created index:

java popcorn search index_dir num_docs keyword_list

