package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;

/**
 * Created by hamza on 8/20/2016.
 */
public class Shop {

    Double lat;
    Double lng;
    String adress;
    String name;

    public Shop(Double lat, Double lng, String adress, String name) {
        this.lat = lat;
        this.lng = lng;
        this.adress = adress;
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
