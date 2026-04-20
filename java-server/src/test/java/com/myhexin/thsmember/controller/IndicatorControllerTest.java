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
                .andExpect(jsonPath("$.data.total", greaterThan(0)))
                .andExpect(jsonPath("$.data.stock_list", hasSize(lessThanOrEqualTo(20))))
                .andExpect(jsonPath("$.data.block_list", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.update_time").exists())
                // 按 turnover 降序首条：2026-04-08 成交额最大为 300308 中际旭创
                .andExpect(jsonPath("$.data.stock_list[0].stock_code").value("300308"))
                .andExpect(jsonPath("$.data.stock_list[0].change_pct").value(14.13))
                .andExpect(jsonPath("$.data.stock_list[0].zf").value(11.074524236114478))
                .andExpect(jsonPath("$.data.stock_list[0].ratio_pct").value(7.9))
                .andExpect(jsonPath("$.data.stock_list[0].index").value(1))
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
                .andExpect(jsonPath("$.data.total").value(20))
                .andExpect(jsonPath("$.data.stock_list", hasSize(20)))
                .andExpect(jsonPath("$.data.update_time").value("2026-04-07 15:30"))
                .andExpect(jsonPath("$.data.stock_list[0].stock_code").value("300308"))
                .andExpect(jsonPath("$.data.stock_list[0].stock_name").value("中际旭创"))
                .andExpect(jsonPath("$.data.stock_list[0].ratio_pct").value(4.91));
    }

    @Test
    public void testPaginationNonLastPageShouldEqualPageSize() throws Exception {
        mockMvc.perform(get("/indicator/capital/v1/grey_rank")
                        .param("date", "2026-04-08")
                        .param("pageNum", "1")
                        .param("pageSize", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(60))
                .andExpect(jsonPath("$.data.stock_list", hasSize(25)))
                .andExpect(jsonPath("$.data.stock_list[0].index").value(1))
                .andExpect(jsonPath("$.data.stock_list[24].index").value(25));
    }

    @Test
    public void testPaginationLastPageCanBeSmallerThanPageSize() throws Exception {
        mockMvc.perform(get("/indicator/capital/v1/grey_rank")
                        .param("date", "2026-04-08")
                        .param("pageNum", "3")
                        .param("pageSize", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(60))
                .andExpect(jsonPath("$.data.stock_list", hasSize(10)))
                .andExpect(jsonPath("$.data.stock_list[0].index").value(51))
                .andExpect(jsonPath("$.data.stock_list[9].index").value(60));
    }

    @Test
    public void testPaginationOversizedPageReturnsEmptyWithTotal() throws Exception {
        mockMvc.perform(get("/indicator/capital/v1/grey_rank")
                        .param("date", "2026-04-07")
                        .param("pageNum", "99")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(20))
                .andExpect(jsonPath("$.data.stock_list", hasSize(0)));
    }

    @Test
    public void testPaginationMiddlePageIndexMonotonic() throws Exception {
        mockMvc.perform(get("/indicator/capital/v1/grey_rank")
                        .param("date", "2026-04-08")
                        .param("pageNum", "2")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(60))
                .andExpect(jsonPath("$.data.stock_list", hasSize(10)))
                .andExpect(jsonPath("$.data.stock_list[0].index").value(11))
                .andExpect(jsonPath("$.data.stock_list[9].index").value(20));
    }

    @Test
    public void testPaginationEmptyDataset() throws Exception {
        mockMvc.perform(get("/indicator/capital/v1/grey_rank")
                        .param("date", "1900-01-01")
                        .param("pageNum", "1")
                        .param("pageSize", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status_code").value(0))
                .andExpect(jsonPath("$.data.total").value(0))
                .andExpect(jsonPath("$.data.stock_list", hasSize(0)));
    }
}
