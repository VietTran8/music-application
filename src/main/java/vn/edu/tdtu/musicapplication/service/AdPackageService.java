package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddAdPackRequest;
import vn.edu.tdtu.musicapplication.mappers.request.AddAdPackRequestMapper;
import vn.edu.tdtu.musicapplication.models.advertisement.AdvertisementPackage;
import vn.edu.tdtu.musicapplication.repository.AdvertisementPackageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdPackageService {
    private final AdvertisementPackageRepository adPackRepository;
    private final AddAdPackRequestMapper addAdPackRequestMapper;

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

        BaseResponse<List<AdvertisementPackage>> response = new BaseResponse<>();
        response.setMessage("Packages fetched successfully");
        response.setData(packages);
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

//    public BaseResponse<?> requestAdvertising(){
//
//    }
}
