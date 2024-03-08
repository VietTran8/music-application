package vn.edu.tdtu.musicapplication.mappers.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.AddSongRequest;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.service.AlbumService;
import vn.edu.tdtu.musicapplication.service.ArtistService;
import vn.edu.tdtu.musicapplication.service.GenreService;

@Component
@RequiredArgsConstructor
public class AddSongRequestMapper implements Mapper<Song, AddSongRequest> {
    private final AlbumService albumService;
    private final GenreService genreService;
    private final ArtistService artistService;
    @Override
    public Song mapToObject(AddSongRequest dto) {
        Song song = new Song();
        song.setActive(true);
        song.setArtistInfoList(artistService.findAllByIds(dto.getArtistIds()));
        song.setAlbum(albumService.findById(dto.getAlbumId()));
        song.setGenre(genreService.findById(dto.getGenreId()));
        song.setName(dto.getName());
        song.setLyrics(dto.getLyrics());
        song.setAudioUrl(dto.getAudioUrl());
        song.setImageUrl(dto.getImageUrl());
        song.setIsPremium(dto.getIsPremium());
        song.setReleaseDate(dto.getReleaseDate());
        song.setAuthor(dto.getAuthor());

        return song;
    }

    @Override
    public AddSongRequest mapToDto(Song object) {
        return null;
    }

    public void bindFromDto(Song song, AddSongRequest dto){
        song.setActive(true);
        song.setArtistInfoList(artistService.findAllByIds(dto.getArtistIds()));
        song.setAlbum(albumService.findById(dto.getAlbumId()));
        song.setGenre(genreService.findById(dto.getGenreId()));
        song.setName(dto.getName());
        song.setLyrics(dto.getLyrics());
        song.setAudioUrl(dto.getAudioUrl());
        song.setImageUrl(dto.getImageUrl());
        song.setIsPremium(dto.getIsPremium());
        song.setReleaseDate(dto.getReleaseDate());
        song.setAuthor(dto.getAuthor());
    }
}
