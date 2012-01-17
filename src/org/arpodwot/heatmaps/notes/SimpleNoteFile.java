package org.arpodwot.heatmaps.notes;

import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SimpleNoteFile implements NoteCollection {
	private String _filePath;
	private int _noteCount;
	private int _currentPosition;
	
	private LinkedList<Note> _list;
	
	public SimpleNoteFile(String filePath) throws IOException {
		_currentPosition = 0;
		_list = new LinkedList<Note>();
		_filePath = filePath;
		
		// read in the notes from the file
		FileReader fr = new FileReader(_filePath);
		BufferedReader br = new BufferedReader(fr);
		
		String note;
		while((note = br.readLine()) != null){
			_list.add(new Note(_list.size(), note));
		}
		br.close();
		fr.close();
		
		_noteCount = _list.size();
	}

	@Override
	public Note getNote(int index) throws IndexOutOfBoundsException {
		return _list.get(index);
	}

	@Override
	public Note[] getNotes() {
		return (Note[])_list.toArray();
	}

	@Override
	public void reset() {
		_currentPosition = 0;
	}

	@Override
	public boolean hasNext() {
		if (_currentPosition >= _list.size())
			return false;
		
		return true;
	}

	@Override
	public Note nextNote() {
		if (hasNext()){
			Note n = _list.get(_currentPosition);
			_currentPosition++;
			return n;
		}
		
		return null;
	}
	
	@Override
	public int size(){
		return _noteCount;
	}
}
