package org.arpodwot.heatmaps.highlighting;

import java.util.ArrayList;

import org.apache.lucene.index.TermPositionVector;
import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.indexing.ngrams.SimpleAnalyzerNGramGenerator;
import org.arpodwot.heatmaps.notes.Note;
import org.arpodwot.heatmaps.util.Stopwords;

public class SimpleDocumentHighlighter implements DocumentHighlighter {
	@Override
	public void highlightDocument(InputDocument doc, Note[] notes) {
		TermPositionVector tpv = doc.getTermPositionVector();
		
		ArrayList<HighlightedNote> allHighlightedNotes =
				new ArrayList<HighlightedNote>(notes.length);
		
		// go find all of the matches
		for (Note n : notes){
			allHighlightedNotes.add(getNoteHighlighting(n, tpv));
		}
		
		Stopwords stops = new Stopwords();
				
		// initial values are 0
		double[] docHighlightValues = new double[sum(tpv.getTermFrequencies())];

		// now apply their respective highlight values
		for (HighlightedNote note : allHighlightedNotes){
			double[] noteHighlightValues = new double[docHighlightValues.length];
			double noteMinProb = note.getMinProbability();
			double noteMaxProb = note.getMaxProbability();
			int noteMaxLength = note.getMaxLength();
			double probRange = noteMaxProb - noteMinProb;
			for (HighlightedPhrase phrase : note){
				int phraseLength = phrase.getLength();
				if (phraseLength < 4)
					continue;
				if (stops.areAllStopWords(phrase.getPhrase()))
					continue;
				if (phrase.getLength() < 5 && stops.getStopwordPercentage(phrase.getPhrase()) >= 0.5)
					continue;
				
				double normPhraseProb = ( probRange == 0.0 ? 1 : (phrase.getProbability() - noteMinProb)/probRange );
				double phraseQuoteyness = 1 - normPhraseProb;
				phraseQuoteyness = phraseQuoteyness * (phraseLength / noteMaxLength);
				if (phraseQuoteyness == 0)
					continue;
				for (int position : phrase.getDocPositions()){
					if (phraseQuoteyness > noteHighlightValues[position])
					noteHighlightValues[position] = phraseQuoteyness;
				}
			}
			
			// add note highlights to doc highlights
			for (int i = 0; i < noteHighlightValues.length; i++){
				docHighlightValues[i] += noteHighlightValues[i];
			}
		}

		// put this highlight data into the InputDocument object
		doc.setRawHighlightData(docHighlightValues);
	}
	
	private HighlightedNote getNoteHighlighting(Note n, TermPositionVector tpv){
		SimpleAnalyzerNGramGenerator ngrams = SimpleAnalyzerNGramGenerator.createGenerator(n);
		HighlightedNote highlights = new HighlightedNote(ngrams.getTotalCount());
		ProbabilityGenerator prob = new ProbabilityGenerator(tpv);

		// look for phrase matches, add them to the HP
		while(ngrams.hasNext()){
			HighlightedPhrase hp = new HighlightedPhrase();
			String[] ngram = ngrams.nextNGramArray();
			hp.setLength(ngram.length);
			hp.setPhrase(ngram);
						
			int rarestTermIndex = -1;
			int rarestTermFreq = Integer.MAX_VALUE;
			
			int[][] termPosVectors = new int[ngram.length][];
			for (int i = 0; i < ngram.length; i++){
				termPosVectors[i] = tpv.getTermPositions(tpv.indexOf(ngram[i]));
				if (termPosVectors[i].length < rarestTermFreq){
					rarestTermFreq = termPosVectors[i].length;
					rarestTermIndex = i;
				}
			}
			
			// all of the positions that the rarest word in the ngram occurs
			int[] rarestTermPosVector = termPosVectors[rarestTermIndex];
			
			// how far we've gotten through the position array for each term
			int[] seekMarkers = new int[ngram.length];
			
			// go through each of the positions for the rarest word in the ngram
			for (int i = 0; i < rarestTermPosVector.length; i++){
				boolean match = true; // each one is a potential match

				// this is where our searching is anchored
				int anchorPos = rarestTermPosVector[i];
				
				// this is where everything else should be
				int[] proposedMatch = new int[ngram.length];
				for (int j = 0; j < proposedMatch.length; j++){
					proposedMatch[j] = anchorPos + (j - rarestTermIndex);
				}
				
				// for each of the other words in the ngram, see if they
				//    fall in the right place
				for (int j = 0; j < termPosVectors.length; j++){
					if (j == rarestTermIndex) continue;
					
					// the position array of this word that we're checking
					int[] jthTermPositions = termPosVectors[j];
					
					// the position that we want to find for this word, given the
					//   current value of p
					int findPos = proposedMatch[j];

					// look through this position array until we either find
					//   the position we're looking for (findPos) or we exceed it
					for (; seekMarkers[j] < jthTermPositions.length; seekMarkers[j]++){
						// if we found the position we're looking for
						if (jthTermPositions[seekMarkers[j]] == findPos)
							break;
						
						// if the current check position has exceeded the position
						//   we're looking for
						if (jthTermPositions[seekMarkers[j]] > findPos){
							match = false;
							break;
						}
						
						// if we ran out of positions to check before finding
						//   the correct one
						if (seekMarkers[j] == jthTermPositions.length - 1){
							match = false;
							break;
						}
					}
					
					if (!match)
						break;
				}
				
				if (match){
					hp.setProbability(prob.getPhraseProbability(ngram));
					hp.addDocPositions(proposedMatch);
				}
			}
			
			if (hp.getMatchCount() > 0)
				highlights.add(hp);
		}
			
		highlights.trimToSize();
		return highlights;
	}
	
	private int sum(int[] x){
		int total = 0;
		for (int i = 0; i < x.length; i++){
			total += x[i];
		}
		return total;
	}
}
