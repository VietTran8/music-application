package vn.edu.tdtu.musicapplication.mappers.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddArtistRequest;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.service.GenreService;

@Component
@RequiredArgsConstructor
public class AdminAddArtistMapper implements Mapper<ArtistInfo, AddArtistRequest> {
    private final GenreService genreService;
    @Override
    public ArtistInfo mapToObject(AddArtistRequest dto) {
        ArtistInfo artistInfo = new ArtistInfo();
        artistInfo.setArtistName(dto.getArtistName());
        artistInfo.setImage(dto.getImage());
        artistInfo.setGenres(genreService.findAllByIds(dto.getGenreIds()));
        artistInfo.setActive(true);

        return artistInfo;
    }

    @Override
    public AddArtistRequest mapToDto(ArtistInfo object) {
        return null;
    }

    public void bindFromDto(ArtistInfo artistInfo, AddArtistRequest dto){
        artistInfo.setArtistName(dto.getArtistName());
        artistInfo.setGenres(genreService.findAllByIds(dto.getGenreIds()));
        artistInfo.setImage(dto.getImage());
    }
}
