package org.arpodwot.heatmaps.util;

public class UnitIntervalTransformation implements Transformation {
	@Override
	public double[] transform(double[] highlightData) {
		double minHighlightValue = Double.MAX_VALUE;
		double maxHighlightValue = Double.MIN_VALUE;

		for (int i = 0; i < highlightData.length; i++){
			if (highlightData[i] < minHighlightValue)
				minHighlightValue = highlightData[i];
			if (highlightData[i] > maxHighlightValue){
				maxHighlightValue = highlightData[i];
			}
		}
		
		double highlightRange = maxHighlightValue - minHighlightValue; 			
		for (int i = 0; i < highlightData.length; i++){
			highlightData[i] = (highlightData[i] - minHighlightValue)/highlightRange;
		}
		return highlightData;
	}
}
