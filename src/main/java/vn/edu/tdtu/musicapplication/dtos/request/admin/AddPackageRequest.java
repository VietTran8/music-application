package vn.edu.tdtu.musicapplication.dtos.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPackageRequest {
    private Integer duration;
    private String description;
    private String name;
    private BigDecimal price;
    private Integer discount; //Don vi phan tram
    private String specialFeatures;
    private String type;
}
