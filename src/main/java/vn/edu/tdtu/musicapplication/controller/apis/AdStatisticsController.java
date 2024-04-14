package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.service.AdPackageService;

@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class AdStatisticsController {
    private final AdPackageService adPackageService;
    @GetMapping("/ad/{id}")
    public ResponseEntity<?> getAdStatistic(@PathVariable("id") Long adId){
        BaseResponse<?> response = adPackageService.getAdsStatistic(adId);

        return ResponseEntity.status(response.getCode()).body(response);
    }
}
