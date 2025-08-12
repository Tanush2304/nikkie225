package com.nichi.nikkie225.model1;


import jakarta.persistence.*;

@Entity
@IdClass(StocksListId.class)
@Table(name = "StocksList")
public class StocksList {
    @Id
    @Column(name = "code_id")
    private String codeId;
    @Column(name = "name")
    private String name;
    @Id
    @Column(name = "market")
    private String market;
    @Column(name = "updatesource")
    private String updateSource;
    @Column(name = "updatetime")
    private String updateTime;
    @Column(name = "n50_f")
    private Integer n50f;
    @Column(name = "n500_f")
    private Integer n500f;

    public StocksList() {
    }

    public StocksList(String codeId, String name, String market, String updateSource, String updateTime, Integer n50f, Integer n500f) {
        this.codeId = codeId;
        this.name = name;
        this.market = market;
        this.updateSource = updateSource;
        this.updateTime = updateTime;
        this.n50f = n50f;
        this.n500f = n500f;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getUpdateSource() {
        return updateSource;
    }

    public void setUpdateSource(String updateSource) {
        this.updateSource = updateSource;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getN50f() {
        return n50f;
    }

    public void setN50f(Integer n50f) {
        this.n50f = n50f;
    }

    public Integer getN500f() {
        return n500f;
    }

    public void setN500f(Integer n500f) {
        this.n500f = n500f;
    }

    @Override
    public String toString() {
        return "StocksList{" +
                "codeId='" + codeId + '\'' +
                ", name='" + name + '\'' +
                ", market='" + market + '\'' +
                ", updateSource='" + updateSource + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", n50f=" + n50f +
                ", n500f=" + n500f +
                '}';
    }
}