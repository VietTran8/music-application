package vn.edu.tdtu.musicapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EPackageType;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Boolean active;
    private Integer duration;
    private String description;
    private String name;
    private BigDecimal price;
    private Integer discount; //Don vi phan tram
    private String specialFeatures;
    private EPackageType type;
    @OneToMany(mappedBy = "mPackage")
    @JsonIgnore
    private List<UserPackageBought> boughtPackages;
}