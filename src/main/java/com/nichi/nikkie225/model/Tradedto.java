package com.nichi.nikkie225.model;

public class Tradedto {
    private String Stocks;
    private String Name;
    private Integer PositionT;
    private Integer PositionTm1;
    private Integer TradePrice;
    private Integer Cashflow;
    private Integer PriceT;
    private Integer PriceTm1;
    private Double pchange;
    private Integer PL;
    private Integer PricePL;
    private Integer TradePL;

    public Tradedto(){

    }
    public Tradedto(String Stocks,String Name,Integer PositionT,Integer PositionTm1,Integer TradePrice,Integer Cashflow,Integer PriceT,Integer PriceTm1,Double pchange,Integer PL,Integer PricePL,Integer TradePL){
        this.Stocks=Stocks;
        this.Name=Name;
        this.PositionT=PositionT;
        this.PositionTm1=PositionTm1;
        this.TradePrice=TradePrice;
        this.Cashflow=Cashflow;
        this.PriceT=PriceT;
        this.PriceTm1=PriceTm1;
        this.pchange=pchange;
        this.PL=PL;
        this.PricePL=PricePL;
        this.TradePL=TradePL;
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

    public Integer getPositionT() {
        return PositionT;
    }

    public void setPositionT(Integer positionT) {
        PositionT = positionT;
    }

    public Integer getPositionTm1() {
        return PositionTm1;
    }

    public void setPositionTm1(Integer positionTm1) {
        PositionTm1 = positionTm1;
    }

    public Integer getTradePrice() {
        return TradePrice;
    }

    public void setTradePrice(Integer tradePrice) {
        TradePrice = tradePrice;
    }

    public Integer getCashflow() {
        return Cashflow;
    }

    public void setCashflow(Integer cashflow) {
        Cashflow = cashflow;
    }

    public Integer getPriceT() {
        return PriceT;
    }

    public void setPriceT(Integer priceT) {
        PriceT = priceT;
    }

    public Integer getPriceTm1() {
        return PriceTm1;
    }

    public void setPriceTm1(Integer priceTm1) {
        PriceTm1 = priceTm1;
    }

    public Double getPchange() {
        return pchange;
    }

    public void setPchange(Double pchange) {
        this.pchange = pchange;
    }

    public Integer getPL() {
        return PL;
    }

    public void setPL(Integer PL) {
        this.PL = PL;
    }

    public Integer getPricePL() {
        return PricePL;
    }

    public void setPricePL(Integer pricePL) {
        PricePL = pricePL;
    }

    public Integer getTradePL() {
        return TradePL;
    }

    public void setTradePL(Integer tradePL) {
        TradePL = tradePL;
    }
}

