package vn.edu.tdtu.musicapplication.mappers.request;

import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddPackageRequest;
import vn.edu.tdtu.musicapplication.enums.EPackageType;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Package;

@Component
public class AddPackageRequestMapper implements Mapper<Package, AddPackageRequest> {
    @Override
    public Package mapToObject(AddPackageRequest dto) {
        Package mPackage = new Package();
        mPackage.setActive(true);
        mPackage.setName(dto.getName());
        mPackage.setDuration(dto.getDuration());
        mPackage.setDescription(dto.getDescription());
        mPackage.setDiscount(dto.getDiscount());
        mPackage.setPrice(dto.getPrice());
        mPackage.setSpecialFeatures(dto.getSpecialFeatures());
        mPackage.setType(EPackageType.valueOf(dto.getType().toUpperCase()));

        return mPackage;
    }

    @Override
    public AddPackageRequest mapToDto(Package object) {
        return null;
    }

    public void bindFromDto(Package mPackage, AddPackageRequest dto) {
        mPackage.setActive(true);
        mPackage.setName(dto.getName());
        mPackage.setDuration(dto.getDuration());
        mPackage.setDescription(dto.getDescription());
        mPackage.setDiscount(dto.getDiscount());
        mPackage.setPrice(dto.getPrice());
        mPackage.setSpecialFeatures(dto.getSpecialFeatures());
        mPackage.setType(EPackageType.valueOf(dto.getType().toUpperCase()));
    }
}