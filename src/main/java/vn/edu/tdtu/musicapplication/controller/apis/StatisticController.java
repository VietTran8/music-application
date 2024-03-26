package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.service.StatisticsService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticsService service;
    @GetMapping("/year/{year}")
    public ResponseEntity<?> getYearStatistics(@PathVariable("year") int year){
        BaseResponse<?> response = service.getYearStatistic(year);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/month")
    public ResponseEntity<?> getMonthStatistics(){
        LocalDateTime now = LocalDateTime.now();
        BaseResponse<?> response = service.getMonthStatistics(now.getMonthValue(), now.getYear());

        return ResponseEntity.status(response.getCode()).body(response);
    }
}
