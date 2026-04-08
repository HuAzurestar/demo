package com.myhexin.thsmember.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Created by Gemini
 */
public class BlockRankDTO {
    @JsonProperty("block_code")
    private String blockCode;
    @JsonProperty("block_name")
    private String blockName;
    private String market;
    private Double zf;
    @JsonProperty("member_stock_cnt")
    private Integer memberStockCnt;
    @JsonProperty("main_grey_capital_in_cnt")
    private Integer mainGreyCapitalInCnt;
    @JsonProperty("main_grey_capital_in")
    private Double mainGreyCapitalIn;
    @JsonProperty("main_grey_pct")
    private Double mainGreyPct;
    @JsonProperty("main_grey_capital_in_cnt_pct")
    private Double mainGreyCapitalInCntPct;
    private List<BlockMemberDTO> stocks;

    // Getters and Setters
    public String getBlockCode() { return blockCode; }
    public void setBlockCode(String blockCode) { this.blockCode = blockCode; }
    public String getBlockName() { return blockName; }
    public void setBlockName(String blockName) { this.blockName = blockName; }
    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }
    public Double getZf() { return zf; }
    public void setZf(Double zf) { this.zf = zf; }
    public Integer getMemberStockCnt() { return memberStockCnt; }
    public void setMemberStockCnt(Integer memberStockCnt) { this.memberStockCnt = memberStockCnt; }
    public Integer getMainGreyCapitalInCnt() { return mainGreyCapitalInCnt; }
    public void setMainGreyCapitalInCnt(Integer mainGreyCapitalInCnt) { this.mainGreyCapitalInCnt = mainGreyCapitalInCnt; }
    public Double getMainGreyCapitalIn() { return mainGreyCapitalIn; }
    public void setMainGreyCapitalIn(Double mainGreyCapitalIn) { this.mainGreyCapitalIn = mainGreyCapitalIn; }
    public Double getMainGreyPct() { return mainGreyPct; }
    public void setMainGreyPct(Double mainGreyPct) { this.mainGreyPct = mainGreyPct; }
    public Double getMainGreyCapitalInCntPct() { return mainGreyCapitalInCntPct; }
    public void setMainGreyCapitalInCntPct(Double mainGreyCapitalInCntPct) { this.mainGreyCapitalInCntPct = mainGreyCapitalInCntPct; }
    public List<BlockMemberDTO> getStocks() { return stocks; }
    public void setStocks(List<BlockMemberDTO> stocks) { this.stocks = stocks; }
}
