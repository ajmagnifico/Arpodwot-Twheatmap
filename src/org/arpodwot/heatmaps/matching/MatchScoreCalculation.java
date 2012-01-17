/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.matching;

public interface MatchScoreCalculation {
	public double calculateMatchScore(int ngramLength);
}
