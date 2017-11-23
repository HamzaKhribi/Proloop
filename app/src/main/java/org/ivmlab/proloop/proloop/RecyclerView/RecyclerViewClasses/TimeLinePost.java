package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;


public class TimeLinePost {

    private String pub_id;
    private String userID;
    private String text;
    private String imgPath;
    private String tagPub;
    private String dateTime;
    private String name;
    private String lastName;
    private String profileImgPath;

    public TimeLinePost(String pub_id, String userID, String text, String imgPath, String tagPub, String dateTime, String name, String lastName, String profileImgPath) {
        this.pub_id = pub_id;
        this.userID = userID;
        this.text = text;
        this.imgPath = imgPath;
        this.tagPub = tagPub;
        this.dateTime = dateTime;
        this.name = name;
        this.lastName = lastName;
        this.profileImgPath = profileImgPath;
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
}
