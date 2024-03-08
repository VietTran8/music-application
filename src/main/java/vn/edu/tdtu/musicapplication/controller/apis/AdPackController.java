package vn.edu.tdtu.musicapplication.controller.apis;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.RequestAdvertisingRequest;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AdApprovalRequest;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddAdPackRequest;
import vn.edu.tdtu.musicapplication.service.AdPackageService;

import java.security.Principal;

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

    @GetMapping("/for-display")
    public ResponseEntity<?> adForDisplay(HttpSession session){
        if(session.getAttribute("premiumAdPage") == null){
            session.setAttribute("premiumAdPage", 0);
        }
        if(session.getAttribute("medAdPage") == null){
            session.setAttribute("medAdPage", 0);
        }
        if(session.getAttribute("norAdPage") == null){
            session.setAttribute("norAdPage", 0);
        }
        BaseResponse<?> response = adPackageService.getAd(session);

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

    @GetMapping("/ad")
    public ResponseEntity<?> getAllAds(){
        BaseResponse<?> response = adPackageService.getAllAds();

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/ad/{id}")
    public ResponseEntity<?> getAdById(@PathVariable("id") Long id){
        BaseResponse<?> response = adPackageService.getAdById(id);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(@PathVariable("id") Long id){
        BaseResponse<?> response = adPackageService.getPackageById(id);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitAdvertisement(Principal principal, @RequestBody RequestAdvertisingRequest request){
        BaseResponse<?> response = adPackageService.requestAdvertising(principal, request);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/approval")
    public ResponseEntity<?> adApproval(@RequestBody AdApprovalRequest request){
        BaseResponse<?> response = adPackageService.adApproval(request);

        return ResponseEntity.status(response.getCode()).body(response);
    }
}