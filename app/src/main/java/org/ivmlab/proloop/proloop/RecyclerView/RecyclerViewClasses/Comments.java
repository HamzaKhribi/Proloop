package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;


public class Comments {

    private String com_text;
    private String user_id;
    private String comID;

    private String DateTime;

    private String name;
    private String profileImg;



    public Comments(String com_text, String dateTime, String name, String profileImg, String comID, String user_id) {
        this.com_text = com_text;
        DateTime = dateTime;
        this.name = name;
        this.profileImg=profileImg;
        this.comID=comID;
        this.user_id=user_id;

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComID() {
        return comID;
    }

    public void setComID(String comID) {
        this.comID = comID;
    }

    public String getCom_text() {
        return com_text;
    }

    public void setCom_text(String com_text) {
        this.com_text = com_text;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
