package vn.edu.tdtu.musicapplication.dtos.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.utils.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPersonalInfoRequest {
    private String fullName;
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime birthDate;
    private String nation;
    private String phoneNumber;
    private String bankAccountName;
    private String bankAccountNumber;
    private List<AddSocialNetRequest> socialNets;
}
