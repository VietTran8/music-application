package vn.edu.tdtu.musicapplication.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.enums.EAdStatus;
import vn.edu.tdtu.musicapplication.enums.EPaymentMethod;
import vn.edu.tdtu.musicapplication.enums.EProductType;
import vn.edu.tdtu.musicapplication.models.Bill;
import vn.edu.tdtu.musicapplication.models.UserPackageBought;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;
import vn.edu.tdtu.musicapplication.repository.AdvertisementRepository;
import vn.edu.tdtu.musicapplication.service.AdPackageService;
import vn.edu.tdtu.musicapplication.service.BillService;
import vn.edu.tdtu.musicapplication.service.PackageService;
import vn.edu.tdtu.musicapplication.service.vnpay.VnPayService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PackageService packageService;
    private final AdvertisementRepository advertisementRepository;
    private final VnPayService vnPayService;
    private final BillService billService;
    private final static String RETURN_URL = "http://localhost:8080/payment/ad/return/";

    @GetMapping("/return/{userPackageId}")
    public String paymentResult(
            @RequestParam("vnp_TransactionNo") String transactionNo,
            @RequestParam("vnp_ResponseCode") String responseCode,
            @PathVariable("userPackageId") Long userPackageId
    ){
        UserPackageBought userPackage = packageService.findUserPackageById(userPackageId);
        if(userPackage != null){
            if(responseCode.equals("00") || responseCode.equals("07")){
                if(!userPackage.getStatus()){
                    userPackage.setStatus(true);
                    userPackage.setPaymentMethod(EPaymentMethod.METHOD_VNPAY);
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
                Bill bill = new Bill();
                bill.setPackageProduct(userPackage);
                bill.setAmount(userPackage.getAmount());
                bill.setCreatedDate(LocalDateTime.now());
                bill.setPaymentMethod(userPackage.getPaymentMethod().getDescription());
                bill.setAdProduct(null);
                bill.setTransactionId(transactionNo);
                bill.setProductType(EProductType.UPGRADE_PACKAGE);
                bill.setUser(userPackage.getUser());

                billService.saveBill(bill);
                billService.sendBillToUser("hóa đơn gói nâng cấp tài khoản", bill);

                return "redirect:/payment/success";
            }
        }
        return "redirect:/payment/failure";
    }

    @GetMapping("/ad/return/{adId}")
    public String adPaymentResult(
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TransactionNo") String transactionNo,
            @PathVariable("adId") Long adId
    ){
        Advertisement foundAd = advertisementRepository.findById(adId).orElse(null);

        if(foundAd != null
                && foundAd.getStatus() == EAdStatus.APPROVED
                && !foundAd.getActive()
                && (responseCode.equals("00") || responseCode.equals("07"))
        ){
            foundAd.setBoughtDate(LocalDateTime.now());
            foundAd.setExpirationDate(LocalDateTime.now()
                    .plusDays(foundAd.getAPackage().getUnit().days)
            );
            foundAd.setActive(true);
            foundAd.setPaymentMethod(EPaymentMethod.METHOD_VNPAY);

            Bill bill = new Bill();
            bill.setAdProduct(foundAd);
            bill.setAmount(foundAd.getAmount());
            bill.setCreatedDate(LocalDateTime.now());
            bill.setPaymentMethod(foundAd.getPaymentMethod().getDescription());
            bill.setPackageProduct(null);
            bill.setTransactionId(transactionNo);
            bill.setProductType(EProductType.ADS);
            bill.setUser(foundAd.getUser());

            advertisementRepository.save(foundAd);
            billService.saveBill(bill);
            billService.sendBillToUser("hóa đơn thanh toán quảng cáo", bill);

            return "redirect:/payment/success";
        }
        return "redirect:/payment/failure";
    }

    @GetMapping("/ad/create-payment")
    public String createPayment(
            @RequestParam("adId") Long adId,
            HttpServletRequest request
    ){
        Advertisement foundAd = advertisementRepository.findById(adId).orElse(null);
        if(foundAd != null
                && foundAd.getStatus() == EAdStatus.APPROVED
                && !foundAd.getActive()){
            try{
                String paymentUrl = vnPayService.createPayment(foundAd.getAmount().longValue(), RETURN_URL, request.getRemoteAddr(), foundAd.getId());
                return "redirect:" + paymentUrl;
            }
            catch (UnsupportedEncodingException e){
                log.error(e.getMessage());
            }
        }

        return "redirect:/home";
    }

    @GetMapping("/success")
    public String successPage(){
        return "public/success";
    }

    @GetMapping("/failure")
    public String failurePage(){
        return "public/failure";
    }
}
