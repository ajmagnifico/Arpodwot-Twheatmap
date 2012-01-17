/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.highlighting.color;

public class YellowHighlighter implements HighlightColorCSSGenerator {
	private double _minPercent;
	
	public YellowHighlighter(){
		_minPercent = 0.0;
	}
	public YellowHighlighter(double minPercent){
		_minPercent = minPercent;
	}
	
	@Override
	public String generateHighlightCSS(double highlightValue) {
		double min255 = 255 * _minPercent;
		int redValue = 255;
		int greenValue = 255;
		
		if (highlightValue < 0.01)
			return "background-color:rgb(255,255,255);";
		
		int blueValue = (int)Math.floor(255 - ((highlightValue * (255-min255))+min255));
		
		return "background-color:rgb("+redValue+","+greenValue+","+blueValue+");";
	}
}
