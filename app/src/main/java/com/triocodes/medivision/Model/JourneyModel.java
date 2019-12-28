package com.triocodes.medivision.Model;

/**
 * Created by admin on 05-05-16.
 */
public class JourneyModel implements Comparable<JourneyModel> {
    String Date,Time,Location,Journey;

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getJourney() {
        return Journey;
    }

    public void setJourney(String journey) {
        Journey = journey;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public int compareTo(JourneyModel another) {
        return 0;
    }
}
