package com.nichi.nikkie225.model.dao;

public class Calculationdto {
    private String code;
    private String name;
    private String tradedate;
    private String side;
    private Double tradeprice;
    private Double quantity;
    private Double tradepriceofquantity;
    private Double minusorplus;
    private Double quantityofside;

    public Calculationdto(String code, String name, String tradedate, String side, Double tradeprice, Double quantity, Double tradepriceofquantity, Double minusorplus, Double quantityofside) {
        this.code = code;
        this.name = name;
        this.tradedate = tradedate;
        this.side = side;
        this.tradeprice = tradeprice;
        this.quantity = quantity;
        this.tradepriceofquantity = tradepriceofquantity;
        this.minusorplus = minusorplus;
        this.quantityofside = quantityofside;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradedate() {
        return tradedate;
    }

    public void setTradedate(String tradedate) {
        this.tradedate = tradedate;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Double getTradeprice() {
        return tradeprice;
    }

    public void setTradeprice(Double tradeprice) {
        this.tradeprice = tradeprice;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTradepriceofquantity() {
        return tradepriceofquantity;
    }

    public void setTradepriceofquantity(Double tradepriceofquantity) {
        this.tradepriceofquantity = tradepriceofquantity;
    }

    public Double getMinusorplus() {
        return minusorplus;
    }

    public void setMinusorplus(Double minusorplus) {
        this.minusorplus = minusorplus;
    }

    public Double getQuantityofside() {
        return quantityofside;
    }

    public void setQuantityofside(Double quantityofside) {
        this.quantityofside = quantityofside;
    }
}
