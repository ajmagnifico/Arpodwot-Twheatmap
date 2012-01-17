package org.arpodwot.heatmaps.indexing.ngrams;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.arpodwot.heatmaps.notes.Note;

public class SimpleAnalyzerNGramGenerator extends NGramGenerator {
	public static Pattern tokenPattern = Pattern.compile("[a-z]+");

	public static SimpleAnalyzerNGramGenerator createGenerator(Note n){
		return createGenerator(n.getText());
	}
	
	public static SimpleAnalyzerNGramGenerator createGenerator(String text){
		//String[] textBits = text.trim().toLowerCase().split("[^a-z]+");
		// using n.getText().trim().toLowerCase().split("[^a-z]+")
		//   yields an empty string in position 0 ("") if the note text
		//   begins with a non a-z character.
		//   This creates more tokens than actually exist, and causes problems
		//   with token counts in other places in the code.
		// Ergo, any code using the .split() command should instead
		//   aggregate tokens with the regular expressions as shown below.

		String cleanText = text.trim().toLowerCase();
		Matcher m = tokenPattern.matcher(cleanText);
		ArrayList<String> tokens = new ArrayList<String>();
		while(m.find()){
			tokens.add(m.group());
		}
		String[] textBits = tokens.toArray(new String[tokens.size()]);
		return new SimpleAnalyzerNGramGenerator(textBits);
	}
	
	protected SimpleAnalyzerNGramGenerator(String[] textBits){
		super(textBits);
	}
}
