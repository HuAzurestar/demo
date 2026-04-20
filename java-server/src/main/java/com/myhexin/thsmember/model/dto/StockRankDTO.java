package com.myhexin.thsmember.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Gemini
 */
public class StockRankDTO {
    @JsonProperty("stock_code")
    private String stockCode;
    @JsonProperty("stock_name")
    private String stockName;
    private String market;
    @JsonProperty("main_grey_capital")
    private Double mainGreyCapital;
    @JsonProperty("main_listed_capital")
    private Double mainListedCapital;
    @JsonProperty("main_capital")
    private Double mainCapital;
    @JsonProperty("retail_capital")
    private Double retailCapital;
    private Double turnover;
    @JsonProperty("change_pct")
    private Double changePct;
    private Double zf;
    /** main_listed_capital / turnover * 100，保留两位小数；非法时为 null */
    @JsonProperty("ratio_pct")
    private Double ratioPct;

    // Getters and Setters
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }
    public Double getMainGreyCapital() { return mainGreyCapital; }
    public void setMainGreyCapital(Double mainGreyCapital) { this.mainGreyCapital = mainGreyCapital; }
    public Double getMainListedCapital() { return mainListedCapital; }
    public void setMainListedCapital(Double mainListedCapital) { this.mainListedCapital = mainListedCapital; }
    public Double getMainCapital() { return mainCapital; }
    public void setMainCapital(Double mainCapital) { this.mainCapital = mainCapital; }
    public Double getRetailCapital() { return retailCapital; }
    public void setRetailCapital(Double retailCapital) { this.retailCapital = retailCapital; }
    public Double getTurnover() { return turnover; }
    public void setTurnover(Double turnover) { this.turnover = turnover; }
    public Double getChangePct() { return changePct; }
    public void setChangePct(Double changePct) { this.changePct = changePct; }
    public Double getZf() { return zf; }
    public void setZf(Double zf) { this.zf = zf; }
    public Double getRatioPct() { return ratioPct; }
    public void setRatioPct(Double ratioPct) { this.ratioPct = ratioPct; }
}
