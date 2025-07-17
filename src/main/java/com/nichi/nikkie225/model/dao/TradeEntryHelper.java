package com.nichi.nikkie225.model.dao;

public class TradeEntryHelper {
    private Integer tradeNo;
    private String code;

    public TradeEntryHelper(Integer tradeNo, String code) {
        this.tradeNo = tradeNo;
        this.code = code;
    }

    public TradeEntryHelper() {
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

    @Override
    public String toString() {
        return "TradeEntryHelper{" +
                "tradeNo=" + tradeNo +
                ", code='" + code + '\'' +
                '}';
    }
}
