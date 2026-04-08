package com.myhexin.thsmember.service;

import com.myhexin.thsmember.model.response.IndicatorRankData;

/**
 * Created by Gemini
 */
public interface IndicatorService {
    IndicatorRankData getIndicatorRankings(String date);
}
