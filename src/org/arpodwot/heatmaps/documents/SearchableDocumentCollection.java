package org.arpodwot.heatmaps.documents;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.arpodwot.heatmaps.documents.input.InputDocument;

public interface SearchableDocumentCollection {
	public int[] searchDocuments(String queryText) throws
		IOException,
		ParseException;
	public InputDocument getDocumentById(int id) throws IOException;
}
