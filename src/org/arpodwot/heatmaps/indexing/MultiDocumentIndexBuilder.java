package org.arpodwot.heatmaps.indexing;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.arpodwot.heatmaps.documents.input.InputDocument;
import org.arpodwot.heatmaps.documents.input.InputDocumentCollection;

public class MultiDocumentIndexBuilder {
	public static IndexWriter.MaxFieldLength DEFAULT_MAX_FIELD_LENGTH = IndexWriter.MaxFieldLength.UNLIMITED;
	
	public static void buildMultiDocumentIndex(String indexPath, InputDocumentCollection docs) throws IOException {
		InputDocument[] docArray = new InputDocument[docs.size()];
		buildMultiDocumentIndex(indexPath, docs.toArray(docArray));
	}
	
	public static void buildMultiDocumentIndex(String indexPath, InputDocument[] docs) throws IOException {
		String _indexPath = indexPath;
		
		// set various index settings
		Analyzer _analyzer = new SimpleAnalyzer();
		IndexWriter.MaxFieldLength _maxFieldLength = DEFAULT_MAX_FIELD_LENGTH;
		
		// initialize the document template
		Document _doc = new Document();
		NumericField _idField = new NumericField("id", Field.Store.YES, true);
		Field _filePathField = new Field("filePath", "Default", Field.Store.YES, Field.Index.NO, Field.TermVector.NO);
		Field _dirPathField = new Field("dirPath", "Default", Field.Store.YES, Field.Index.NO, Field.TermVector.NO);
		Field _fileNameField = new Field("fileName", "Default", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.WITH_POSITIONS_OFFSETS);
		Field _textField = new Field("text", "Default", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.WITH_POSITIONS_OFFSETS);
		_doc.add(_idField);
		_doc.add(_filePathField);
		_doc.add(_dirPathField);
		_doc.add(_fileNameField);
		_doc.add(_textField);
		
		// initialize the IndexWriter
		File f = new File(_indexPath);
		FSDirectory dir = FSDirectory.open(f);
		IndexWriter writer = new IndexWriter(dir, _analyzer, true, _maxFieldLength);

		// write each file to the index
		for (InputDocument doc : docs){
			_idField.setIntValue(doc.getId());
			_filePathField.setValue(doc.getFilePath());
			_dirPathField.setValue(doc.getDirPath());
			_fileNameField.setValue(doc.getFileName());
			_textField.setValue(doc.getText());
			
			writer.addDocument(_doc);
		}
		
		// close the IndexWriter
		writer.commit();
		writer.optimize();
		writer.close();
	}
}
