package main.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Main {

    static private String INDEX_DIRECTORY = "/Users/xin/Documents/19Spring/DS/index";
    private String OUTPUT_DIR = "output";
    static final private int Max_Results = 100;

    static IndexData indexer;

    public static void main(String[] args) throws IOException {
        System.setProperty("file.encoding", "UTF-8");

        String queryPath = "/Users/xin/Documents/19Spring/DS/Y1-tree-qrels/benchmarkY1-train/train.pages.cbor.tree.qrels";

        String dataPath = "/Users/xin/Documents/19Spring/DS/paragraphCorpus/dedup.articles-paragraphs.cbor";




        INDEX_DIRECTORY = args[0];
        queryPath = args[1];
        dataPath = args[2];


       // indexer = new IndexData(INDEX_DIRECTORY, dataPath);
        QueryData queryData = new QueryData(queryPath);
//
        Map<String,String> pageMap = queryData.getAllPageQueries();
        Map<String,String> sectionMap = queryData.getAllSectionQueries();

        // Store all query strings temporarily.


        System.out.println("Got " + pageMap.size() + " pages and " + sectionMap.size() + " sections.");

        // Lucene Search


        SearchData searcher = new SearchData(INDEX_DIRECTORY, pageMap, sectionMap, Max_Results);


        System.out.println("Finished");
    }
}
