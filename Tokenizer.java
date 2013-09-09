import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

class Tokenizer {

	public Tokenizer()
	{
	
	}
		

	public ArrayList<String> tokenize(ArrayList<String> lines)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		
		String r = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		Pattern p = Pattern.compile(r);
		
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
			String line = it.next(); //get the next line
			
			
			//remove email addresses from line first
			Matcher m = p.matcher(line);
			while (m.find()) 
			{
				String email = m.group(0);
				tokens.add(email);
				line = line.replaceFirst(email, "");
			}
			
			// split the words on any of the characters within the square brackets (can appear one or more times together)
			String[] words = line.trim().split("[ {.,:;\"\'()?!}]+");

			
			int i = 1; // word count in the line
			int numbWords = words.length;
			for (String word : words)
			{
				// make sure word has no surrounding white space
				word = word.trim();
				
				// determine once if word is an uppercase word
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
				/////////////
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