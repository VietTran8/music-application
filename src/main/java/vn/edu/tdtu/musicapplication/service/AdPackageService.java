package vn.edu.tdtu.musicapplication.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.MailDetails;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AdApprovalRequest;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddAdPackRequest;
import vn.edu.tdtu.musicapplication.dtos.request.RequestAdvertisingRequest;
import vn.edu.tdtu.musicapplication.dtos.response.AdResponse;
import vn.edu.tdtu.musicapplication.enums.EAdStatus;
import vn.edu.tdtu.musicapplication.enums.EAdType;
import vn.edu.tdtu.musicapplication.enums.EUploadFolder;
import vn.edu.tdtu.musicapplication.mappers.request.AddAdPackRequestMapper;
import vn.edu.tdtu.musicapplication.mappers.request.RequestAdRequestMapper;
import vn.edu.tdtu.musicapplication.models.Bill;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;
import vn.edu.tdtu.musicapplication.models.advertisement.AdvertisementPackage;
import vn.edu.tdtu.musicapplication.models.advertisement.ContactInfo;
import vn.edu.tdtu.musicapplication.repository.AdvertisementPackageRepository;
import vn.edu.tdtu.musicapplication.repository.AdvertisementRepository;
import vn.edu.tdtu.musicapplication.service.cloudinary.FileServiceImpl;
import vn.edu.tdtu.musicapplication.service.mail.MailService;
import vn.edu.tdtu.musicapplication.utils.FileHandle;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdPackageService {
    private final AdvertisementPackageRepository adPackRepository;
    private final AdvertisementRepository adRepository;
    private final AddAdPackRequestMapper addAdPackRequestMapper;
    private final RequestAdRequestMapper requestAdRequestMapper;
    private final UserService userService;
    private final MailService mailService;
    private final Configuration config;
    private static final String CREATE_PAYMENT_URL = "http://localhost:8080/payment/ad/create-payment";

    public AdvertisementPackage findById(Long id){
        return adPackRepository.findByIdAndActive(id, true).orElse(null);
    }

    public List<AdvertisementPackage> findAllByIds(List<Long> ids){
        List<AdvertisementPackage> result = new ArrayList<>();
        adPackRepository.findAll().forEach(mPackage -> {
            if(ids.contains(mPackage.getId()) && mPackage.getActive()){
                result.add(mPackage);
            }
        });
        return result;
    }

    public AdvertisementPackage save(AdvertisementPackage mPackage){
        return adPackRepository.save(mPackage);
    }

    public BaseResponse<?> savePackage(AddAdPackRequest request){
        AdvertisementPackage mPackage = addAdPackRequestMapper.mapToObject(request);

        BaseResponse<AdvertisementPackage> response = new BaseResponse<>();
        response.setStatus(false);
        response.setMessage("This package has already existed");
        response.setData(null);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);

        if(!adPackRepository.existsByNameAndActive(mPackage.getName(), true)){
            adPackRepository.save(mPackage);
            response.setStatus(true);
            response.setMessage("Package added successfully");
            response.setData(mPackage);
            response.setCode(HttpServletResponse.SC_CREATED);
        }

        return response;
    }

    public BaseResponse<?> updatePackage(Long id, AddAdPackRequest request){
        AdvertisementPackage mPackage = findById(id);
        BaseResponse<AdvertisementPackage> response = new BaseResponse<>();
        response.setMessage("Package not found with id: " + id);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setData(null);
        response.setStatus(false);

        if(mPackage != null && mPackage.getActive()){
            if(!adPackRepository.existsByNameAndActive(request.getName(), true)) {
                addAdPackRequestMapper.bindFromDto(mPackage, request);
                adPackRepository.save(mPackage);

                response.setMessage("Package updated successfully");
                response.setCode(HttpServletResponse.SC_ACCEPTED);
                response.setData(mPackage);
                response.setStatus(true);
            }else{
                response.setStatus(false);
                response.setMessage("This package has already existed");
                response.setData(null);
                response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
        }

        return response;
    }

    public BaseResponse<?> deletePackage(Long id){
        AdvertisementPackage foundPackage = findById(id);
        BaseResponse<AdvertisementPackage> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setStatus(false);
        response.setMessage("Package not found with id: " + id);

        if(foundPackage != null && foundPackage.getActive()){
            foundPackage.setActive(false);
            adPackRepository.save(foundPackage);

            response.setData(null);
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setStatus(true);
            response.setMessage("Package deleted successfully");
        }

        return response;
    }

    public BaseResponse<?> getAllPackages(){
        List<AdvertisementPackage> packages = adPackRepository.findByActive(true);

        Map<EAdType, List<AdvertisementPackage>> data = packages.stream().collect(
                Collectors.groupingBy(AdvertisementPackage::getType)
        );

        BaseResponse<Map<EAdType, List<AdvertisementPackage>>> response = new BaseResponse<>();
        response.setMessage("Packages fetched successfully");
        response.setData(data);
        response.setStatus(true);
        response.setCode(HttpServletResponse.SC_OK);

        return response;
    }

    public BaseResponse<?> getPackageById(Long id){
        AdvertisementPackage mPackage = adPackRepository.findById(id).orElse(null);
        BaseResponse<AdvertisementPackage> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(false);
        response.setMessage("Package not found with id: " + id);

        if(mPackage != null && mPackage.getActive()) {
            response.setData(mPackage);
            response.setCode(HttpServletResponse.SC_OK);
            response.setStatus(true);
            response.setMessage("Package fetched successfully");
        }

        return response;
    }

    public BaseResponse<?> requestAdvertising(Principal principal, RequestAdvertisingRequest request){
        BaseResponse<Advertisement> response = new BaseResponse<>();
        response.setMessage("You are not authenticated");
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setData(null);
        response.setStatus(false);

        if(principal != null){
            Advertisement advertisement = requestAdRequestMapper.mapToObject(request);
            AdvertisementPackage adPackage = findById(request.getPackageId());
            User foundUser = userService.findByEmail(principal.getName());

            response.setMessage("Package not found with id: " + request.getPackageId());
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);
            response.setStatus(false);

            if(adPackage != null) {
                advertisement.setAPackage(adPackage);
                advertisement.setUser(foundUser);

                adRepository.save(advertisement);

                response.setMessage("Advertisement submitted successfully");
                response.setCode(HttpServletResponse.SC_ACCEPTED);
                response.setData(advertisement);
                response.setStatus(true);
            }
        }

        return response;
    }

    public BaseResponse<?> adApproval(AdApprovalRequest request){
        Advertisement foundAd = adRepository.findById(request.getAdId()).orElse(null);
        BaseResponse<Advertisement> response = new BaseResponse<>();
        response.setStatus(false);
        response.setMessage("Advertisement not found with id: " + request.getAdId());
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setData(null);

        if(foundAd != null){
            //If found ad not paid yet
            if(!foundAd.getActive()){
                foundAd.setStatus(EAdStatus.valueOf(request.getStatus().toUpperCase()));

                adRepository.save(foundAd);

                String createPaymentUrl = CREATE_PAYMENT_URL;

                if(foundAd.getStatus() == EAdStatus.APPROVED){
                    createPaymentUrl = CREATE_PAYMENT_URL + "?adId=" + foundAd.getId();
                }

                sendMailToUser(request, foundAd, createPaymentUrl);

                response.setStatus(true);
                response.setMessage("Advertisement status updated successfully");
                response.setCode(HttpServletResponse.SC_OK);
                response.setData(foundAd);
            }else{
                response.setStatus(false);
                response.setMessage("This advertisement is paid");
                response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.setData(null);
            }
        }

        return response;
    }

    private void sendMailToUser(AdApprovalRequest request, Advertisement advertisement, String createPaymentUrl){
        try{
            MailDetails mailDetails = new MailDetails();
            mailDetails.setSubject("Kết quả phê duyệt quảng cáo".toUpperCase());
            mailDetails.setSendTo(advertisement.getUser().getEmail());

            switch (advertisement.getStatus()){
                case NOT_APPROVED -> {
                    mailDetails.setText(String.format(
                            "Xin chào %s, quảng cáo [%d] của bạn đã bị từ chối bởi quản trị viên.<br>" +
                                    "Lý do: %s. <br>" +
                                    "Nếu có thắc mắc, vui lòng liên hệ đường dây nóng: 1999.2923.<br>" +
                                    "Cảm ơn bạn đã liên hệ với chúng tôi, mong nhận được những yêu cầu quảng cáo khác của bạn!",
                            advertisement.getContactInfo().getFullName(), advertisement.getId(), request.getReason()
                    ));
                }
                case APPROVED -> {
                    Map<String, Object> model = new HashMap<>();
                    ContactInfo contactInfo = advertisement.getContactInfo();

                    model.put("username", advertisement.getUser().getUsername());
                    model.put("adId", request.getAdId());
                    model.put("day", LocalDateTime.now().getDayOfMonth());
                    model.put("month", LocalDateTime.now().getMonth().getValue());
                    model.put("year", String.valueOf(LocalDateTime.now().getYear()));
                    model.put("fullName", contactInfo.getFullName());
                    model.put("birthYear", String.valueOf(contactInfo.getBirthDay().getYear()));
                    model.put("legalDocId", contactInfo.getLegalDocId());
                    model.put("issuedBy", contactInfo.getIssuedBy());
                    model.put("dated", contactInfo.getDated().getDayOfMonth()+"/"+contactInfo.getDated().getMonth().getValue()+"/"+String.valueOf(contactInfo.getDated().getYear()));
                    model.put("address",contactInfo.getAddress());
                    model.put("productName",advertisement.getProductName());
                    model.put("amount",advertisement.getAmount());
                    model.put("duration",advertisement.getAPackage().getUnit().days);
                    model.put("createPaymentUrl", createPaymentUrl);

                    Template t = config.getTemplate("hopdong.html");
                    String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

                    mailDetails.setText(html);
                }
            }
            mailService.sendMail(mailDetails);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public BaseResponse<?> getAllAds(){
        List<Advertisement> ads = adRepository.findAll();

        BaseResponse<List<Advertisement>> response = new BaseResponse<>();
        response.setMessage("Ads fetched successfully");
        response.setData(ads);
        response.setStatus(true);
        response.setCode(HttpServletResponse.SC_OK);

        return response;
    }

    public BaseResponse<?> getAdById(Long id){
        Advertisement ad = adRepository.findById(id).orElse(null);

        BaseResponse<Advertisement> response = new BaseResponse<>();
        response.setMessage(ad != null ? "Ad fetched successfully" : "Ad not found with id: " + id);
        response.setData(ad);
        response.setStatus(ad != null);
        response.setCode(ad != null ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);

        return response;
    }

    public BaseResponse<?> getAd(HttpSession session){
        Integer premiumAdPage = (Integer) session.getAttribute("premiumAdPage");
        Integer medAdPage = (Integer) session.getAttribute("medAdPage");
        Integer norAdPage = (Integer) session.getAttribute("norAdPage");

        Page<Advertisement> premiumAds = adRepository.findActiveAndApprovedAdvertisementsWithExpirationAfter(LocalDateTime.now(), EAdType.TYPE_PREMIUM, PageRequest.of(premiumAdPage, 1));
        Page<Advertisement> norAds = adRepository.findActiveAndApprovedAdvertisementsWithExpirationAfter(LocalDateTime.now(), EAdType.TYPE_NORMAL, PageRequest.of(norAdPage, 1));
        Page<Advertisement> medAds = adRepository.findActiveAndApprovedAdvertisementsWithExpirationAfter(LocalDateTime.now(), EAdType.TYPE_MEDIUM, PageRequest.of(medAdPage, 1));

        List<Advertisement> preAdList = premiumAds.get().toList();
        List<Advertisement> medAdList = medAds.get().toList();
        List<Advertisement> norAdList = norAds.get().toList();

        if(premiumAdPage < premiumAds.getTotalPages() - 1){
            session.setAttribute("premiumAdPage", premiumAdPage + 1);
        }else{
            session.setAttribute("premiumAdPage", 0);
        }

        if(medAdPage < medAds.getTotalPages() - 1){
            session.setAttribute("medAdPage", medAdPage + 1);
        }else{
            session.setAttribute("medAdPage", 0);
        }

        if(norAdPage < norAds.getTotalPages() - 1){
            session.setAttribute("norAdPage", norAdPage + 1);
        }else{
            session.setAttribute("norAdPage", 0);
        }

        AdResponse data = new AdResponse();

        if(!preAdList.isEmpty())
            data.setPreAdImg(preAdList.get(0).getImageUrl());
        if(!norAdList.isEmpty())
            data.setNorAdImg(norAdList.get(0).getImageUrl());
        if(!medAdList.isEmpty())
            data.setMedAdImg(medAdList.get(0).getImageUrl());

        BaseResponse<AdResponse> response = new BaseResponse<>();
        response.setStatus(true);
        response.setMessage("Ad fetched successfully!");
        response.setCode(HttpServletResponse.SC_OK);
        response.setData(data);

        return response;
    }
}