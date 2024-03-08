package vn.edu.tdtu.musicapplication.models.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.utils.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullName;
    //INPUT: yyyy-MM-dd
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime birthDay;
    private String position;
    private String phoneNumber;
    private String email;
    private String legalDocId;
    private String issuedBy;
    //INPUT: yyyy-MM-dd
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dated;
    private String address;
}
