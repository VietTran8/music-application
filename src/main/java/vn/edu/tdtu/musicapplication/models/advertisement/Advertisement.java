package vn.edu.tdtu.musicapplication.models.advertisement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EPaymentMethod;
import vn.edu.tdtu.musicapplication.models.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Boolean active;
    private LocalDateTime expirationDate;
    private LocalDateTime boughtDate;
    private String imageUrl;
    private EPaymentMethod paymentMethod;
    private BigDecimal amount;
    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "packageId")
    private AdvertisementPackage aPackage;
    @OneToOne
    @JoinColumn(name = "contactInfoId")
    private ContactInfo contactInfo;
    @OneToOne
    @JoinColumn(name = "enterpriseInfoId")
    private EnterpriseInfo enterpriseInfo;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
