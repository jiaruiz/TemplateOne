package com.hc.nmj.bean;

import java.io.Serializable;

import org.kymjs.kjframe.database.annotate.Id;

public class Clothes implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id()
    private int id;
    private String url;
    private String title;
    private String price;
    private int amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
