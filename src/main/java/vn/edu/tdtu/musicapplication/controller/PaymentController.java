package vn.edu.tdtu.musicapplication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.models.UserPackageBought;
import vn.edu.tdtu.musicapplication.service.PackageService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PackageService packageService;

    @GetMapping("/return/{userPackageId}")
    public String paymentResult(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @PathVariable("userPackageId") Long userPackageId
    ){
        UserPackageBought userPackage = packageService.findUserPackageById(userPackageId);
        if(userPackage != null){
            if(responseCode.equals("00") || responseCode.equals("07")){
                if(!userPackage.getStatus()){
                    userPackage.setStatus(true);
                    userPackage.setBoughtDate(LocalDateTime.now());
                    userPackage.setExpirationDate(
                            LocalDateTime.now().plusDays(userPackage.getMPackage().getDuration())
                    );
                }else{
                    userPackage.setBoughtDate(LocalDateTime.now());
                    if(packageService.userPackageIsValid(userPackage, userPackage.getMPackage())){
                        userPackage.setExpirationDate(
                                userPackage.getExpirationDate()
                                        .plusDays(userPackage.getMPackage().getDuration())
                        );
                    }else{
                        userPackage.setExpirationDate(
                                userPackage.getBoughtDate()
                                        .plusDays(userPackage.getMPackage().getDuration())
                        );
                    }
                }
                packageService.saveUserPackage(userPackage);
                //TODO: Send email to user here

                return "redirect:/payment/success";
            }
        }
        return "redirect:/payment/failure";
    }

    @GetMapping("/success")
    public String successPage(){
        return "success";
    }

    @GetMapping("/failure")
    public String failurePage(){
        return "failure";
    }
}
