package org.ivmlab.proloop.proloop.bluesourceteam;

import java.io.Serializable;

/**
 * Created by Masterkey on 20.06.2015.
 */
public class TeamMember implements Serializable {
    private String name;
    private int imageId;
    private String description;

    public TeamMember(String name, int imageId, String description) {
        this.name = name;
        this.imageId = imageId;
        setDescription(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description + "\n\n" + description + "\n\n" + description + "\n\n" +
                description + "\n\n" + description + "\n\n" + description + "\n\n";
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
