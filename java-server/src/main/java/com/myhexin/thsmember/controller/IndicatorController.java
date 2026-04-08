package com.myhexin.thsmember.controller;

import com.myhexin.thsmember.model.response.IndicatorRankData;
import com.myhexin.thsmember.model.response.ResultDTO;
import com.myhexin.thsmember.service.IndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Gemini
 */
@RestController
@RequestMapping("/indicator/capital/v1")
public class IndicatorController {

    @Autowired
    private IndicatorService indicatorService;

    @GetMapping("/grey_rank")
    public ResultDTO<IndicatorRankData> getGreyRank(@RequestParam(required = false) String date) {
        return ResultDTO.success(indicatorService.getIndicatorRankings(date));
    }
}
