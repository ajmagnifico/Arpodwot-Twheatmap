/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.documents.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.TermPositionVector;

public class SimpleTextDocument implements InputDocument {
	public static Analyzer DEFAULT_ANALYZER = new SimpleAnalyzer();
	
	@Override
	public Analyzer getDocumentClassAnalyzer(){
		return DEFAULT_ANALYZER;
	}
	
	private int _id;
	private String _filePath;
	private String _dirPath;
	private String _fileName;
	private String _text;
	
	private TermPositionVector _termPositionVector;
	private double[] _rawHighlightData;
	
	public SimpleTextDocument(){
		// return a blank document
	}
	
	public SimpleTextDocument(String filePath) throws IOException {
		_filePath = filePath;
		File f = new File(_filePath);
		_dirPath = f.getParent();
		_fileName = f.getName();
		
		FileReader fr = new FileReader(_filePath);
		BufferedReader br = new BufferedReader(fr);
		
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null){
			if (line.trim().length() == 0) continue;
			builder.append(line+"\n");
		}
		
		br.close();
		fr.close();
		
		_text = builder.toString();
	}
	
	@Override
	public String getText() {
		return _text;
	}
	@Override
	public void setText(String text){
		_text = text;
	}
	
	@Override
	public int getId() {
		return _id;
	}
	@Override
	public void setId(int id) {
		_id = id;
	}
	
	@Override
	public String getFilePath(){
		return _filePath;
	}
	@Override
	public void setFilePath(String filePath){
		_filePath = filePath;
	}
	
	@Override
	public String getDirPath(){
		return _dirPath;
	}
	@Override
	public void setDirPath(String dirPath){
		_dirPath = dirPath;
	}
	
	@Override
	public String getFileName(){
		return _fileName;
	}
	@Override
	public void setFileName(String fileName){
		_fileName = fileName;
	}
	
	@Override
	public TermPositionVector getTermPositionVector(){
		return _termPositionVector;
	}
	@Override
	public void setTermPositionVector(TermPositionVector tpv){
		_termPositionVector = tpv;
	}
	
	@Override
	public double[] getRawHighlightData(){
		return _rawHighlightData;
	}
	@Override
	public void setRawHighlightData(double[] highlightData){
		_rawHighlightData = highlightData;
	}
}
