package com.example.karokojnr.nadab.model;

import com.google.gson.annotations.SerializedName;

public class Product {

//    @SerializedName("data")
//    private List<Product> products;

    @SerializedName("name")
    private String name;
    @SerializedName("unitMeasure")
    private String unitMeasure;
    @SerializedName("price")
    private String price;
    @SerializedName("hotel")
    private String hotel;
    @SerializedName("image")
    private String image;
//    @SerializedName("sellingStatus")
//    private String sellingStatus;
//    @SerializedName("servedWith")
//    private String servedWith;

    public Product(String name, String unitMeasure, String price) {
        this.name = name;
        this.unitMeasure = unitMeasure;
        this.price = price;
        this.hotel = hotel;
        this.image = image;
//        this.sellingStatus = sellingStatus;
        //this.servedWith =  servedWith;
    }
//    @SerializedName("genre")
//    private List<String> genre;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUnitMeasure() {
        return unitMeasure;
    }
    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = unitMeasure;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getHotel() {
        return hotel;
    }
    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

//    public String getSellingStatus() {
//        return sellingStatus;
//    }
//    public void setSellingStatus(String sellingStatus) {
//        this.sellingStatus = sellingStatus;
//
//    }

//    public String getServedWith() {
//        return  servedWith;
//    }
//    public void setServedWith(String servedWith) {
//        this.servedWith = servedWith;
//    }


//    public List<String> getGenre() {
//        return genre;
//    }
//    public void setGenre(List<String> genre) {
//        this.genre = genre;cd
//    }
    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", unitMeasure='" + unitMeasure + '\'' +
                ", price='" + price + '\'' +
                ", image=" + image +
//                ", sellingStatus=" + sellingStatus +
               // ", servedWith=" + servedWith +

                '}';
    }
}

