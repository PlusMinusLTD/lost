package com.PlusMinusLLC.lost.Missing;

import java.util.Date;

public class MissingModel {
    private String PosterImageDB;
    private String PosterNameDB;
    private String PosterEmailDB;
    private String PosterID;
    private String PostID;
    private String MissingPersonImageDB;
    private String MissingPersonNameDB;
    private String MissingPersonAgeDB;
    private String MissingPersonDetailsDB;
    private String MissingPersonPlaceDB;
    private String MissingPersonTimeDB;
    private String MissingPersonPostalCodeDB;
    private Date Time;

    public MissingModel(String posterImageDB, String posterNameDB, String posterEmailDB, String posterID, String postID, String missingPersonImageDB, String missingPersonNameDB, String missingPersonAgeDB, String missingPersonDetailsDB, String missingPersonPlaceDB, String missingPersonTimeDB, String missingPersonPostalCodeDB, Date time) {
        PosterImageDB = posterImageDB;
        PosterNameDB = posterNameDB;
        PosterEmailDB = posterEmailDB;
        PosterID = posterID;
        PostID = postID;
        MissingPersonImageDB = missingPersonImageDB;
        MissingPersonNameDB = missingPersonNameDB;
        MissingPersonAgeDB = missingPersonAgeDB;
        MissingPersonDetailsDB = missingPersonDetailsDB;
        MissingPersonPlaceDB = missingPersonPlaceDB;
        MissingPersonTimeDB = missingPersonTimeDB;
        MissingPersonPostalCodeDB = missingPersonPostalCodeDB;
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

    public String getMissingPersonImageDB() {
        return MissingPersonImageDB;
    }

    public void setMissingPersonImageDB(String missingPersonImageDB) {
        MissingPersonImageDB = missingPersonImageDB;
    }

    public String getMissingPersonNameDB() {
        return MissingPersonNameDB;
    }

    public void setMissingPersonNameDB(String missingPersonNameDB) {
        MissingPersonNameDB = missingPersonNameDB;
    }

    public String getMissingPersonAgeDB() {
        return MissingPersonAgeDB;
    }

    public void setMissingPersonAgeDB(String missingPersonAgeDB) {
        MissingPersonAgeDB = missingPersonAgeDB;
    }

    public String getMissingPersonDetailsDB() {
        return MissingPersonDetailsDB;
    }

    public void setMissingPersonDetailsDB(String missingPersonDetailsDB) {
        MissingPersonDetailsDB = missingPersonDetailsDB;
    }

    public String getMissingPersonPlaceDB() {
        return MissingPersonPlaceDB;
    }

    public void setMissingPersonPlaceDB(String missingPersonPlaceDB) {
        MissingPersonPlaceDB = missingPersonPlaceDB;
    }

    public String getMissingPersonTimeDB() {
        return MissingPersonTimeDB;
    }

    public void setMissingPersonTimeDB(String missingPersonTimeDB) {
        MissingPersonTimeDB = missingPersonTimeDB;
    }

    public String getMissingPersonPostalCodeDB() {
        return MissingPersonPostalCodeDB;
    }

    public void setMissingPersonPostalCodeDB(String missingPersonPostalCodeDB) {
        MissingPersonPostalCodeDB = missingPersonPostalCodeDB;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
