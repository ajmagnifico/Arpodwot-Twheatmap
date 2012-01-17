/*
 * Copyright (C) 2012 Aaron W. Johnson
 * 
 * All rights reserved.  Licensing yet to be determined.
 */

package org.arpodwot.heatmaps.indexing.ngrams;

import java.util.List;

public class NGramGenerator implements NGramQueryGenerator {
	private String[] _textBits;
	private int _bitCount;
	
	private int _previousNGramLength;
	private int _currentLength;
	private int _currentIndex;
	
	private int _totalCount;
	
	public static NGramGenerator createGenerator(List<String> textBits){
		String[] tb = new String[textBits.size()];
		return new NGramGenerator(textBits.toArray(tb));
	}
	
	public static NGramGenerator createGenerator(String[] textBits){
		return new NGramGenerator(textBits);
	}
	
	protected NGramGenerator(String[] textBits){
		_textBits = textBits;
		_bitCount = _textBits.length;
		_totalCount = ((_bitCount * _bitCount) + _bitCount) / 2;
		_currentLength = 1;
		_currentIndex = 0;
	}

	@Override
	public String nextNGramString() {
		if (hasNext()){
			// jump to the next length if we need to
			if (_currentIndex + _currentLength > _bitCount){
				_currentIndex = 0;
				_currentLength++;
			}
			_previousNGramLength = _currentLength;

			StringBuilder ngram = new StringBuilder("\"");
			for (int i = 0; i < _currentLength; i++){
				if (i > 0)
					ngram.append(' ');

				ngram.append(_textBits[_currentIndex + i]);
			}
			ngram.append('"');

			_currentIndex++;

			return ngram.toString();
		}
		
		return null;
	}
	
	public String[] nextNGramArray(){
		if (hasNext()){
			// jump to the next length if we need to
			if (_currentIndex + _currentLength > _bitCount){
				_currentIndex = 0;
				_currentLength++;
			}
			_previousNGramLength = _currentLength;
			
			String[] ngram = new String[_currentLength];
			for (int i = 0; i < _currentLength; i++){
				ngram[i] = _textBits[_currentIndex + i];
			}
			
			_currentIndex++;
			
			return ngram;
		}
		
		return null;
	}
	
	@Override
	public boolean hasNext(){
		if (_currentLength > _bitCount)
			return false;
		
		if (_currentLength == _bitCount & _currentIndex + _currentLength > _bitCount)
			return false;
		
		return true;
	}

	@Override
	public void reset() {
		_currentIndex = 0;
		_currentLength = 1;
	}
	public void reset(int index, int length){
		_currentIndex = index;
		_currentLength = length;
	}
	
	@Override
	public int getPreviousNGramLength(){
		return _previousNGramLength;
	}
	
	@Override
	public int getTotalCount(){
		return _totalCount;
	}
}
