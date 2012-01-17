package org.arpodwot.heatmaps.indexing;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.arpodwot.heatmaps.documents.SearchableDocumentCollection;
import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.documents.input.SimpleTextDocument;

public class MultiDocumentIndexSearcher implements 
	SearchableDocumentCollection
{
	public static Analyzer DEFAULT_ANALYZER = new SimpleAnalyzer();
	public static QueryParser DEFAULT_PARSER = new QueryParser(Version.LUCENE_30, "text", DEFAULT_ANALYZER);
	
	private HashMap<Integer, InputDocument> _documentCache;

	private String _indexPath;
	private IndexReader _reader;
	private IndexSearcher _searcher;
	
	private QueryParser _parser;
	
	public MultiDocumentIndexSearcher(String indexPath) throws IOException {
		// initialize things
		_parser = DEFAULT_PARSER;
		_indexPath = indexPath;
		_documentCache = new HashMap<Integer, InputDocument>();
		
		openIndex();
	}
	
	private void openIndex() throws IOException {
		File f = new File(_indexPath);
		FSDirectory dir = FSDirectory.open(f);
		_searcher = new IndexSearcher(dir);
		_reader = _searcher.getIndexReader();
	}
	
	public void close() throws IOException {
		_searcher.close();
		_reader.close();
		
		_searcher = null;
		_reader = null;
	}
	
	@Override
	public int[] searchDocuments(String queryText) throws
		IOException,
		ParseException
	{
		if (_reader == null || _searcher == null)
			throw new IllegalStateException("This instance has already been closed and is no longer usable.");
		
		Query query = _parser.parse(queryText);
		TopDocs hits = _searcher.search(query, _reader.numDocs());
				
		int[] ids = new int[hits.scoreDocs.length];
		for (int i = 0; i < hits.scoreDocs.length; i++){
			Document d = _reader.document(hits.scoreDocs[i].doc);
			ids[i] = Integer.parseInt(d.get("id"));
		}
		
		return ids;
	}

	@Override
	public InputDocument getDocumentById(int id) throws IOException {
		Integer idObj = new Integer(id);
		
		// see if we've already retrieved it from the index
		if (_documentCache.containsKey(idObj)){
			return _documentCache.get(idObj);
		} else {
			// if not, go get it!
			InputDocument retDoc = new SimpleTextDocument();

			// retrieve the document from the index
			Query q = NumericRangeQuery.newIntRange("id", idObj, idObj, true, true); 
			TopDocs hits = _searcher.search(q, 1);
			
			if (hits.totalHits == 0)
				return null; // couldn't find this id in the index!
			
			int internalDocId = hits.scoreDocs[0].doc;
			Document doc = _reader.document(internalDocId);

			retDoc.setId(Integer.parseInt(doc.get("id")));
			retDoc.setFilePath(doc.get("filePath"));
			retDoc.setDirPath(doc.get("dirPath"));
			retDoc.setFileName(doc.get("fileName"));
			retDoc.setText(doc.get("text"));
			retDoc.setTermPositionVector((TermPositionVector)_reader.getTermFreqVector(internalDocId, "text"));
			
			// add it to the cache
			_documentCache.put(idObj, retDoc);

			return retDoc;
		}
	}
}
