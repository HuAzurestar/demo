package com.myhexin.thsmember.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Gemini
 */
public class BlockMemberDTO {
    @JsonProperty("stock_code")
    private String stockCode;
    @JsonProperty("stock_name")
    private String stockName;
    private String market;
    private Double zf;

    // Getters and Setters
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }
    public Double getZf() { return zf; }
    public void setZf(Double zf) { this.zf = zf; }
}
