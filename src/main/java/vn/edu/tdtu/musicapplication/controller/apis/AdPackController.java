package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddAdPackRequest;
import vn.edu.tdtu.musicapplication.service.AdPackageService;

@RestController
@RequestMapping("/api/ad-package")
@RequiredArgsConstructor
public class AdPackController {
    private final AdPackageService adPackageService;
    @PostMapping("/add")
    public ResponseEntity<?> addPackage(@RequestBody AddAdPackRequest requestBody){
        BaseResponse<?> response = adPackageService.savePackage(requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updatePackage(@PathVariable("id") Long id, @RequestBody AddAdPackRequest requestBody){
        BaseResponse<?> response = adPackageService.updatePackage(id, requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable("id") Long id){
        BaseResponse<?> response = adPackageService.deletePackage(id);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping()
    public ResponseEntity<?> getAllPackages(){
        BaseResponse<?> response = adPackageService.getAllPackages();

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(@PathVariable("id") Long id){
        BaseResponse<?> response = adPackageService.getPackageById(id);

        return ResponseEntity.status(response.getCode()).body(response);
    }
}
