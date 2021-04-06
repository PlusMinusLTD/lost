package com.PlusMinusLLC.lost.Crime;

import java.util.Date;

public class CrimeModel {
    private String PosterImageDB;
    private String PosterNameDB;
    private String PosterEmailDB;
    private String PosterID;
    private String PostID;
    private String CriminalImageDB;
    private String CriminalNameDB;
    private String CrimeTypeDB;
    private String CrimeDetailsDB;
    private String CrimePlaceDB;
    private String CrimeTimeDB;
    private String CrimePostalCodeDB;
    private Date Time;

    public CrimeModel(String posterImageDB, String posterNameDB, String posterEmailDB, String posterID, String postID, String criminalImageDB, String criminalNameDB, String crimeTypeDB, String crimeDetailsDB, String crimePlaceDB, String crimeTimeDB, String crimePostalCodeDB, Date time) {
        PosterImageDB = posterImageDB;
        PosterNameDB = posterNameDB;
        PosterEmailDB = posterEmailDB;
        PosterID = posterID;
        PostID = postID;
        CriminalImageDB = criminalImageDB;
        CriminalNameDB = criminalNameDB;
        CrimeTypeDB = crimeTypeDB;
        CrimeDetailsDB = crimeDetailsDB;
        CrimePlaceDB = crimePlaceDB;
        CrimeTimeDB = crimeTimeDB;
        CrimePostalCodeDB = crimePostalCodeDB;
        Time = time;
    }

    public String getPosterImageDB() {
        return PosterImageDB;
    }

    public void setPosterImageDB(String posterImageDB) {
        PosterImageDB = posterImageDB;
    }

    public String getPosterNameDB() {
        return PosterNameDB;
    }

    public void setPosterNameDB(String posterNameDB) {
        PosterNameDB = posterNameDB;
    }

    public String getPosterEmailDB() {
        return PosterEmailDB;
    }

    public void setPosterEmailDB(String posterEmailDB) {
        PosterEmailDB = posterEmailDB;
    }

    public String getPosterID() {
        return PosterID;
    }

    public void setPosterID(String posterID) {
        PosterID = posterID;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getCriminalImageDB() {
        return CriminalImageDB;
    }

    public void setCriminalImageDB(String criminalImageDB) {
        CriminalImageDB = criminalImageDB;
    }

    public String getCriminalNameDB() {
        return CriminalNameDB;
    }

    public void setCriminalNameDB(String criminalNameDB) {
        CriminalNameDB = criminalNameDB;
    }

    public String getCrimeTypeDB() {
        return CrimeTypeDB;
    }

    public void setCrimeTypeDB(String crimeTypeDB) {
        CrimeTypeDB = crimeTypeDB;
    }

    public String getCrimeDetailsDB() {
        return CrimeDetailsDB;
    }

    public void setCrimeDetailsDB(String crimeDetailsDB) {
        CrimeDetailsDB = crimeDetailsDB;
    }

    public String getCrimePlaceDB() {
        return CrimePlaceDB;
    }

    public void setCrimePlaceDB(String crimePlaceDB) {
        CrimePlaceDB = crimePlaceDB;
    }

    public String getCrimeTimeDB() {
        return CrimeTimeDB;
    }

    public void setCrimeTimeDB(String crimeTimeDB) {
        CrimeTimeDB = crimeTimeDB;
    }

    public String getCrimePostalCodeDB() {
        return CrimePostalCodeDB;
    }

    public void setCrimePostalCodeDB(String crimePostalCodeDB) {
        CrimePostalCodeDB = crimePostalCodeDB;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
