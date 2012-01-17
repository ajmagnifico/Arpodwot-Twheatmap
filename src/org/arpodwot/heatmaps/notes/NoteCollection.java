package org.arpodwot.heatmaps.notes;

public interface NoteCollection {
	public Note getNote(int index);
	public Note[] getNotes();
	public void reset();
	public boolean hasNext();
	public Note nextNote();
	public int size();
}
