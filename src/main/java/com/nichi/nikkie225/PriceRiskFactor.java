package com.nichi.nikkie225;

public class PriceRiskFactor {
    private String code;
    private Integer dt;
    private String market;
    private double dma200;
    private double dma50;
    private double dma13;
    private double dma8;
    private double dma5;

    public PriceRiskFactor(String code, Integer dt, String market,
                           double dma200, double dma50, double dma13, double dma8, double dma5) {
        this.code = code;
        this.dt = dt;
        this.market = market;
        this.dma200 = dma200;
        this.dma50 = dma50;
        this.dma13 = dma13;
        this.dma8 = dma8;
        this.dma5 = dma5;
    }

    public String getCode() { return code; }
    public Integer getDt() { return dt; }
    public String getMarket() { return market; }
    public double getDma200() { return dma200; }
    public double getDma50() { return dma50; }
    public double getDma13() { return dma13; }
    public double getDma8() { return dma8; }
    public double getDma5() { return dma5; }
}
