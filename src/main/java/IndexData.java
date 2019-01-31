package main.java;


import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IndexData {
//    static final private String INDEX_DIRECTORY = "index";

    public static void indexData(String INDEX_DIRECTORY,String filePath) throws IOException {
        Directory indexDir = FSDirectory.open((new File(INDEX_DIRECTORY)).toPath());

        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());

        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter indexWriter = new IndexWriter(indexDir, config);

        System.out.println("Start Indexing...");

        int count = 0;
        for(Data.Paragraph p: DeserializeData.iterableParagraphs(new FileInputStream(new File(filePath))))
        {
            //System.out.println(count++);
            Document doc = convertToLuceneDoc(p);
            indexWriter.addDocument(doc);
        }
        System.out.println("=======================");
        System.out.println("Indexing was done");
        indexWriter.commit();
        indexWriter.close();
    }

    public static Document convertToLuceneDoc(Data.Paragraph paragraph)
    {
        Document doc = new Document();
        doc.add(new StringField("paraid", paragraph.getParaId(), Field.Store.YES));
        doc.add(new TextField("content", paragraph.getTextOnly(), Field.Store.NO));
        return doc;
    }
}
