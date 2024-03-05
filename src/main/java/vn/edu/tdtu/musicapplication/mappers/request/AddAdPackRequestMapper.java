package vn.edu.tdtu.musicapplication.mappers.request;

import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.AddAdPackRequest;
import vn.edu.tdtu.musicapplication.enums.EAdType;
import vn.edu.tdtu.musicapplication.enums.EAdUnit;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.advertisement.AdvertisementPackage;

@Component
public class AddAdPackRequestMapper implements Mapper<AdvertisementPackage, AddAdPackRequest> {
    @Override
    public AdvertisementPackage mapToObject(AddAdPackRequest dto) {
        AdvertisementPackage adPack = new AdvertisementPackage();
        adPack.setPrice(dto.getPrice());
        adPack.setName(dto.getName());
        adPack.setType(EAdType.valueOf(dto.getType().toUpperCase()));
        adPack.setUnit(EAdUnit.valueOf(dto.getUnit().toUpperCase()));
        adPack.setDescription(dto.getDescription());
        adPack.setActive(true);
        adPack.setDiscount(dto.getDiscount());
        adPack.setSpecialFeatures(dto.getSpecialFeatures());

        return adPack;
    }

    @Override
    public AddAdPackRequest mapToDto(AdvertisementPackage object) {
        return null;
    }

    public void bindFromDto(AdvertisementPackage adPack, AddAdPackRequest dto) {
        adPack.setPrice(dto.getPrice());
        adPack.setName(dto.getName());
        adPack.setType(EAdType.valueOf(dto.getType().toUpperCase()));
        adPack.setUnit(EAdUnit.valueOf(dto.getUnit().toUpperCase()));
        adPack.setDescription(dto.getDescription());
        adPack.setDiscount(dto.getDiscount());
        adPack.setSpecialFeatures(dto.getSpecialFeatures());
    }
}
