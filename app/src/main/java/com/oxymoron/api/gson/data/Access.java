
package com.oxymoron.api.gson.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Access {

    @SerializedName("line")
    @Expose
    private String line;
    @SerializedName("station")
    @Expose
    private String station;
    @SerializedName("station_exit")
    @Expose
    private String stationExit;
    @SerializedName("walk")
    @Expose
    private String walk;
    @SerializedName("note")
    @Expose
    private String note;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStationExit() {
        return stationExit;
    }

    public void setStationExit(String stationExit) {
        this.stationExit = stationExit;
    }

    public String getWalk() {
        return walk;
    }

    public void setWalk(String walk) {
        this.walk = walk;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String showUserAround() {
        String accessString = this.getLine() + this.getStation() + this.getWalk();
        return accessString.equals("") ? "" : accessString + "分";
    }

}
