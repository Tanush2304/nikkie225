package com.nichi.nikkie225.model.dao;

public class Pricedto {

    private String dt;
    private String code;
    private String market;
    private Double price;

    public Pricedto(String dt, String code, String market, double price) {
        this.dt = dt;
        this.code = code;
        this.market = market;
        this.price = price;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "pricedto{" +
                "dt='" + dt + '\'' +
                ", code='" + code + '\'' +
                ", market='" + market + '\'' +
                ", price=" + price +
                '}';
    }
}
