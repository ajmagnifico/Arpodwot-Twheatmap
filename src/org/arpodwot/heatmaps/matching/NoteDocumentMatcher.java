/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.matching;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.arpodwot.heatmaps.documents.SearchableDocumentCollection;
import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.indexing.ngrams.SimpleAnalyzerNGramGenerator;
import org.arpodwot.heatmaps.notes.Note;

public class NoteDocumentMatcher {
	public static InputDocument findMatchingDocument(Note n, SearchableDocumentCollection docs) throws
		IOException,
		ParseException
	{
		SimpleAnalyzerNGramGenerator ngrams = SimpleAnalyzerNGramGenerator.createGenerator(n);
		
		MatchScoreCalculation calc = new NSquaredMatchScore();
		DocumentScoreAggregator scores = new DocumentScoreAggregator();
		while(ngrams.hasNext()){
			// parse the query and do the search
			int[] hits = docs.searchDocuments(ngrams.nextNGramString());
			scores.incrementDocumentScores(hits, calc.calculateMatchScore(ngrams.getPreviousNGramLength()));
		}
		
		return docs.getDocumentById(scores.getMaxScoreDocId());
	}
}
