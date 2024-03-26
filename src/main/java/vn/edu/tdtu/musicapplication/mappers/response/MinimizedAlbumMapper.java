package vn.edu.tdtu.musicapplication.mappers.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.response.AlbumDetails;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedAlbum;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedArtistInfo;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedSong;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Album;
import vn.edu.tdtu.musicapplication.models.Song;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MinimizedAlbumMapper implements Mapper<Album, MinimizedAlbum> {
    @Override
    public Album mapToObject(MinimizedAlbum dto) {
        return null;
    }

    @Override
    public MinimizedAlbum mapToDto(Album object) {
        MinimizedAlbum minimizedAlbum = new MinimizedAlbum();

        minimizedAlbum.setId(object.getId());
        minimizedAlbum.setTitle(object.getTitle());
        minimizedAlbum.setDescription(object.getDescription());
        minimizedAlbum.setImageUrl(object.getImageUrl());
        minimizedAlbum.setReleasedDate(object.getReleasedDate());
        minimizedAlbum.setNoOfSongs(object.getSongs().size());
        minimizedAlbum.setArtist(object.getArtistInfo().getArtistName());

        return minimizedAlbum;
    }

    public AlbumDetails mapToAlbumDetails(Album object){
        AlbumDetails minimizedAlbum = new AlbumDetails();

        minimizedAlbum.setId(object.getId());
        minimizedAlbum.setTitle(object.getTitle());
        minimizedAlbum.setDescription(object.getDescription());
        minimizedAlbum.setImageUrl(object.getImageUrl());
        minimizedAlbum.setReleasedDate(object.getReleasedDate());
        if(object.getArtistInfo().getActive()){
            MinimizedArtistInfo artistInfo = new MinimizedArtistInfo();
            artistInfo.setId(object.getId());
            artistInfo.setArtistName(object.getArtistInfo().getArtistName());

            minimizedAlbum.setArtist(artistInfo);
        }


        List<MinimizedSong> songs = object.getSongs()
                .stream()
                .filter(Song::getActive)
                .map(song -> {
                    MinimizedSongMapper minimizedSongMapper = new MinimizedSongMapper();
                    return minimizedSongMapper.mapToDto(song);
                })
                .toList();

        minimizedAlbum.setSongs(songs);

        return minimizedAlbum;
    }
}