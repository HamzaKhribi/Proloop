package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;

/**
 * Created by hamza on 8/14/2016.
 */
public class Favourites {
    private String user_id;
    private String name;
    private String LastName;


    public Favourites(String user_id, String name, String lastName) {
        this.user_id = user_id;
        this.name = name;
        LastName = lastName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
