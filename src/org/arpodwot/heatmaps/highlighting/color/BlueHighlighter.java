package org.arpodwot.heatmaps.highlighting.color;

public class BlueHighlighter implements HighlightColorCSSGenerator {
	private double _minPercent;
	private double _maxPercent;
	
	public BlueHighlighter(){
		_minPercent = 0.0;
		_maxPercent = 1.0;
	}
	
	public BlueHighlighter(double minPercent, double maxPercent){
		_minPercent = minPercent;
		_maxPercent = maxPercent;
	}
	

	@Override
	public String generateHighlightCSS(double highlightValue) {
		double min255 = 255 * _minPercent;
		double max255 = 255 * _maxPercent;
		int blueValue = 255;
		
		if (highlightValue < 0.01)
			return "background-color:rgb(255,255,255);";
		
		int redGreenValue = (int)Math.floor(max255 - ((highlightValue * (max255-min255))+min255));
		
		return "background-color:rgb("+redGreenValue+","+redGreenValue+","+blueValue+");";
	}

}
