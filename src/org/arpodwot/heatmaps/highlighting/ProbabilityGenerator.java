package org.arpodwot.heatmaps.highlighting;

import java.util.HashMap;

import org.apache.lucene.index.TermPositionVector;

public class ProbabilityGenerator {
	private HashMap<String, Double> _probabilityMap = new HashMap<String, Double>();
	
	public ProbabilityGenerator(TermPositionVector index){
		String[] terms = index.getTerms();
		int[] freqs = index.getTermFrequencies();
		
		double sum = 0;
		// get the sum
		for (int i = 0; i < freqs.length; i++){
			sum += freqs[i];
		}
		
		// compute probabilities
		for (int i = 0; i < freqs.length; i++){
			_probabilityMap.put(terms[i], new Double(freqs[i] / sum));
		}
	}
	
	public double getPhraseProbability(String[] phrase){
		double prod = 1;
		for (int i = 0; i < phrase.length; i++){
			prod *= _probabilityMap.get(phrase[i]);
		}
		return prod;
	}
	
//	public double getPhraseProbability(String[] phrase){
//		HashMap<String, Integer> counts = new HashMap<String, Integer>();
//		for(String s : phrase){
//			if (!counts.containsKey(s))
//				counts.put(s,1);
//			else
//				counts.put(s, counts.get(s)+1);
//		}
//		
//		// calculate the probability
//		double n_fact = factorial(phrase.length);
//		
//		double fact_prod = 1;
//		double prob_prod = 1;
//		for (String s : counts.keySet()){
//			int c = counts.get(s);
//			fact_prod *= factorial(c);
//			prob_prod *= Math.pow(_probabilityMap.get(s), c);
//		}
//		
//		return (n_fact / fact_prod) * prob_prod;
//	}
//	
//	public static double factorial(int n){
//		double prod = 1;
//		while(n > 1){
//			prod *= n;
//			n--;
//		}
//		return prod;
//	}
}