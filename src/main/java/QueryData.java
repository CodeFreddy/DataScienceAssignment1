package main.java;

import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryData {
//    ArrayList<String> pageQueryList;
//    ArrayList<String> sectionQueryList;

        Map<String,String> pageQueryMap;
        Map<String,String> sectionQueryMap;


    static final private int numofQueryFiles = 5;
    //static final private String pageName = "train.pages.cbor";

    public  QueryData(String queryFilePath)
    {

        if (pageQueryMap == null || sectionQueryMap == null){
            pageQueryMap = new HashMap<>();
            sectionQueryMap = new HashMap<>();
            try {
                storeAllQuery(queryFilePath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public Map<String,String> getAllPageQueries(){
        return pageQueryMap;
    }

    public  Map<String,String> getAllSectionQueries()
    {
        return sectionQueryMap;
    }

    public void storeAllQuery(String filePath) throws FileNotFoundException {
        System.out.println("Retrieve queries from " + filePath);
        FileInputStream fis = new FileInputStream(new File(filePath));

        for(Data.Page page : DeserializeData.iterableAnnotations(fis))
        {
            pageQueryMap.put(page.getPageId(),page.getPageName());


            for (List<Data.Section> sectionPath : page.flatSectionPaths()) {
                String queryStr = page.getPageName();
                for (Data.Section section : sectionPath) {
                    //queryStr += "/";

                    //queryStr += section.getHeading();

                    queryStr +=" ";

                    queryStr += section.getHeading();
                }
//                sectionQueryList.add(queryStr);

                sectionQueryMap.put(Data.sectionPathId(page.getPageId(),sectionPath),queryStr);
            }
        }
    }
}
