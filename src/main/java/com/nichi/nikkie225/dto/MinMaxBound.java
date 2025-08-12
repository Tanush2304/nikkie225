package com.nichi.nikkie225.dto;

public class MinMaxBound {
    private String code;
    private Double lowerBond;
    private Double higherBond;

    public MinMaxBound(String code, Double lowerBond, Double higherBond) {
        this.code = code;
        this.lowerBond = lowerBond;
        this.higherBond = higherBond;
    }

    public Double getLowerBond() {
        return lowerBond;
    }

    public void setLowerBond(Double lowerBond) {
        this.lowerBond = lowerBond;
    }

    public Double getHigherBond() {
        return higherBond;
    }

    public void setHigherBond(Double higherBond) {
        this.higherBond = higherBond;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MinMaxBound{" +
                "code='" + code + '\'' +
                ", lowerBond=" + lowerBond +
                ", higherBond=" + higherBond +
                '}';
    }
}
