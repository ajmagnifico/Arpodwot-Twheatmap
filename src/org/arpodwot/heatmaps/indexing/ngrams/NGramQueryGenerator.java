package org.arpodwot.heatmaps.indexing.ngrams;

public interface NGramQueryGenerator {
	public int getTotalCount();
	public int getPreviousNGramLength();
	public String nextNGramString();
	public String[] nextNGramArray();
	public boolean hasNext();
	public void reset();
}
