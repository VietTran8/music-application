package vn.edu.tdtu.musicapplication.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.musicapplication.models.artist_request.OwnerShip;
import vn.edu.tdtu.musicapplication.models.artist_request.Work;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitArtistRequest {
    private String artistName;
    private List<Long> genreIds;
    private AddPersonalInfoRequest personalInfo;
    private List<AddLegalDocRequest> legalDocs;
    private List<Work> works;
    private List<OwnerShip> ownerShips;
}