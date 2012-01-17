package org.arpodwot.heatmaps.highlighting;

import java.util.ArrayList;
import java.util.HashSet;

public class HighlightedPhrase {
	private int _length;
	private String[] _phrase;
	private double _probability;
	private int _matchCount;
	
	private HashSet<Integer> _docPositions;
	
	public HighlightedPhrase(){
		_docPositions = new HashSet<Integer>();
	}
	
	public int getLength(){
		return _length;
	}
	public void setLength(int len){
		_length = len;
	}
	
	public double getProbability(){
		return _probability;
	}
	public void setProbability(double prob){
		_probability = prob;
	}
	public int getMatchCount(){
		return _matchCount;
	}
	
	public String[] getPhrase(){
		return _phrase;
	}
	public void setPhrase(String[] phrase){
		_phrase = phrase;
	}
	
//	public void addDocPosition(int i){
//		_docPositions.add(i);
//	}
//	public void addDocPositionRange(int min, int max){
//		ArrayList<Integer> tmpIndices = new ArrayList<Integer>((max-min)+1);
//		for (int i = min; i <= max; i++){
//			tmpIndices.set(i-min, i);
//		}
//		_docPositions.addAll(tmpIndices);
//	}
	public void addDocPositions(int[] positions){
		_matchCount++;
		ArrayList<Integer> tmpIndices = new ArrayList<Integer>(positions.length);
		for (int i = 0; i < positions.length; i++){
			tmpIndices.add(positions[i]);
		}
		_docPositions.addAll(tmpIndices);
	}
//	public void addDocPositions(Collection<Integer> positions){
//		_docPositions.addAll(positions);
//	}
	
	public Integer[] getDocPositions(){
		return _docPositions.toArray(new Integer[_docPositions.size()]);
	}
}
