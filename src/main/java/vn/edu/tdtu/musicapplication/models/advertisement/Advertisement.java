package vn.edu.tdtu.musicapplication.models.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EAdStatus;
import vn.edu.tdtu.musicapplication.enums.EPaymentMethod;
import vn.edu.tdtu.musicapplication.models.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime createDate;
    private String imageUrl;
    private String productName;
    private EPaymentMethod paymentMethod;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private EAdStatus status;
    @ManyToOne
    @JoinColumn(name = "packageId")
    private AdvertisementPackage aPackage;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contactInfoId")
    private ContactInfo contactInfo;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "enterpriseInfoId")
    private EnterpriseInfo enterpriseInfo;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "advertisement")
    private List<AdStatistics> adStatistics;

    public boolean isValid(){
        return this.getActive()
                && this.getExpirationDate().isAfter(LocalDateTime.now())
                && this.getStatus() == EAdStatus.APPROVED;
    }

    public String getStringStatus(){
        switch (this.getStatus()){
            case APPROVED -> {
                return "Đã phê duyệt";
            }
            case NOT_APPROVED -> {
                return "Từ chối";
            }
            default -> {
                return "Đang chờ";
            }
        }
    }
}
