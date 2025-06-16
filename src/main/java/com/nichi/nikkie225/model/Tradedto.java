package com.nichi.nikkie225.model;

public class Tradedto {
    private String Stocks;
    private String Name;
    private Double PositionT;
    private Double PositionTm1;
    private Double TradePrice;
    private Double Cashflow;
    private Double PriceT;
    private Double PriceTm1;
    private Double pchange;
    private Double PL;
    private Double PricePL;
    private Double TradePL;
    private Double TotalPL;

    public Tradedto() {

    }

    public String getStocks() {
        return Stocks;
    }

    public void setStocks(String stocks) {
        Stocks = stocks;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getPositionT() {
        return PositionT;
    }

    public void setPositionT(Double positionT) {
        PositionT = positionT;
    }

    public Double getPositionTm1() {
        return PositionTm1;
    }

    public void setPositionTm1(Double positionTm1) {
        PositionTm1 = positionTm1;
    }

    public Double getTradePrice() {
        return TradePrice;
    }

    public void setTradePrice(Double tradePrice) {
        TradePrice = tradePrice;
    }

    public Double getCashflow() {
        return Cashflow;
    }

    public void setCashflow(Double cashflow) {
        Cashflow = cashflow;
    }

    public Double getPriceT() {
        return PriceT;
    }

    public void setPriceT(Double priceT) {
        PriceT = priceT;
    }

    public Double getPriceTm1() {
        return PriceTm1;
    }

    public void setPriceTm1(Double priceTm1) {
        PriceTm1 = priceTm1;
    }

    public Double getPchange() {
        return pchange;
    }

    public void setPchange(Double pchange) {
        this.pchange = pchange;
    }

    public Double getPL() {
        return PL;
    }

    public void setPL(Double PL) {
        this.PL = PL;
    }

    public Double getPricePL() {
        return PricePL;
    }

    public void setPricePL(Double pricePL) {
        PricePL = pricePL;
    }

    public Double getTradePL() {
        return TradePL;
    }

    public void setTradePL(Double tradePL) {
        TradePL = tradePL;
    }

    public Double getTotalPL() {
        return TotalPL;
    }

    public void setTotalPL(Double totalPL) {
        TotalPL = totalPL;
    }

    public Tradedto(String stocks, String name, Double positionT, Double positionTm1, Double tradePrice, Double cashflow, Double priceT, Double priceTm1, Double pchange, Double PL, Double pricePL, Double tradePL, Double totalPL) {
        Stocks = stocks;
        Name = name;
        PositionT = positionT;
        PositionTm1 = positionTm1;
        TradePrice = tradePrice;
        Cashflow = cashflow;
        PriceT = priceT;
        PriceTm1 = priceTm1;
        this.pchange = pchange;
        this.PL = PL;
        PricePL = pricePL;
        TradePL = tradePL;
        TotalPL = totalPL;
    }
}