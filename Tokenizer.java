import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.io.*;

class Tokenizer {

	public Tokenizer()
	{
	
	}
		

	public ArrayList<String> tokenize(ArrayList<String> lines)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		
		int lineNumber = 0;
		String lastLineWord = "";
		boolean firstWordNeedsJoining = false;
		int numberRunningUpCaseWords = 0;
		boolean lastWordWasStartingUpper = false;
		boolean dontAddToken = false;
		boolean hyphenatedWordAdded = false;
		boolean isUpperCaseWord = false;
		String runningUpperCaseWords = "";
		Iterator<String> it = lines.iterator();
		while (it.hasNext())
		{
			lineNumber++; //for debug
			String line = it.next();
			String[] words = line.trim().split(" ");
			int i = 1;
			int numbWords = words.length;
			for (String word : words)
			{
				word = word.trim();
				if (Character.isUpperCase(word.charAt(0)))
				{
					isUpperCaseWord = true;
				} else {
					isUpperCaseWord = false;
				}
				/////////////
				// CAPITAL LETTER CHAINS
				////////////
			 	if ((i == 1) && firstWordNeedsJoining) { 
					if (numberRunningUpCaseWords > 0)
					{
						runningUpperCaseWords = runningUpperCaseWords.substring(0,runningUpperCaseWords.length()-1)+word;
						System.out.println("=====");
						System.out.println(runningUpperCaseWords);
						
						if (isUpperCaseWord)
						{
							numberRunningUpCaseWords--;
						}
						System.out.println(numberRunningUpCaseWords);
						System.out.println("=====");
					} else {
						tokens.add(lastLineWord.substring(0,lastLineWord.length()-1)+word);
					}
					
					firstWordNeedsJoining = false;
					dontAddToken = true;
					hyphenatedWordAdded = true;	
				}
				if (isUpperCaseWord)
				{
					System.out.println(numberRunningUpCaseWords);
					System.out.println(word);
					lastWordWasStartingUpper = true;
					numberRunningUpCaseWords++;
					System.out.println(numberRunningUpCaseWords+"\n");
					if ((numberRunningUpCaseWords > 1) && !firstWordNeedsJoining)
					{
						System.out.println("in here");
						runningUpperCaseWords = runningUpperCaseWords+" "+word;
					} else {
						runningUpperCaseWords = word;
					}
					System.out.println(runningUpperCaseWords);
					dontAddToken = true;
				} else if (lastWordWasStartingUpper && !hyphenatedWordAdded) {
					
					if (numberRunningUpCaseWords > 0 && !(hyphenatedWordAdded && numberRunningUpCaseWords == 1))
					{
						tokens.add(runningUpperCaseWords);
					} 
					else if ((!hyphenatedWordAdded && numberRunningUpCaseWords != 1)) 
					{
						tokens.add(runningUpperCaseWords);
					}
					numberRunningUpCaseWords = 0;
					runningUpperCaseWords = "";
					lastWordWasStartingUpper = false;
					dontAddToken = false;
				} else if (lastWordWasStartingUpper && hyphenatedWordAdded) { 
					lastWordWasStartingUpper = true;
					dontAddToken = true;
				} else {
					lastWordWasStartingUpper = false;
					dontAddToken = false;
				}
				/////////////
				// CAPITAL LETTER CHAINS
				////////////
				
				/////////////
				// HYPHENS
				////////////
				//Store last word for hyphen check of first word in next line
				if ((i == numbWords) && word.endsWith("-"))
				{
					lastLineWord = word;
					firstWordNeedsJoining = true;
					dontAddToken = true;
				}
				
				//if first word of line contains hyphen join to last word of last line
				if ((i == 1) && firstWordNeedsJoining && numberRunningUpCaseWords == 0)
				{
					System.out.println(lastLineWord.substring(0,lastLineWord.length()-1)+word);
					tokens.add(lastLineWord.substring(0,lastLineWord.length()-1)+word);
					firstWordNeedsJoining = false;
					dontAddToken = true;
					hyphenatedWordAdded = true; 
				} else {
					hyphenatedWordAdded = false; 
				}
				/////////////
				// END HYPHENS
				/////////////
				
				/////////////
				// EMAIL ADDRESSES
				////////////
				
				/////////////
				// END EMAIL ADDRESSES
				////////////
				
				if (!dontAddToken)
				{
					tokens.add(word);
				}
				
				dontAddToken = false;
				
				i++;
			}
		}
		
		return tokens;
	}


}