package com.virus.tracker.Corona.Virus.Tracker.services;

import com.virus.tracker.Corona.Virus.Tracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataFetcher {
    private static final String TrackerURL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    private List<LocationStats> allStats=new ArrayList<>();
    @Scheduled(fixedDelay = 60000)
    public void fetchData(){
        System.out.println("Data fetched");
        List<LocationStats> newStats=new ArrayList<>();
        HttpClient httpClient=HttpClient.newHttpClient();
        HttpRequest httpRequest=HttpRequest.newBuilder(URI.create(TrackerURL)).build();
        try {
            HttpResponse<String> httpResponse=httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(httpResponse.body());
            Reader reader=new StringReader(httpResponse.body());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord csvRecord: records){
                LocationStats locationStats=new LocationStats();
                locationStats.setState(csvRecord.get("Province/State"));
                locationStats.setCountry(csvRecord.get("Country/Region"));
                locationStats.setTotalCases(Integer.parseInt(csvRecord.get(csvRecord.size()-1)));
                int fromPrevDay=Integer.parseInt(csvRecord.get(csvRecord.size()-2));
                //System.out.println(locationStats);
                locationStats.setDiffFromPrevDay(locationStats.getTotalCases()-fromPrevDay);

                newStats.add(locationStats);

            }
            this.allStats=newStats;

        }
        catch(IOException ioex){
             System.out.println("An io exception occurred "+ioex);
        }
        catch(InterruptedException inex){
            System.out.println("An interrupted exception occurred "+inex);
        }
    }
}
