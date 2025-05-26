package com.nichi.nikkie225.model;
import java.time.LocalDate;

public class TradeEntry {
    private Integer tradeno;
    private String code;
    private String name;
    private String tradedate;
    private String side;
    private Integer tradeprice;
    private Integer quantity;

    public TradeEntry(Integer tradeno, String code, String name, String tradedate, String side, Integer tradeprice, Integer quantity) {
        this.tradeno = tradeno;
        this.code = code;
        this.name = name;
        this.tradedate = tradedate;
        this.side = side;
        this.tradeprice = tradeprice;
        this.quantity = quantity;
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

    public Integer getTradeprice() {
        return tradeprice;
    }

    public void setTradeprice(Integer tradeprice) {
        this.tradeprice = tradeprice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TradeEntry{" +
                "tradeno=" + tradeno +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", tradedate='" + tradedate + '\'' +
                ", side='" + side + '\'' +
                ", tradeprice=" + tradeprice +
                ", quantity=" + quantity +
                '}';
    }
}