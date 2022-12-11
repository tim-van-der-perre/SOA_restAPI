package com.Create.API.rest.Models;


import javax.persistence.*;

@Entity
public class Product {
    @Column
    private String title;
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private long id;
    @Column
    private String description;
    @Column
    private float price;
    @Column
    private String type;
    @Column
    private String picture_url;

    public Product(String title, String description, float price, String picture_url) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.type = "Anime";
        this.picture_url = picture_url;
    }

    public Product() {

    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public float getPrice() {
        return price;
    }


    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getJsonValue(){
        return "{"
                + '\"' + "title" + '\"' + ":" + '\"' + title + '\"' + ","
                + '\"' + "id" + '\"' + ":" +  id  + ","
                + '\"' + "price" + '\"' + ":"  + price  + ","
                + '\"' + "type" + '\"' + ":" + '\"' + type + '\"' + ","
                + '\"' + "picture_url" + '\"' + ":" + '\"' + picture_url + '\"'
                +  '}';
    }
    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", picture_url='" + picture_url + '\'' +
                '}';
    }
}
