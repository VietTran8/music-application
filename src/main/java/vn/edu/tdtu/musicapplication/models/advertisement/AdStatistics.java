package vn.edu.tdtu.musicapplication.models.advertisement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate date;
    private long totalApproach;
    @ManyToOne
    @JoinColumn(name = "adId")
    @JsonIgnore
    private Advertisement advertisement;
}