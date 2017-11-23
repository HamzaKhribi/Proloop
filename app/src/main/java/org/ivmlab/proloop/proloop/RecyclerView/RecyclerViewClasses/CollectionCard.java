package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;


public class CollectionCard {

    private String id;
    private String user_id;
    private String sneaker_model;
    private String sneaker_imgPath;
    private String dateOfBuy;
    private String price;

    public CollectionCard(String id, String user_id, String sneaker_model, String sneaker_imgPath, String dateOfBuy, String price) {
        this.id = id;
        this.user_id = user_id;
        this.sneaker_model = sneaker_model;
        this.sneaker_imgPath = sneaker_imgPath;
        this.dateOfBuy = dateOfBuy;
        this.price = price;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSneaker_model() {
        return sneaker_model;
    }

    public void setSneaker_model(String sneaker_model) {
        this.sneaker_model = sneaker_model;
    }

    public String getSneaker_imgPath() {
        return sneaker_imgPath;
    }

    public void setSneaker_imgPath(String sneaker_imgPath) {
        this.sneaker_imgPath = sneaker_imgPath;
    }

    public String getDateOfBuy() {
        return dateOfBuy;
    }

    public void setDateOfBuy(String dateOfBuy) {
        this.dateOfBuy = dateOfBuy;
    }
}
