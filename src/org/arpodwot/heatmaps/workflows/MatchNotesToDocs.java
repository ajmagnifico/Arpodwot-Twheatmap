/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.workflows;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.lucene.queryParser.ParseException;
import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.indexing.MultiDocumentIndexSearcher;
import org.arpodwot.heatmaps.matching.NoteDocumentMatcher;
import org.arpodwot.heatmaps.notes.Note;
import org.arpodwot.heatmaps.notes.SimpleNoteFile;

public class MatchNotesToDocs {
	public static void main(String[] args) throws
		Exception,
		IOException,
		ParseException
	{
		// args[0] = document index path
		// args[1] = note file
		String indexPath = args[0];
		String noteFilePath = args[1];
		String matchOutputFilePath = args[2];
		
		// load in the notes
		System.out.print("Loading notes ... ");
		SimpleNoteFile noteCollection = new SimpleNoteFile(noteFilePath);
		System.out.println("Done!");

		// get ready to write the mappings file
		FileWriter fw = new FileWriter(matchOutputFilePath);
		BufferedWriter bw = new BufferedWriter(fw);

		// match the notes to their respective documents
		System.out.println("Matching notes to documents:");
		MultiDocumentIndexSearcher searcher = new MultiDocumentIndexSearcher(indexPath);
		int noteCount = 0;
		while (noteCollection.hasNext()){
			if (++noteCount % 100 == 0)
				System.out.print("\t"+noteCount+" of "+noteCollection.size()+"\r");

			Note n = noteCollection.nextNote();
			InputDocument doc = NoteDocumentMatcher.findMatchingDocument(n, searcher);
			if (doc == null) continue;

			bw.write(n.getId()+"\t"+doc.getId()+"\n");
		}
		
		bw.close();
		fw.close();
		
		System.out.println("\t---Done!");
	}
}
