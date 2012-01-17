/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.workflows;

import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.arpodwot.heatmaps.documents.input.InputDocumentCollection;
import org.arpodwot.heatmaps.indexing.MultiDocumentIndexBuilder;

public class AddDocsToIndex {
	public static void main(String[] args) throws
	Exception,
	IOException,
	ParseException
	{
		// args[0] = document filepath list file
		// args[1] = note file
		String docListFilePath = args[0];
		String indexPath = args[1];

		// load in all of the documents
		System.out.print("Loading Documents ... ");
		InputDocumentCollection docs = new InputDocumentCollection(docListFilePath);
		System.out.println("Done!");

		// put documents into an index
		System.out.print("Building index ... ");
		MultiDocumentIndexBuilder.buildMultiDocumentIndex(indexPath, docs);
		docs = null; // we'll be accessing these via index shortly
		System.out.println("Done!");
	}
}
