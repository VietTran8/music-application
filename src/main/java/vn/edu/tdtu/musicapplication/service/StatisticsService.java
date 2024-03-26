package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.RevenueStatisticsDTO;
import vn.edu.tdtu.musicapplication.enums.EProductType;
import vn.edu.tdtu.musicapplication.models.Bill;
import vn.edu.tdtu.musicapplication.repository.BillRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final BillRepository billRepository;

    public BaseResponse<?> getYearStatistic(int year){
        BaseResponse<Map<EProductType, Map<Integer, BigDecimal>>> response = new BaseResponse<>();

        List<RevenueStatisticsDTO> statisticsDTOList = billRepository.calculateMonthlyRevenueByProductTypeForYear(year);

        Map<EProductType, List<RevenueStatisticsDTO>> eProductTypeListMap = statisticsDTOList
                .stream()
                .collect(
                        Collectors.groupingBy(RevenueStatisticsDTO::getProductType)
                );

        Map<Integer, BigDecimal> adsRevenue = new HashMap<>();
        Map<Integer, BigDecimal> packRevenue = new HashMap<>();

        IntStream.rangeClosed(1, 12).forEach(i -> {
            adsRevenue.put(i, BigDecimal.ZERO);
            packRevenue.put(i, BigDecimal.ZERO);
        });

        eProductTypeListMap.getOrDefault(EProductType.ADS, new ArrayList<>())
                .forEach(item -> {
                    adsRevenue.put(item.getTimeUnit(), item.getTotalRevenue());
                });
        eProductTypeListMap.getOrDefault(EProductType.UPGRADE_PACKAGE, new ArrayList<>())
                .forEach(item -> {
                    packRevenue.put(item.getTimeUnit(), item.getTotalRevenue());
                });

        Map<EProductType, Map<Integer, BigDecimal>> data = new HashMap<>();
        data.put(EProductType.ADS, adsRevenue);
        data.put(EProductType.UPGRADE_PACKAGE, packRevenue);

        response.setStatus(true);
        response.setData(data);
        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("Statistics fetched successfully!");

        return response;
    }

    public BaseResponse<?> getMonthStatistics(int month, int year){
        BaseResponse<Map<EProductType, Map<Integer, BigDecimal>>> response = new BaseResponse<>();

        List<Bill> bills = billRepository.calculateWeeklyRevenueByProductTypeForCurrentMonth(month, year);

        response.setStatus(true);
        response.setData(Bill.calculateWeeklyRevenueByProductType(bills, YearMonth.of(year, month)));
        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("Statistics fetched successfully!");

        return response;
    }
}
