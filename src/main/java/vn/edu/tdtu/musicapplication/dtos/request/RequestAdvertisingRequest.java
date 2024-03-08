package vn.edu.tdtu.musicapplication.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.models.advertisement.ContactInfo;
import vn.edu.tdtu.musicapplication.models.advertisement.EnterpriseInfo;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAdvertisingRequest {
    private Long packageId;
    private String productName;
    private String imageUrl;
    private BigDecimal amount;
    private ContactInfo contactInfo;
    private EnterpriseInfo enterpriseInfo;
}
