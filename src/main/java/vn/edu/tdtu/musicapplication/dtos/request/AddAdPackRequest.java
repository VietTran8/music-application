package vn.edu.tdtu.musicapplication.dtos.request;

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
    private String unit;
    private String type;
}
