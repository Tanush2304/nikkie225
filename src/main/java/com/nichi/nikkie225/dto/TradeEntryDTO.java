package com.nichi.nikkie225.dto;


public class TradeEntryDTO {
    private Integer tradeNo;
    private String code;
    private String name;
    private String tradeDate;
    private String side;
    private Double tradePrice;
    private Integer quantity;
    private Integer isDeleted;

    public TradeEntryDTO(Integer tradeNo, String code, String name, String tradeDate, String side, Double tradePrice, Integer quantity, Integer isDeleted) {
        this.tradeNo = tradeNo;
        this.code = code;
        this.name = name;
        this.tradeDate = tradeDate;
        this.side = side;
        this.tradePrice = tradePrice;
        this.quantity = quantity;
        this.isDeleted = isDeleted;
    }

    public Integer getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(Integer tradeNo) {
        this.tradeNo = tradeNo;
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

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(Double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "TradeEntryDTO{" +
                "tradeNo=" + tradeNo +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", tradeDate='" + tradeDate + '\'' +
                ", side='" + side + '\'' +
                ", tradePrice=" + tradePrice +
                ", quantity=" + quantity +
                '}';

    }
}
