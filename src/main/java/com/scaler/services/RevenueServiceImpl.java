package com.scaler.services;

import com.scaler.exceptions.UnAuthorizedAccess;
import com.scaler.exceptions.UserNotFoundException;
import com.scaler.models.AggregatedRevenue;
import com.scaler.models.User;
import com.scaler.models.UserType;
import com.scaler.repositories.DailyRevenueRepository;
import com.scaler.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class RevenueServiceImpl implements RevenueService{

    private final UserRepository userRepository;

    public RevenueServiceImpl(DailyRevenueRepository dailyRevenueRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AggregatedRevenue calculateRevenue(long userId, String queryType) throws UnAuthorizedAccess, UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getUserType() != UserType.BILLING) {
            throw new UnAuthorizedAccess("User not authorized to access this service");
        }

        double revenueFromFoodSales;
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        switch (queryType) {
            case "CURRENT_FY":
                // Revenue for the current financial year
                revenueFromFoodSales = currentMonth * (currentMonth + 1) * 1000 / 2.0;
                break;

            case "PREVIOUS_FY":
                // Revenue for the previous financial year
                revenueFromFoodSales = 12 * (12 + 1) * 1000 / 2.0;
                break;

            case "CURRENT_MONTH":
                // Revenue for the current month
                revenueFromFoodSales = currentMonth * 1000;
                break;

            case "PREVIOUS_MONTH":
                // Revenue for the previous month
                int previousMonth = currentMonth == 1 ? 12 : currentMonth - 1;
                revenueFromFoodSales = previousMonth * 1000;
                break;

            default:
                throw new IllegalArgumentException("Invalid query type: " + queryType);
        }

        double totalGst = revenueFromFoodSales * 0.05;
        double totalServiceCharge = revenueFromFoodSales * 0.1;

        AggregatedRevenue aggregatedRevenue = new AggregatedRevenue();
        aggregatedRevenue.setRevenueFromFoodSales(revenueFromFoodSales);
        aggregatedRevenue.setTotalGst(totalGst);
        aggregatedRevenue.setTotalServiceCharge(totalServiceCharge);

        return aggregatedRevenue;
    }
}
