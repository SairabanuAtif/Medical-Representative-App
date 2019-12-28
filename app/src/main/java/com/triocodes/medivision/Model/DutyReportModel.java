package com.triocodes.medivision.Model;

/**
 * Created by admin on 03-05-16.
 */
public class DutyReportModel implements Comparable<DutyReportModel> {
    int dutyId;
    String date;
    String inLocation;
    String outLocation;
    String timeIn;
    String timeOut;

    public int getDutyId(){
        return dutyId;
    }
    public void setDutyId(int dutyId){this.dutyId=dutyId;}

    public String getDate(){
        return date;
    }
    public void setDate(String date){this.date=date;}

    public String getInLocation(){
        return inLocation;
    }
    public void setInLocation(String inLocation){this.inLocation=inLocation;}

    public String getOutLocation(){
        return outLocation;
    }
    public void setOutLocation(String outLocation){this.outLocation=outLocation;}

    public String getTimeIn(){
        return timeIn;
    }
    public void setTimeIn(String timeIn){this.timeIn=timeIn;}

    public String getTimeOut(){
        return timeOut;
    }
    public void setTimeOut(String timeOut){this.timeOut=timeOut;}






    @Override
    public int compareTo(DutyReportModel another) {
        return 0;
    }
}
