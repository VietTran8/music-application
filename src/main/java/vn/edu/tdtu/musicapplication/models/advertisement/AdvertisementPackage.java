package vn.edu.tdtu.musicapplication.models.advertisement;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Boolean active;
    private String name;
    private String specialFeatures;
    @Enumerated(EnumType.STRING)
    private EAdUnit unit;
    @Enumerated(EnumType.STRING)
    private EAdType type;
    @OneToMany(mappedBy = "aPackage")
    @JsonIgnore
    private List<Advertisement> advertisements;

    public String getStringType(){
        switch (this.getType()){
            case TYPE_PREMIUM -> {
                return "Cao cấp";
            }
            case TYPE_NORMAL -> {
                return "Phổ thông";
            }
            default -> {
                return "Trung bình";
            }
        }
    }

    public String getDurationString(){
        String duration =  this.getUnit().description.split("/")[1];
        if(duration.contains("th"))
            duration = "1 " + duration;
        return duration;
    }
}
