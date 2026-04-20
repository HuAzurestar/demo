package com.myhexin.thsmember.service.impl;

import com.myhexin.thsmember.model.dto.BlockMemberDTO;
import com.myhexin.thsmember.model.dto.BlockRankDTO;
import com.myhexin.thsmember.model.dto.StockRankDTO;
import com.myhexin.thsmember.model.response.IndicatorRankData;
import com.myhexin.thsmember.service.IndicatorService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by Gemini
 */
@Service
public class IndicatorServiceImpl implements IndicatorService {

    @Override
    public IndicatorRankData getIndicatorRankings(String date) {
        String targetDate = date;
        if (!StringUtils.hasText(targetDate)) {
            targetDate = getLatestDate();
        }

        IndicatorRankData data = new IndicatorRankData();
        List<StockRankDTO> stocks = loadStocks(targetDate);
        enrichRatioAndSortStocks(stocks);
        data.setStockList(stocks);
        data.setBlockList(loadBlocks(targetDate));
        data.setUpdateTime(loadMetadata(targetDate));
        return data;
    }

    private String getLatestDate() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("mockdata/metadata.csv").getInputStream(), StandardCharsets.UTF_8))) {
            br.readLine(); // header
            String firstLine = br.readLine();
            return firstLine != null ? firstLine.split(",")[0] : "2026-04-08";
        } catch (Exception e) {
            return "2026-04-08";
        }
    }

    private List<StockRankDTO> loadStocks(String date) {
        List<StockRankDTO> stocks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("mockdata/stocks.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!values[0].equals(date)) continue;
                StockRankDTO stock = new StockRankDTO();
                stock.setStockCode(values[1]);
                stock.setStockName(values[2]);
                stock.setMarket(values[3]);
                stock.setMainGreyCapital(Double.valueOf(values[4]));
                stock.setMainListedCapital(Double.valueOf(values[5]));
                stock.setMainCapital(Double.valueOf(values[6]));
                stock.setRetailCapital(Double.valueOf(values[7]));
                stock.setTurnover(Double.valueOf(values[8]));
                stock.setChangePct(Double.valueOf(values[9]));
                stock.setZf(Double.valueOf(values[10]));
                stocks.add(stock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stocks;
    }

    private void enrichRatioAndSortStocks(List<StockRankDTO> stocks) {
        for (StockRankDTO s : stocks) {
            s.setRatioPct(computeRatioPct(s.getMainListedCapital(), s.getTurnover()));
        }
        stocks.sort((a, b) -> {
            Double ta = a.getTurnover();
            Double tb = b.getTurnover();
            if (ta == null && tb == null) return 0;
            if (ta == null) return 1;
            if (tb == null) return -1;
            return Double.compare(tb, ta);
        });
    }

    /** 占比 = main_listed_capital / turnover * 100，两位小数 */
    private Double computeRatioPct(Double mainListedCapital, Double turnover) {
        if (mainListedCapital == null || turnover == null) return null;
        if (!Double.isFinite(mainListedCapital) || !Double.isFinite(turnover)) return null;
        if (turnover == 0.0) return null;
        double raw = mainListedCapital / turnover * 100.0;
        if (!Double.isFinite(raw)) return null;
        return Math.round(raw * 100.0) / 100.0;
    }

    private List<BlockRankDTO> loadBlocks(String date) {
        Map<String, List<BlockMemberDTO>> memberMap = loadBlockMembers(date);
        List<BlockRankDTO> blocks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("mockdata/blocks.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!values[0].equals(date)) continue;
                BlockRankDTO block = new BlockRankDTO();
                block.setBlockCode(values[1]);
                block.setBlockName(values[2]);
                block.setMarket(values[3]);
                block.setZf(Double.valueOf(values[4]));
                block.setMemberStockCnt(Integer.valueOf(values[5]));
                block.setMainGreyCapitalInCnt(Integer.valueOf(values[6]));
                block.setMainGreyCapitalIn(Double.valueOf(values[7]));
                block.setMainGreyPct(Double.valueOf(values[8]));
                block.setMainGreyCapitalInCntPct(Double.valueOf(values[9]));
                block.setStocks(memberMap.getOrDefault(block.getBlockCode(), new ArrayList<>()));
                blocks.add(block);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blocks;
    }

    private Map<String, List<BlockMemberDTO>> loadBlockMembers(String date) {
        Map<String, List<BlockMemberDTO>> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("mockdata/block_members.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (!values[0].equals(date)) continue;
                String blockCode = values[1];
                BlockMemberDTO member = new BlockMemberDTO();
                member.setStockCode(values[2]);
                member.setStockName(values[3]);
                member.setMarket(values[4]);
                member.setZf(Double.valueOf(values[5]));
                map.computeIfAbsent(blockCode, k -> new ArrayList<>()).add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private String loadMetadata(String date) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("mockdata/metadata.csv").getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(date)) return values[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date + " 15:30";
    }
}
