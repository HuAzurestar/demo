package com.myhexin.thsmember.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IndicatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetIndicatorRankings() throws Exception {
        mockMvc.perform(get("/indicator/capital/v1/grey_rank"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(0))
                .andExpect(jsonPath("$.status_msg").value("请求成功!"))
                .andExpect(jsonPath("$.data.stock_list", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.block_list", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.update_time").exists())
                // Verify specific stock data mapping
                .andExpect(jsonPath("$.data.stock_list[0].stock_code").value("002195"))
                .andExpect(jsonPath("$.data.stock_list[0].change_pct").value(22.67))
                .andExpect(jsonPath("$.data.stock_list[0].zf").value(6.171428571428562))
                // Verify block data mapping (zf as change_pct)
                .andExpect(jsonPath("$.data.block_list[0].block_code").value("885530"))
                .andExpect(jsonPath("$.data.block_list[0].zf").value(5.575))
                // Verify block stocks mapping
                .andExpect(jsonPath("$.data.block_list[0].stocks[0].stock_code").value("300139"))
                .andExpect(jsonPath("$.data.block_list[0].stocks[0].zf").value(16.828663998475314));
    }

    @Test
    public void testGetIndicatorRankingsWithDate() throws Exception {
        mockMvc.perform(get("/indicator/capital/v1/grey_rank").param("date", "2026-04-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(0))
                .andExpect(jsonPath("$.data.update_time").value("2026-04-07 15:30"))
                // Verify specific stock data for 2026-04-07
                .andExpect(jsonPath("$.data.stock_list[0].stock_code").value("600010"))
                .andExpect(jsonPath("$.data.stock_list[0].stock_name").value("包钢股份"));
    }
}
