/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.matching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.indexing.MultiDocumentIndexSearcher;
import org.arpodwot.heatmaps.notes.Note;
import org.arpodwot.heatmaps.notes.NoteCollection;
import org.arpodwot.heatmaps.notes.SimpleNoteFile;

public class MatchesFile {
	public static HashMap<InputDocument, ArrayList<Note>> getMatchesFromFile(String indexPath, String noteFilePath, String matchFilePath) throws
		IOException
	{
		System.out.print("Initializing document index ... ");
		MultiDocumentIndexSearcher search = 
				new MultiDocumentIndexSearcher(indexPath);
		System.out.println("Done!");
		
		System.out.print("Loading notes from file ... ");		
		NoteCollection notes = new SimpleNoteFile(noteFilePath);
		System.out.println("Done!");
		

		HashMap<InputDocument, ArrayList<Note>> matches = 
				new HashMap<InputDocument, ArrayList<Note>>();
		
		FileReader fr = new FileReader(matchFilePath);
		BufferedReader br = new BufferedReader(fr);

		System.out.print("Loading in matches from file ... ");
		String line;
		while ((line = br.readLine()) != null){
			if (line.trim().length() == 0) continue;
			
			String[] ids = line.split("\t");
			int noteId = Integer.parseInt(ids[0]);
			int docId = Integer.parseInt(ids[1]);
			
			Note n = notes.getNote(noteId);
			InputDocument doc = search.getDocumentById(docId);
			
			if (!matches.containsKey(doc))
				matches.put(doc, new ArrayList<Note>());
			
			matches.get(doc).add(n);
		}
		
		br.close();
		fr.close();
		
		System.out.println("Done!");
		
		return matches;
	}
}
