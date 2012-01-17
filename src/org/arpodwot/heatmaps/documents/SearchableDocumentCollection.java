/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

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
