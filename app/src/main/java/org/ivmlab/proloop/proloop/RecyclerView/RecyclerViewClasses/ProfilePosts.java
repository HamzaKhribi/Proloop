package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;


public class ProfilePosts {

    private String id;

    public ProfilePosts(String id, String userID, String text, String imgPath, String tag_pub, String time) {
        this.id = id;
        this.userID = userID;
        this.text = text;
        this.imgPath = imgPath;
        this.tag_pub = tag_pub;
        this.time = time;
    }

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    private String userID;

    public String getUserID() { return this.userID; }

    public void setUserID(String userID) { this.userID = userID; }

    private String text;

    public String getText() { return this.text; }

    public void setText(String text) { this.text = text; }

    private String imgPath;

    public String getImgPath() { return this.imgPath; }

    public void setImgPath(String imgPath) { this.imgPath = imgPath; }

    private String tag_pub;

    public String getTagPub() { return this.tag_pub; }

    public void setTagPub(String tag_pub) { this.tag_pub = tag_pub; }

    private String time;

    public String getTime() { return this.time; }

    public void setTime(String time) { this.time = time; }


}
