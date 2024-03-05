package vn.edu.tdtu.musicapplication.mappers.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.AddAlbumRequest;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Album;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.repository.SongRepository;
import vn.edu.tdtu.musicapplication.service.AlbumService;
import vn.edu.tdtu.musicapplication.service.ArtistService;
import vn.edu.tdtu.musicapplication.service.SongService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddAlbumRequestMapper implements Mapper<Album, AddAlbumRequest> {
    private final ArtistService artistService;
    private final SongRepository songRepository;
    @Override
    public Album mapToObject(AddAlbumRequest dto) {
        Album album = new Album();
        album.setActive(true);
        album.setDescription(dto.getDescription());
        album.setTitle(dto.getTitle());
        album.setArtistInfo(artistService.findById(dto.getArtistId()));
        album.setReleasedDate(dto.getReleasedDate());
        album.setImageUrl(dto.getImageUrl());
        if(dto.getSongIds() != null){
            List<Song> songs = new ArrayList<>();
            songRepository.findAll().forEach(song -> {
                if(dto.getSongIds().contains(song.getId()) && song.getActive()){
                    song.setAlbum(album);
                    songs.add(song);
                }
            });
            album.setSongs(songs);
        }

        return album;
    }

    @Override
    public AddAlbumRequest mapToDto(Album object) {
        return null;
    }

    public void bindFromDto(Album album, AddAlbumRequest dto){
        album.setDescription(dto.getDescription());
        album.setTitle(dto.getTitle());
        album.setArtistInfo(artistService.findById(dto.getArtistId()));
        album.setReleasedDate(dto.getReleasedDate());
        album.setImageUrl(dto.getImageUrl());
    }
}
