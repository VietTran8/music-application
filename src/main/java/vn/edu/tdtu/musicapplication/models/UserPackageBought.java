package vn.edu.tdtu.musicapplication.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EPaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPackageBought {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime expirationDate;
    private LocalDateTime boughtDate;
    private EPaymentMethod paymentMethod;
    private BigDecimal amount;
    private Boolean status;
    private Boolean isLatest;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "packageId")
    private Package mPackage;
    @OneToMany(mappedBy = "packageProduct")
    private List<Bill> bills;

    public boolean isNotExpired(){
        return this.getStatus() && this.getExpirationDate().isAfter(LocalDateTime.now());
    }
}