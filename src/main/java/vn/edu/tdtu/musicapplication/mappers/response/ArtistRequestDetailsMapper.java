package vn.edu.tdtu.musicapplication.mappers.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.response.ArtistInfoDetails;
import vn.edu.tdtu.musicapplication.dtos.response.ArtistRequestDetails;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedGenre;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedUser;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArtistRequestDetailsMapper implements Mapper<ArtistRequest, ArtistRequestDetails> {
    private final MinimizedGenreMapper minimizedGenreMapper;
    @Override
    public ArtistRequest mapToObject(ArtistRequestDetails dto) {
        return null;
    }

    @Override
    public ArtistRequestDetails mapToDto(ArtistRequest object) {
        ArtistRequestDetails requestDetails = new ArtistRequestDetails();

        ArtistInfoDetails infoDetails = new ArtistInfoDetails();
        infoDetails.setId(object.getArtistInfo().getId());
        infoDetails.setPersonalInfo(object.getArtistInfo().getPersonalInfo());
        infoDetails.setArtistName(object.getArtistInfo().getArtistName());
        infoDetails.setImage(object.getArtistInfo().getImage());

        List<MinimizedGenre> minimizedGenres = object.getArtistInfo().getGenres().stream().map(
                minimizedGenreMapper::mapToDto
        ).toList();

        infoDetails.setGenres(minimizedGenres);

        MinimizedUser minimizedUser = new MinimizedUser();
        minimizedUser.setId(object.getUser().getId());
        minimizedUser.setEmail(object.getUser().getEmail());
        minimizedUser.setAvatar(object.getUser().getAvatar());
        minimizedUser.setUsername(object.getUser().getUsername());

        requestDetails.setRequestedDate(object.getRequestedDate());
        requestDetails.setId(object.getId());
        requestDetails.setActive(object.getActive());
        requestDetails.setOwnerships(object.getOwnerships());
        requestDetails.setWorks(object.getWorks());
        requestDetails.setLegalDocuments(object.getLegalDocuments());
        requestDetails.setStatus(object.getStatus().name());
        requestDetails.setArtistInfo(infoDetails);
        requestDetails.setUser(minimizedUser);

        return requestDetails;
    }
}
