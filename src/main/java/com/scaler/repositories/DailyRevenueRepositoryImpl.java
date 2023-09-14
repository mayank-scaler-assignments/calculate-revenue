package com.scaler.repositories;

import com.scaler.models.DailyRevenue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class DailyRevenueRepositoryImpl implements DailyRevenueRepository{

    List<DailyRevenue> dailyRevenues;

    public DailyRevenueRepositoryImpl() {
        this.dailyRevenues = new ArrayList<>();
    }

    @Override
    public DailyRevenue save(DailyRevenue dailyRevenue) {
        dailyRevenues.add(dailyRevenue);
        return dailyRevenue;
    }

    @Override
    public List<DailyRevenue> getDailyRevenueBetweenDates(Date startDate, Date endDate) {
        return dailyRevenues.stream().filter(dailyRevenue ->
                dailyRevenue.getDate().after(startDate) &&
                        dailyRevenue.getDate().before(endDate))
                .toList();
    }
}
