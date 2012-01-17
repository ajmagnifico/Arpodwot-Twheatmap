package org.arpodwot.heatmaps.notes;

public class Note {
	private int _id;
	private String _note;
	
	public Note (int id, String note){
		_id = id;
		_note = note;
	}
	
	public Note (String note){
		_note = note;
	}
	
	public int getId(){
		return _id;
	}
	public void setId(int id){
		_id = id;
	}
	
	public String getText(){
		return _note;
	}
}
