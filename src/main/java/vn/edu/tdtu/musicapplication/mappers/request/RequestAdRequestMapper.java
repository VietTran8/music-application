package vn.edu.tdtu.musicapplication.mappers.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.RequestAdvertisingRequest;
import vn.edu.tdtu.musicapplication.enums.EAdStatus;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;
import vn.edu.tdtu.musicapplication.service.AdPackageService;

@Component
public class RequestAdRequestMapper implements Mapper<Advertisement, RequestAdvertisingRequest> {
    @Override
    public Advertisement mapToObject(RequestAdvertisingRequest dto) {
        Advertisement advertisement = new Advertisement();
        advertisement.setActive(false);
        advertisement.setProductName(dto.getProductName());
        advertisement.setImageUrl(dto.getImageUrl());
        advertisement.setContactInfo(dto.getContactInfo());
        advertisement.setEnterpriseInfo(dto.getEnterpriseInfo());
        advertisement.setStatus(EAdStatus.PENDING);
        advertisement.setAmount(dto.getAmount());

        return advertisement;
    }

    @Override
    public RequestAdvertisingRequest mapToDto(Advertisement object) {
        return null;
    }
}
