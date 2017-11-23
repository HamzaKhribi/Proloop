package org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses;

/**
 * Created by Mal on 8/7/2017.
 */

public class Search {

    private int id;
    private String barCode;
    private String styleNum;

    public Search(){}

    public Search(String barCode, String styleNum) {
        this.barCode = barCode;
        this.styleNum = styleNum;
    }

    @Override
    public String toString() {
        return "Search{" +
                "id=" + id +
                ", barCode='" + barCode + '\'' +
                ", styleNum='" + styleNum + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getStyleNum() {
        return styleNum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setStyleNum(String styleNum) {
        this.styleNum = styleNum;
    }
}
