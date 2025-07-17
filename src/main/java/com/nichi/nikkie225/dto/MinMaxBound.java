package com.nichi.nikkie225.dto;

public class MinMaxBound {
    private Double lowerBond;
    private Double higherBond;

    public MinMaxBound(Double lowerBond, Double higherBond) {
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

    @Override
    public String toString() {
        return "MinMaxBound{" +
                "lowerBond=" + lowerBond +
                ", higherBond=" + higherBond +
                '}';
    }
}
