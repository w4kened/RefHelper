package com.RefHelper.service.impl;

import java.util.List;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.RefHelper.entity.AidCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RefHelper.entity.Aid;
import com.RefHelper.repository.AidRepository;
import com.RefHelper.service.AidService;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;


@Service
public class AidServiceImplementation implements AidService {
    @Autowired
    private AidRepository aidRepo;

    public List<Aid> findAll() {
        save_csv();
        return aidRepo.findAll();
    }

    public void save_csv() {
        List<Aid>  res = aidRepo.findAll();

        if(res.isEmpty()) {
            System.out.println("No data");

            String[] HEADERS = {"Aid_id", "Town", "HelpCategory", "Latitude", "Longtitude"};
            String fileLocation = "~/code/java/RefHelper/src/main/resources/aid_data.csv";

            try {
                Reader fInput = new FileReader(fileLocation);
                Iterable<CSVRecord> records = CSVFormat.DEFAULT
                        .withHeader(HEADERS)
                        .withFirstRecordAsHeader()
                        .parse(fInput);


                for (CSVRecord record : records) {
                    String s_aid_id = record.get("Aid_id");
                    String s_aid_title = record.get("Aid_Title");
                    String s_aid_amountOfFollowers = record.get("Aid_amount_of_followers");
                    String s_aid_town = record.get("Aid_town");
                    String s_aid_description = record.get("Aid_description");
                    String s_categoryHelp = record.get("CategoryHelp");
                    String s_createdDate = record.get("CreatedDate");
                    String s_latitude = record.get("Latitude");
                    String s_longitude = record.get("Longitude");

                    //Converting to proper data types
                    Long aid_id = Long.valueOf(s_aid_id);
                    Integer aid_amountOfFollowers = Integer.valueOf(s_aid_amountOfFollowers);
                    AidCategory categoryHelp = AidCategory.valueOf(s_categoryHelp);


                    Double latitude = Double.parseDouble(s_latitude);
                    Double longitude = Double.parseDouble(s_longitude);
                    Point geom = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));


                    //load data into Aid table
                    Aid aidObj = new Aid();
                    aidObj.setAid_id(aid_id);
                    aidObj.setAid_title(s_aid_title);
                    aidObj.setAid_amountOfFollowers(aid_amountOfFollowers);
                    aidObj.setTown(s_aid_town);
                    aidObj.setAid_description(s_aid_description);
                    aidObj.setCategory(categoryHelp);
                    aidObj.setGeom(geom);



                    aidRepo.save(aidObj);
                }


            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            System.out.println("Data Loaded");
        }

    }
    

}
