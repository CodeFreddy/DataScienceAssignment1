package main.java;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class main {
    static final private String INDEX_DIRECTORY = "index";
    static final private String OUTPUT_DIR = "output";
    static final private int Max_Results = 100;

    static IndexData indexer = new IndexData();

    public static void main(String[] args)
    {
        System.setProperty("file.encoding", "UTF-8");

        String queryPath = "";
        String dataPath = "";

        // Store all query strings temporarily.
        QueryData queryData = new QueryData(queryPath);
        ArrayList<String> pageList = queryData.getAllPageQueries();
        ArrayList<String> sectionList = queryData.getAllSectionQueries();
        System.out.println("Got " + pageList.size() + " pages and " + sectionList.size() + " sections.");

        // Lucene Search
        // 1. For pages.
        System.out.println("Search Results for " + pageList.size() + " pages...");
        ArrayList<String> pageResults = null;
        try {
            pageResults = getSearchResult(pageList, Max_Results);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String pageRunFileName = "pages-bm25.run";
        System.out.println("Retrieved " + pageResults.size() + " results for pages. Write results to " + OUTPUT_DIR + "/"
                + pageRunFileName);
        writeToFile(pageRunFileName, pageResults);
        System.out.println("Pages Done.");

        // 2. Create run file for sections.
        System.out.println("Search Results for " + sectionList.size() + " sections...");
        ArrayList<String> sectionResults = null;
        try {
            sectionResults = getSearchResult(sectionList, Max_Results);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sectionRunFileName = "section-bm25.run";
        System.out.println("Retrieved " + sectionResults.size() + " results for sections. Write results to " + OUTPUT_DIR
                + "/" + sectionRunFileName);
        writeToFile(sectionRunFileName, sectionResults);
        System.out.println("Sections Done.");

        System.out.println("All Done!");

    }




    private static ArrayList<String> getSearchResult(ArrayList<String> queriesStr, int max_result)
            throws IOException, ParseException {
        ArrayList<String> runFileStr = new ArrayList<String>();

        IndexSearcher searcher = new IndexSearcher(
                DirectoryReader.open(FSDirectory.open((new File(INDEX_DIRECTORY).toPath()))));
        searcher.setSimilarity(new BM25Similarity());

        QueryParser parser = new QueryParser("content", new StandardAnalyzer());

        for (String queryStr : queriesStr) {
            Query q = parser.parse(QueryParser.escape(queryStr));
            TopDocs tops = searcher.search(q, max_result);
            ScoreDoc[] scoreDoc = tops.scoreDocs;
            for (int i = 0; i < scoreDoc.length; i++) {
                ScoreDoc score = scoreDoc[i];
                Document doc = searcher.doc(score.doc);
                String paraId = doc.getField("paraid").stringValue();
                float rankScore = score.score;
                int rank = i + 1;

                String runStr = "enwiki:" + queryStr.replace(" ", "%20") + " Q0 " + paraId + " " + rank + " "
                        + rankScore + " BM25";
                runFileStr.add(runStr);
            }
        }

        return runFileStr;
    }

    private static void writeToFile(String filename, ArrayList<String> runfileStrings) {
        String fullpath = OUTPUT_DIR + "/" + filename;
        try (FileWriter runfile = new FileWriter(new File(fullpath))) {
            for (String line : runfileStrings) {
                runfile.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not open " + fullpath);
        }
    }
}
