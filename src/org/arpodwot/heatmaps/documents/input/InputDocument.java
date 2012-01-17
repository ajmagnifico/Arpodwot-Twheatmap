package org.arpodwot.heatmaps.documents.input;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.TermPositionVector;

public interface InputDocument {
	public int getId();
	public void setId(int id);
	
	public String getFilePath();
	public void setFilePath(String filePath);
	
	public String getDirPath();
	public void setDirPath(String dirPath);
	
	public String getFileName();
	public void setFileName(String fileName);
	
	public String getText();
	public void setText(String text);
	
	public TermPositionVector getTermPositionVector();
	public void setTermPositionVector(TermPositionVector tpv);
	
	public double[] getRawHighlightData();
	public void setRawHighlightData(double[] highlightData);
	
	public Analyzer getDocumentClassAnalyzer();
}
