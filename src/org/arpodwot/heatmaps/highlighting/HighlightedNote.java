/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.highlighting;

import java.util.ArrayList;

public class HighlightedNote extends ArrayList<HighlightedPhrase> {
	private static final long serialVersionUID = 1L;
	
	private double _minProbability = Double.MAX_VALUE;
	private double _maxProbability = Double.MIN_VALUE;
	private int _maxLength = Integer.MIN_VALUE; 
	
	public HighlightedNote(int initialCapacity){
		super(initialCapacity);
	}
	
	@Override
	public boolean add(HighlightedPhrase hp){
		boolean result = super.add(hp);
		int length = hp.getLength();
		double prob = hp.getProbability();
		if (prob < _minProbability)
			_minProbability = prob;
		if (prob > _maxProbability)
			_maxProbability = prob;
		if (length > _maxLength)
			_maxLength = length;
		
		return result;
	}
	
	public double getMinProbability(){
		return _minProbability;
	}
	public double getMaxProbability(){
		return _maxProbability;
	}
	public int getMaxLength(){
		return _maxLength;
	}
}
