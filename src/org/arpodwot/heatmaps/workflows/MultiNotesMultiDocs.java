package org.arpodwot.heatmaps.workflows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.queryParser.ParseException;
import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.documents.input.InputDocumentCollection;
import org.arpodwot.heatmaps.documents.output.HighlightedHTMLDocumentGenerator;
import org.arpodwot.heatmaps.documents.output.OutputDocumentGenerator;
import org.arpodwot.heatmaps.highlighting.SimpleDocumentHighlighter;
import org.arpodwot.heatmaps.indexing.MultiDocumentIndexBuilder;
import org.arpodwot.heatmaps.indexing.MultiDocumentIndexSearcher;
import org.arpodwot.heatmaps.matching.NoteDocumentMatcher;
import org.arpodwot.heatmaps.notes.Note;
import org.arpodwot.heatmaps.notes.SimpleNoteFile;
import org.arpodwot.heatmaps.util.Transformation;
import org.arpodwot.heatmaps.util.UnitIntervalTransformation;

public class MultiNotesMultiDocs {
	public static void main(String[] args) throws
		Exception,
		IOException,
		ParseException
	{
		// args[0] = document list file path
		// args[1] = document index path
		// args[2] = note file
		String docListFilePath = args[0];
		String indexPath = args[1];
		String noteFilePath = args[2];
		
		// load in all of the documents
		System.out.print("Loading Documents ... ");
		InputDocumentCollection docs = new InputDocumentCollection(docListFilePath);
		System.out.println("Done!");
		
		// put documents into an index
		System.out.print("Building index ... ");
		MultiDocumentIndexBuilder.buildMultiDocumentIndex(indexPath, docs);
		docs = null; // we'll be accessing these via index shortly
		System.out.println("Done!");
		
		// load in the notes
		System.out.print("Loading notes ... ");
		SimpleNoteFile noteCollection = new SimpleNoteFile(noteFilePath);
		System.out.println("Done!");

		// match the notes to their respective documents
		System.out.println("Matching notes to documents:");
		MultiDocumentIndexSearcher searcher = new MultiDocumentIndexSearcher(indexPath);
		HashMap<InputDocument, ArrayList<Note>> docNoteMap =
				new HashMap<InputDocument, ArrayList<Note>>();
		int noteCount = 0;
		while (noteCollection.hasNext()){
			if (++noteCount % 100 == 0)
				System.out.print("\t"+noteCount+"\r");

			Note n = noteCollection.nextNote();
			InputDocument doc = NoteDocumentMatcher.findMatchingDocument(n, searcher);
			if (doc == null) continue;

			if (!docNoteMap.containsKey(doc))
				docNoteMap.put(doc, new ArrayList<Note>());

			docNoteMap.get(doc).add(n);
		}
		System.out.println("\t---Done!");
		
		// for each document,
		//   Apply all notes to it, giving raw highlight data
		System.out.println("Calculating highlights for each document ...");
		SimpleDocumentHighlighter highlight = new SimpleDocumentHighlighter();
		for (InputDocument doc : docNoteMap.keySet()){
			System.out.println("\t"+doc.getFileName());
			Note[] notes = docNoteMap.get(doc).toArray(new Note[0]);
			highlight.highlightDocument(doc, notes);
		}
		System.out.println("\t---Done!");
		
		// Rescale to 0.0-1.0
		System.out.print("Normalizing highlight values ... ");
		Transformation t = new UnitIntervalTransformation();
		for (InputDocument doc : docNoteMap.keySet()){
			doc.setRawHighlightData(t.transform(doc.getRawHighlightData()));
		}
		System.out.println("Done!");
		
		//   output to HTML
		System.out.println("Generating HTML output ...");
		OutputDocumentGenerator out = new HighlightedHTMLDocumentGenerator();
		for (InputDocument doc : docNoteMap.keySet()){
			String outFilePath = doc.getDirPath()+File.separator+"HL_"+doc.getFileName();
			System.out.println("\t"+outFilePath);
			out.writeToFile(doc.getRawHighlightData(), doc.getText(), outFilePath);
		}
		System.out.println("\t---Done!\n");
		System.out.println("PROCESS COMPLETE");
	}
}
