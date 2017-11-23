package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;

/**
 * Created by hamza on 8/10/2016.
 */
public class Sneaker {



    String imgPath;
    String imgFace1Path;
    String imgFace2Path;
    String imgFace3Path;
    String imgFace4Path;
    String imgFace5Path;
    float rating;
    int nbrVotes;
    String model_name;



    public Sneaker(String imgPath, String imgFace1Path, String imgFace2Path, String imgFace3Path, String imgFace4Path, String imgFace5Path, float rating, int nbrVotes, String model_name) {

        this.imgPath = imgPath;
        this.imgFace1Path = imgFace1Path;
        this.imgFace2Path = imgFace2Path;
        this.imgFace3Path = imgFace3Path;
        this.imgFace4Path = imgFace4Path;
        this.imgFace5Path = imgFace5Path;
        this.rating = rating;
        this.nbrVotes = nbrVotes;
        this.model_name = model_name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgFace1Path() {
        return imgFace1Path;
    }

    public void setImgFace1Path(String imgFace1Path) {
        this.imgFace1Path = imgFace1Path;
    }

    public String getImgFace2Path() {
        return imgFace2Path;
    }

    public void setImgFace2Path(String imgFace2Path) {
        this.imgFace2Path = imgFace2Path;
    }

    public String getImgFace3Path() {
        return imgFace3Path;
    }

    public void setImgFace3Path(String imgFace3Path) {
        this.imgFace3Path = imgFace3Path;
    }

    public String getImgFace4Path() {
        return imgFace4Path;
    }

    public void setImgFace4Path(String imgFace4Path) {
        this.imgFace4Path = imgFace4Path;
    }

    public String getImgFace5Path() {
        return imgFace5Path;
    }

    public void setImgFace5Path(String imgFace5Path) {
        this.imgFace5Path = imgFace5Path;
    }
    /*
        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public int getNbrVotes() {
            return nbrVotes;
        }

        public void setNbrVotes(int nbrVotes) {
            this.nbrVotes = nbrVotes;
        }*/
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNbrVotes() {
        return nbrVotes;
    }

    public void setNbrVotes(int nbrVotes) {
        this.nbrVotes = nbrVotes;
    }
    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }
}
