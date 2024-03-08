package vn.edu.tdtu.musicapplication.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EDocumentType;
import vn.edu.tdtu.musicapplication.utils.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLegalDocRequest {
    private String legalDocId;
    private String issuedBy;
    //yyyy-MM-dd
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime dated;
    private String address;
}
