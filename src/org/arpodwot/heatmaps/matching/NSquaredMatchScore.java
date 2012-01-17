package org.arpodwot.heatmaps.matching;

public class NSquaredMatchScore implements MatchScoreCalculation {
	@Override
	public double calculateMatchScore(int ngramLength){
		return Math.pow(ngramLength, 2);
	}
}
