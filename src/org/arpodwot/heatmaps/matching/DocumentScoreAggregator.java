package org.arpodwot.heatmaps.matching;

import java.util.HashMap;

import org.apache.lucene.search.ScoreDoc;

public class DocumentScoreAggregator {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private HashMap<Integer, Double> _scoreMap = new HashMap<Integer, Double>();
	private int _maxScoreDocId;
	private double _maxScore;
	
	public DocumentScoreAggregator(){
		_maxScoreDocId = -1;
		_maxScore = -1;
	}
	
	public void incrementDocumentScores(int[] docs, double score){
		for (int i = 0; i < docs.length; i++){
			incrementDocumentScore(docs[i], score);
		}
	}
	
	public void incrementDocumentScores(ScoreDoc[] documents, double score){
		for (int i = 0; i < documents.length; i++){
			ScoreDoc sd = documents[i];
			incrementDocumentScore(sd.doc, score);
		}
	}
	
	public double incrementDocumentScore(int docId, double score){
		// docScore is either 0 or the existing score
		double docScore = 0;
		if (_scoreMap.containsKey(docId))
			docScore = _scoreMap.get(docId);
		
		// increase docScore by the score argument
		docScore += score;
		
		// store that new score in the map
		_scoreMap.put(docId, docScore);
		
		// if this is the new maximum
		if (docScore > _maxScore){
			_maxScoreDocId = docId;
			_maxScore = docScore;
		}
		
		return docScore;
	}
	
	public int getMaxScoreDocId(){
		return _maxScoreDocId;
	}
	
	public double getMaxScore(){
		return _maxScore;
	}
}
