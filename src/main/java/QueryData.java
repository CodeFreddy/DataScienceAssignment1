package main.java;

import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class QueryData {
    ArrayList<String> pageQueryList;
    ArrayList<String> sectionQueryList;
    static final private int numofQueryFiles = 5;
    static final private String pageName = "train.pages.cbor";

    public  QueryData(String queryFilePath)
    {
        if(pageQueryList == null || sectionQueryList == null)
        {
            pageQueryList = new ArrayList<>();
            sectionQueryList = new ArrayList<>();
            try {
                storeAllQuery(queryFilePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getAllPageQueries()
    {
        return pageQueryList;

    }

    public  ArrayList<String> getAllSectionQueries()
    {
        return sectionQueryList;
    }

    public void storeAllQuery(String filePath) throws FileNotFoundException {
        System.out.println("Retrieve queries from " + filePath);
        FileInputStream fis = new FileInputStream(new File(filePath));

        for(Data.Page page : DeserializeData.iterableAnnotations(fis))
        {
            pageQueryList.add(page.getPageName());
            for (List<Data.Section> sectionPath : page.flatSectionPaths()) {
                String queryStr = page.getPageName();
                for (Data.Section section : sectionPath) {
                    queryStr += "/";
                    queryStr += section.getHeading();
                }
                sectionQueryList.add(queryStr);
            }
        }

    }
}
