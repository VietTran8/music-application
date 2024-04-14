package vn.edu.tdtu.musicapplication.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.enums.EArtistRequestStatus;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.models.artist_request.LegalDocument;
import vn.edu.tdtu.musicapplication.models.artist_request.OwnerShip;
import vn.edu.tdtu.musicapplication.models.artist_request.Work;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistRequestDetails {
    private long id;
    private String status;
    private Boolean active;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime requestedDate;
    private List<Work> works;
    private List<OwnerShip> ownerships;
    private List<LegalDocument> legalDocuments;
    private MinimizedUser user;
    private ArtistInfoDetails artistInfo;

    public String getStringStatus(){
        switch (this.getStatus()){
            case "APPROVED" -> {
                return "Đã phê duyệt";
            }
            case "NOT_APPROVED" -> {
                return "Từ chối";
            }
            default -> {
                return "Đang chờ";
            }
        }
    }
}
