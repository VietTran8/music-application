package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddUserPackageBoughtRequest;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddPackageRequest;
import vn.edu.tdtu.musicapplication.dtos.response.CreatePaymentResponse;
import vn.edu.tdtu.musicapplication.enums.EPaymentMethod;
import vn.edu.tdtu.musicapplication.mappers.request.AddPackageRequestMapper;
import vn.edu.tdtu.musicapplication.models.Package;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.UserPackageBought;
import vn.edu.tdtu.musicapplication.repository.PackageRepository;
import vn.edu.tdtu.musicapplication.repository.UserPackageBoughtRepository;
import vn.edu.tdtu.musicapplication.service.vnpay.VnPayService;
import vn.edu.tdtu.musicapplication.utils.PrincipalUtils;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PackageService {
    private final PackageRepository packageRepository;
    private final UserPackageBoughtRepository userPackageBoughtRepository;
    private final AddPackageRequestMapper addPackageRequestMapper;
    private final UserService userService;
    private final VnPayService vnPayService;
    private final PrincipalUtils principalUtils;
    private static final String RETURN_URL = "http://localhost:8080/payment/return/";

    public Package findById(Long id){
        return packageRepository.findByIdAndActive(id, true).orElse(null);
    }

    public List<Package> findAllByIds(List<Long> ids){
        List<Package> result = new ArrayList<>();
        packageRepository.findAll().forEach(mPackage -> {
            if(ids.contains(mPackage.getId()) && mPackage.getActive()){
                result.add(mPackage);
            }
        });
        return result;
    }

    public Package save(Package mPackage){
        return packageRepository.save(mPackage);
    }

    public BaseResponse<?> savePackage(AddPackageRequest request){
        Package mPackage = addPackageRequestMapper.mapToObject(request);

        BaseResponse<Package> response = new BaseResponse<>();
        response.setStatus(false);
        response.setMessage("This package has already existed");
        response.setData(null);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);

        if(!packageRepository.existsByNameAndActive(mPackage.getName(), true)){
            packageRepository.save(mPackage);
            response.setStatus(true);
            response.setMessage("Package added successfully");
            response.setData(mPackage);
            response.setCode(HttpServletResponse.SC_CREATED);
        }

        return response;
    }

    public BaseResponse<?> updatePackage(Long id, AddPackageRequest request){
        Package mPackage = findById(id);
        BaseResponse<Package> response = new BaseResponse<>();
        response.setMessage("Package not found with id: " + id);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setData(null);
        response.setStatus(false);

        if(mPackage != null && mPackage.getActive()){
            addPackageRequestMapper.bindFromDto(mPackage, request);
            packageRepository.save(mPackage);

            response.setMessage("Package updated successfully");
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setData(mPackage);
        }

        return response;
    }

    public BaseResponse<?> deletePackage(Long id){
        Package foundPackage = findById(id);
        BaseResponse<Package> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setStatus(false);
        response.setMessage("Package not found with id: " + id);

        if(foundPackage != null && foundPackage.getActive()){
            foundPackage.setActive(false);
            packageRepository.save(foundPackage);

            response.setData(null);
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setStatus(true);
            response.setMessage("Package deleted successfully");
        }

        return response;
    }

    public BaseResponse<List<Package>> getAllPackages(){
        List<Package> packages = packageRepository.findByActive(true);

        BaseResponse<List<Package>> response = new BaseResponse<>();
        response.setMessage("Packages fetched successfully");
        response.setData(packages);
        response.setStatus(true);
        response.setCode(HttpServletResponse.SC_OK);

        return response;
    }

    public BaseResponse<Map<String, Object>> getAllPackagesForView(int page, int limit){
        Page<Package> packages = packageRepository.findByActive(true, PageRequest.of(page - 1, limit));

        Map<String, Object> data = new HashMap<>();
        data.put("totalPages", packages.getTotalPages());
        data.put("packages", packages.get());

        BaseResponse<Map<String, Object>> response = new BaseResponse<>();
        response.setMessage("Packages fetched successfully");
        response.setData(data);
        response.setStatus(true);
        response.setCode(HttpServletResponse.SC_OK);

        return response;
    }

    public BaseResponse<?> getPackageById(Long id){
        Package mPackage = packageRepository.findById(id).orElse(null);
        BaseResponse<Package> response = new BaseResponse<>();
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

    public BaseResponse<?> createPayment(Principal principal, HttpServletRequest request, AddUserPackageBoughtRequest requestBody){
        String ipAddress = request.getRemoteAddr();

        BaseResponse<CreatePaymentResponse> response = new BaseResponse<>();
        response.setMessage("You are not authenticated");
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setData(null);
        response.setStatus(false);

        if(principal != null){
            User foundUser = principalUtils.loadUserFromPrincipal(principal);

            response.setMessage("Package not found");
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);

            if (foundUser != null){
                Package foundPackage = findById(requestBody.getPackageId());

                if(foundPackage != null && foundPackage.getActive()){
                    UserPackageBought userPackageBought = new UserPackageBought();
                    if(!didBuyPackage(foundUser, foundPackage)){
                        userPackageBought.setMPackage(foundPackage);
                        userPackageBought.setStatus(false);
                        userPackageBought.setUser(foundUser);
                        userPackageBought.setAmount(requestBody.getAmount());
                        userPackageBought.setPaymentMethod(EPaymentMethod.METHOD_VNPAY);
                        userPackageBoughtRepository.save(userPackageBought);
                    }else{
                        userPackageBought = foundUser.getPackages().stream().filter(
                                p -> userPackageIsValid(p, foundPackage)
                        ).toList().get(0);
                    }
                    try {
                        String paymentUrl = vnPayService.createPayment(requestBody.getAmount().longValue(), RETURN_URL, ipAddress, userPackageBought.getId());
                        CreatePaymentResponse data = new CreatePaymentResponse();
                        data.setUrl(paymentUrl);

                        response.setMessage("Payment created successfully");
                        response.setCode(HttpServletResponse.SC_CREATED);
                        response.setData(data);
                        response.setStatus(true);

                    }catch (UnsupportedEncodingException e){
                        log.error(e.getMessage());
                        response.setMessage(e.getMessage());
                        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
                    }
                }
            }else{
                response.setMessage("Chưa đăng nhập!");
                response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
                response.setData(null);
                response.setStatus(true);
            }
        }

        return response;
    }

    public List<UserPackageBought> findUserPackageByUser(User user){
        return userPackageBoughtRepository
                .findByUserAndStatus(user, true)
                .stream()
                .filter(UserPackageBought::isNotExpired)
                .toList();
    }

    public UserPackageBought findUserPackageById(Long id){
        return userPackageBoughtRepository.findById(id).orElse(null);
    }

    public UserPackageBought saveUserPackage(UserPackageBought userPackage){
        return userPackageBoughtRepository.save(userPackage);
    }

    public Boolean didBuyPackage(User user, Package mPackage){
        return !user.getPackages().stream()
                .filter(
                    p -> userPackageIsValid(p, mPackage)
                ).toList().isEmpty();
    }

    public Boolean userPackageIsValid(UserPackageBought p, Package mPackage){
        return p.getExpirationDate() != null
                && p.getMPackage().getId() == mPackage.getId()
                && p.getExpirationDate().isAfter(LocalDateTime.now())
                && p.getStatus();
    }
}
