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
		boolean lastWordWasStartingUpper = false;
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
				/////////////
				// CAPITAL LETTER CHAINS
				////////////
				if (!(i == 1 && (firstWordNeedsJoining || word.startsWith("-"))))
				{
					if (Character.isUpperCase(word.charAt(0)))
					{
						lastWordWasStartingUpper = true;
						runningUpperCaseWords = runningUpperCaseWords+" "+word;
					} else if (lastWordWasStartingUpper) {
						tokens.add(runningUpperCaseWords);
						runningUpperCaseWords = "";
						lastWordWasStartingUpper = false;
					} else {
						lastWordWasStartingUpper = false;
					}
				}
				/////////////
				// CAPITAL LETTER CHAINS
				////////////
				
				/////////////
				// HYPHENS
				////////////
				//Store last word for hyphen check of first word in next line
				if (i == numbWords)
				{
					lastLineWord = word;
					if (word.endsWith("-"))
					{
						firstWordNeedsJoining = true;
					}
				}
				//if first word of line contains hyphen join to last word of last line
				if (lineNumber != 1 && i == 1)
				{
					if (word.startsWith("-") || firstWordNeedsJoining)
					{
						if (!lastWordWasStartingUpper)
						{
							tokens.add(lastLineWord+word.substring(0,word.length()));
							firstWordNeedsJoining = false;
						} else {
							runningUpperCaseWords = runningUpperCaseWords+" "+word;
							lastWordWasStartingUpper = true;
						}
					}
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
				
				
				
				i++;
			}
		}
		
		return tokens;
	}


}