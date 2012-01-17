package org.arpodwot.heatmaps.documents.input;

import java.io.IOException;
import java.util.ArrayList;


public class InputDocumentCollection extends ArrayList<InputDocument> {
	private static final long serialVersionUID = 1L;

	public InputDocumentCollection(String filePath) throws IOException {
		DocumentListFile docList = new DocumentListFile(filePath);
		int nextId = 0;
		while (docList.hasNext()){
			InputDocument d = new SimpleTextDocument(docList.nextFile());
			d.setId(nextId);
			this.add(d);
			nextId++;
		}
	}
}
