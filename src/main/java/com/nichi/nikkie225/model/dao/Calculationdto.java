package com.nichi.nikkie225.model.dao;

public class Calculationdto {
    private String code;
    private String name;
    private String tradedate;
    private String side;
    private int tradeprice;
    private int quantity;
    private int tradepriceofquantity;
    private int minusorplus;
    private int quantityofside;

    public int getQuantityofside() {
        return quantityofside;
    }

    public void setQuantityofside(int quantityofside) {
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

    public int getTradeprice() {
        return tradeprice;
    }

    public void setTradeprice(int tradeprice) {
        this.tradeprice = tradeprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTradepriceofquantity() {
        return tradepriceofquantity;
    }

    public void setTradepriceofquantity(int tradepriceofquantity) {
        this.tradepriceofquantity = tradepriceofquantity;
    }

    public int getMinusorplus() {
        return minusorplus;
    }

    public void setMinusorplus(int minusorplus) {
        this.minusorplus = minusorplus;
    }

    public Calculationdto(String code, String name, String tradedate, String side, int tradeprice, int quantity, int tradepriceofquantity, int minusorplus, int quantityofside) {
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
    public Calculationdto() {
    }

    @Override
    public String toString() {
        return "Calculationdto{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", tradedate='" + tradedate + '\'' +
                ", side='" + side + '\'' +
                ", tradeprice=" + tradeprice +
                ", quantity=" + quantity +
                ", tradepriceofquantity=" + tradepriceofquantity +
                ", minusorplus=" + minusorplus +
                ", quantityofside=" + quantityofside +
                '}';
    }
}
