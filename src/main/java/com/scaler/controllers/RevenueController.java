package com.scaler.controllers;

import com.scaler.dtos.CalculateRevenueRequestDto;
import com.scaler.dtos.CalculateRevenueResponseDto;
import com.scaler.dtos.ResponseStatus;
import com.scaler.exceptions.UnAuthorizedAccess;
import com.scaler.exceptions.UserNotFoundException;
import com.scaler.models.AggregatedRevenue;
import com.scaler.services.RevenueService;
import org.springframework.stereotype.Controller;

@Controller
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    public CalculateRevenueResponseDto calculateRevenue(CalculateRevenueRequestDto requestDto){
        CalculateRevenueResponseDto responseDto = new CalculateRevenueResponseDto();
        try {
            AggregatedRevenue aggregatedRevenue = revenueService.calculateRevenue(requestDto.getUserId(), requestDto.getRevenueQueryType());

            responseDto.setAggregatedRevenue(aggregatedRevenue);
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (UnAuthorizedAccess | UserNotFoundException e) {
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }
}
