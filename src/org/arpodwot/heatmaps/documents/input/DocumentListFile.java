package org.arpodwot.heatmaps.documents.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class DocumentListFile {
	private String _filePath;
	private int _fileCount;
	private int _currentPosition;
	
	private LinkedList<String> _list;
	
	public DocumentListFile(String filePath) throws IOException {
		_currentPosition = 0;
		_list = new LinkedList<String>();
		_filePath = filePath;
		
		// read in the notes from the file
		FileReader fr = new FileReader(_filePath);
		BufferedReader br = new BufferedReader(fr);
		
		String file;
		while((file = br.readLine()) != null){
			_list.add(file);
		}
		br.close();
		fr.close();
		
		_fileCount = _list.size();
	}

	public String[] getFiles() {
		return (String[])_list.toArray();
	}

	public void reset() {
		_currentPosition = 0;
	}

	public boolean hasNext() {
		if (_currentPosition >= _list.size())
			return false;
		
		return true;
	}

	public String nextFile() {
		if (hasNext()){
			String file = _list.get(_currentPosition);
			_currentPosition++;
			return file;
		}
		
		return null;
	}
	
	public int size(){
		return _fileCount;
	}
}
