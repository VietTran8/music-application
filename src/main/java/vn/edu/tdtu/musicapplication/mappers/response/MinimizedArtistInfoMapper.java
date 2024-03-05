package vn.edu.tdtu.musicapplication.mappers.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.response.*;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Album;
import vn.edu.tdtu.musicapplication.models.Genre;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MinimizedArtistInfoMapper implements Mapper<ArtistInfo, MinimizedArtistInfo> {
    private final MinimizedAlbumMapper minimizedAlbumMapper;
    private final MinimizedSongMapper minimizedSongMapper;
    private final MinimizedGenreMapper minimizedGenreMapper;

    @Override
    public ArtistInfo mapToObject(MinimizedArtistInfo dto) {
        return null;
    }

    @Override
    public MinimizedArtistInfo mapToDto(ArtistInfo object) {
        MinimizedArtistInfo artistInfo = new MinimizedArtistInfo();
        artistInfo.setId(object.getId());
        artistInfo.setImage(object.getImage());
        artistInfo.setArtistName(object.getArtistName());

        return artistInfo;
    }

    public ArtistDetails mapToArtistDetails(ArtistInfo object) {
        ArtistDetails artistInfo = new ArtistDetails();
        artistInfo.setId(object.getId());
        artistInfo.setArtistName(object.getArtistName());
        artistInfo.setImage(object.getImage());
        artistInfo.setPersonalInfo(object.getPersonalInfo());

        List<Song> primarySongs = object.getSongs();
        List<Album> primaryAlbums = object.getAlbums();
        List<Genre> primaryGenres = object.getGenres();

        if(primarySongs != null){
            List<MinimizedSong> songs = primarySongs
                    .stream()
                    .filter(Song::getActive)
                    .map(minimizedSongMapper::mapToDto)
                    .toList();
            artistInfo.setSongs(songs);
        }

        if(primaryAlbums != null){
            List<MinimizedAlbum> albums = primaryAlbums
                    .stream()
                    .filter(Album::getActive)
                    .map(minimizedAlbumMapper::mapToDto)
                    .toList();
            artistInfo.setAlbums(albums);
        }

        if(primaryGenres != null){
            List<MinimizedGenre> genres = primaryGenres
                    .stream()
                    .filter(Genre::getActive)
                    .map(minimizedGenreMapper::mapToDto)
                    .toList();
            artistInfo.setGenres(genres);
        }

        return artistInfo;
    }
}
