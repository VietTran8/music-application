package vn.edu.tdtu.musicapplication.controller.apis;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddUserPackageBoughtRequest;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddGenreRequest;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddPackageRequest;
import vn.edu.tdtu.musicapplication.service.PackageService;

import java.security.Principal;

@RestController
@RequestMapping("/api/package")
@RequiredArgsConstructor
public class PackageController {
    private final PackageService packageService;

    @PostMapping("/add")
    public ResponseEntity<?> addPackage(@RequestBody AddPackageRequest requestBody){
        BaseResponse<?> response = packageService.savePackage(requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updatePackage(@PathVariable("id") Long id, @RequestBody AddPackageRequest requestBody){
        BaseResponse<?> response = packageService.updatePackage(id, requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable("id") Long id){
        BaseResponse<?> response = packageService.deletePackage(id);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping()
    public ResponseEntity<?> getAllPackages(){
        BaseResponse<?> response = packageService.getAllPackages();

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(@PathVariable("id") Long id){
        BaseResponse<?> response = packageService.getPackageById(id);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(Principal principal, HttpServletRequest request, @RequestBody AddUserPackageBoughtRequest requestBody){
        BaseResponse<?> response = packageService.createPayment(principal, request, requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }
}
