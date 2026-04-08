package com.myhexin.thsmember.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myhexin.thsmember.model.dto.BlockRankDTO;
import com.myhexin.thsmember.model.dto.StockRankDTO;
import java.util.List;

/**
 * Created by Gemini
 */
public class IndicatorRankData {
    @JsonProperty("stock_list")
    private List<StockRankDTO> stockList;
    @JsonProperty("block_list")
    private List<BlockRankDTO> blockList;
    @JsonProperty("update_time")
    private String updateTime;

    // Getters and Setters
    public List<StockRankDTO> getStockList() { return stockList; }
    public void setStockList(List<StockRankDTO> stockList) { this.stockList = stockList; }
    public List<BlockRankDTO> getBlockList() { return blockList; }
    public void setBlockList(List<BlockRankDTO> blockList) { this.blockList = blockList; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
