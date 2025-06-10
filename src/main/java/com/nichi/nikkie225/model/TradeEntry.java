package com.nichi.nikkie225.model;
import java.time.LocalDate;
import java.util.Date;

public class TradeEntry {
    private Integer tradeno;
    private String code;


    private String name;
    private String tradedate;


    private String side;

    private Double tradeprice;
    private Double quantity;
    private String isdeleted;

    public TradeEntry(Integer tradeno, String code, String name, String tradedate, String side, Double tradeprice, Double quantity,String isdeleted) {
        this.tradeno = tradeno;
        this.code = code;
        this.name = name;
        this.tradedate = tradedate;
        this.side = side;
        this.tradeprice = tradeprice;
        this.quantity = quantity;
        this.isdeleted=isdeleted;
    }

    public Integer getTradeno() {
        return tradeno;
    }

    public void setTradeno(Integer tradeno) {
        this.tradeno = tradeno;
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
    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String side) {
        this.isdeleted = isdeleted;
    }
}