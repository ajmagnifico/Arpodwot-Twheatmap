/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.documents.output;

public interface OutputDocumentGenerator {
	public void writeToFile(double[] highlightData, String originalText, String filePath) throws Exception;
}
