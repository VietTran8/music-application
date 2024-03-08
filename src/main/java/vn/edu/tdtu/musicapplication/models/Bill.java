package vn.edu.tdtu.musicapplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EProductType;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;
import vn.edu.tdtu.musicapplication.models.advertisement.ContactInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDateTime createdDate;
    private EProductType productType;
    private String paymentMethod;
    private String transactionId;
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userPackId")
    private UserPackageBought packageProduct;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adId")
    private Advertisement adProduct;
}
