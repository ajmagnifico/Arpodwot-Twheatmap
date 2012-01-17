package org.arpodwot.heatmaps.workflows;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.documents.output.HighlightedHTMLDocumentGenerator;
import org.arpodwot.heatmaps.documents.output.OutputDocumentGenerator;
import org.arpodwot.heatmaps.highlighting.SimpleDocumentHighlighter;
import org.arpodwot.heatmaps.matching.MatchesFile;
import org.arpodwot.heatmaps.notes.Note;
import org.arpodwot.heatmaps.util.Transformation;
import org.arpodwot.heatmaps.util.UnitIntervalTransformation;

public class CreateVisualization {
	public static void main(String[] args) throws Exception {
		String indexPath = args[0];
		String noteFilePath = args[1];
		String matchFilePath = args[2];
		
		HashMap<InputDocument, ArrayList<Note>> docNoteMap =
				MatchesFile.getMatchesFromFile(indexPath, noteFilePath, matchFilePath);
		
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
