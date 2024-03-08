package vn.edu.tdtu.musicapplication.dtos.request.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAdPackRequest {
    private BigDecimal price;
    private String name;
    private Integer discount; //Don vi phan tram
    private String description;
    private String specialFeatures;
    private String unit; //PER_MONTH - PER_7_DAYS - PER_3_DAYS
    private String type; //TYPE_PREMIUM - TYPE_NORMAL - TYPE_MEDIUM
}
