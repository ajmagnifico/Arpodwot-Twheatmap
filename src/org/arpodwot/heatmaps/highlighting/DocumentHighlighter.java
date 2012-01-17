package org.arpodwot.heatmaps.highlighting;

import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.notes.Note;

public interface DocumentHighlighter {
	public void highlightDocument(InputDocument doc, Note[] notes);
}
