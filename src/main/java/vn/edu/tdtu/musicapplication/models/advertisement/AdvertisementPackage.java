package vn.edu.tdtu.musicapplication.models.advertisement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EAdType;
import vn.edu.tdtu.musicapplication.enums.EAdUnit;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdvertisementPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private BigDecimal price;
    private Integer discount; //Don vi phan tram
    private String description;
    @Enumerated(EnumType.STRING)
    private EAdUnit unit;
    @Enumerated(EnumType.STRING)
    private EAdType type;
    @OneToMany(mappedBy = "aPackage")
    private List<Advertisement> advertisements;
}