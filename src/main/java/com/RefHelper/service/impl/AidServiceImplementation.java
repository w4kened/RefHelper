package com.RefHelper.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.RefHelper.entity.Category;
import com.RefHelper.entity.Volunteer;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
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
        List<Aid> res = aidRepo.findAll();

        if (res.isEmpty()) {
            System.out.println("No data");

            String[] HEADERS = {
                    "id", "amount_of_followers", "category_id", "created_date", "description",
                    "latitude", "longitude", "title", "town", "volunteer_id"};
            String fileLocation = "/home/w4kened/code/java/RefHelper/src/main/resources/aid_data.csv";

            try {
                Reader fInput = new FileReader(fileLocation);
                Iterable<CSVRecord> records = CSVFormat.DEFAULT
                        .withHeader(HEADERS)
                        .withFirstRecordAsHeader()
                        .parse(fInput);

                for (CSVRecord record : records) {
                    String s_id = record.get("id");
                    String s_amount_of_followers = record.get("amount_of_followers");
                    String s_category_id = record.get("category_id");
                    String s_created_date = record.get("created_date");
                    String s_description = record.get("description");
                    String s_latitude = record.get("latitude");
                    String s_longitude = record.get("longitude");
                    String s_title = record.get("title");
                    String s_town = record.get("town");
                    String s_volunteer_id = record.get("volunteer_id");


                    //Converting to proper data types
                    Long id = Long.valueOf(s_id);
                    Integer amount_of_followers = Integer.valueOf(s_amount_of_followers);
                    Category category_e = Category.valueOf(s_category_id);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime createdDate = LocalDateTime.parse(s_created_date, formatter);

                    Double latitude = Double.parseDouble(s_latitude);
                    Double longitude = Double.parseDouble(s_longitude);
                    Point geom = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
                    Volunteer volunteer = Volunteer.valueOf(s_volunteer_id);

                    //load data into Aid table
                    Aid aidObj = new Aid();
                    aidObj.setId(id);
                    aidObj.setAmountOfFollowers(amount_of_followers);
                    aidObj.setCategory(category_e);
                    aidObj.setCreatedDate(createdDate);
                    aidObj.setDescription(s_description);
                    aidObj.setGeom(geom);
                    aidObj.setTitle(s_title);
                    aidObj.setTown(s_town);
                    aidObj.setVolunteer(volunteer);

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
