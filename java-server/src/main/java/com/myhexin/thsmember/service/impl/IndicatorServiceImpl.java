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
    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Override
    public IndicatorRankData getIndicatorRankings(String date, Integer pageNum, Integer pageSize) {
        String targetDate = date;
        if (!StringUtils.hasText(targetDate)) {
            targetDate = getLatestDate();
        }

        int safePageNum = pageNum == null || pageNum < 1 ? DEFAULT_PAGE_NUM : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;

        List<StockRankDTO> allStocks = loadStocks(targetDate);
        long total = allStocks.size();
        List<StockRankDTO> pagedStocks = paginateStocks(allStocks, safePageNum, safePageSize);
        fillAbsoluteIndex(pagedStocks, safePageNum, safePageSize);

        IndicatorRankData data = new IndicatorRankData();
        data.setTotal(total);
        data.setStockList(pagedStocks);
        data.setBlockList(loadBlocks(targetDate));
        data.setUpdateTime(loadMetadata(targetDate));
        return data;
    }

    private List<StockRankDTO> paginateStocks(List<StockRankDTO> allStocks, int pageNum, int pageSize) {
        if (allStocks.isEmpty()) {
            return new ArrayList<>();
        }
        int offset = (pageNum - 1) * pageSize;
        if (offset >= allStocks.size()) {
            return new ArrayList<>();
        }
        int toIndex = Math.min(offset + pageSize, allStocks.size());
        return new ArrayList<>(allStocks.subList(offset, toIndex));
    }

    private void fillAbsoluteIndex(List<StockRankDTO> pagedStocks, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        for (int i = 0; i < pagedStocks.size(); i++) {
            pagedStocks.get(i).setIndex(offset + i + 1);
        }
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
