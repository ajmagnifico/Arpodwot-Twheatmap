/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.matching;

public class NSquaredMatchScore implements MatchScoreCalculation {
	@Override
	public double calculateMatchScore(int ngramLength){
		return Math.pow(ngramLength, 2);
	}
}
