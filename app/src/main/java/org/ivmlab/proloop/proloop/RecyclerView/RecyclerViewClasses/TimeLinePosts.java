package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;


import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewUtils.Item;

import java.util.ArrayList;

public class TimeLinePosts extends Item {

    private String followedId;
    private String pub_id;
    private String userID;
    private String text;
    private String imgPath;
    private String tagPub;
    private String dateTime;
    private String name;
    private String lastName;
    private String profileImgPath;
    ArrayList<Favourites> faves;
    private String faved;
    private int countFaves;
    private int nbrOfComs;

    public TimeLinePosts(String followedId, String pub_id, String userID, String text, String imgPath, String tagPub, String dateTime, String name, String lastName, String profileImgPath, ArrayList<Favourites> faves, String faved, int countFaves, int nbrOfComs) {
        this.followedId = followedId;
        this.pub_id = pub_id;
        this.userID = userID;
        this.text = text;
        this.imgPath = imgPath;
        this.tagPub = tagPub;
        this.dateTime = dateTime;
        this.name = name;
        this.lastName = lastName;
        this.profileImgPath = profileImgPath;
        this.faves = faves;
        this.faved = faved;
        this.countFaves = countFaves;
        this.nbrOfComs=nbrOfComs;
    }

    public TimeLinePosts(String pub_id, String userID, String text, String imgPath, String tagPub, String dateTime, String name, String lastName, String profileImgPath, ArrayList<Favourites> faves, String faved, int countFaves, int nbrOfComs) {
        this.pub_id = pub_id;
        this.userID = userID;
        this.text = text;
        this.imgPath = imgPath;
        this.tagPub = tagPub;
        this.dateTime = dateTime;
        this.name = name;
        this.lastName = lastName;
        this.profileImgPath = profileImgPath;
        this.faves = faves;
        this.faved = faved;
        this.countFaves = countFaves;
        this.nbrOfComs = nbrOfComs;
    }

    public int getNbrOfComs() {
        return nbrOfComs;
    }

    public void setNbrOfComs(int nbrOfComs) {
        this.nbrOfComs = nbrOfComs;
    }

    public String getFollowedId() {
        return followedId;
    }

    public void setFollowedId(String followedId) {
        this.followedId = followedId;
    }

    public String getPub_id() {
        return pub_id;
    }

    public void setPub_id(String pub_id) {
        this.pub_id = pub_id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTagPub() {
        return tagPub;
    }

    public void setTagPub(String tagPub) {
        this.tagPub = tagPub;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImgPath() {
        return profileImgPath;
    }

    public void setProfileImgPath(String profileImgPath) {
        this.profileImgPath = profileImgPath;
    }

    public ArrayList<Favourites> getFaves() {
        return faves;
    }

    public void setFaves(ArrayList<Favourites> faves) {
        this.faves = faves;
    }

    public String getFaved() {
        return faved;
    }

    public void setFaved(String faved) {
        this.faved = faved;
    }

    public int getCountFaves() {
        return countFaves;
    }

    public void setCountFaves(int countFaves) {
        this.countFaves = countFaves;
    }
}
