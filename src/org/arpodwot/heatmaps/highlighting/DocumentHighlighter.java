/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.highlighting;

import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.notes.Note;

public interface DocumentHighlighter {
	public void highlightDocument(InputDocument doc, Note[] notes);
}
