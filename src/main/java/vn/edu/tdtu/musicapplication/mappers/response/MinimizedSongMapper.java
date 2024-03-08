package vn.edu.tdtu.musicapplication.mappers.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedAlbum;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedArtistInfo;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedGenre;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedSong;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MinimizedSongMapper implements Mapper<Song, MinimizedSong> {

    @Override
    public Song mapToObject(MinimizedSong dto) {
        return null;
    }

    @Override
    public MinimizedSong mapToDto(Song object) {
        MinimizedSong minimizedSong = new MinimizedSong();
        MinimizedAlbum minimizedAlbum = null;

        //Album settings
        if(object.getAlbum() != null && object.getAlbum().getActive()){
            minimizedAlbum = new MinimizedAlbum();
            minimizedAlbum.setId(object.getAlbum().getId());
            minimizedAlbum.setTitle(object.getAlbum().getTitle());
            minimizedAlbum.setDescription(object.getAlbum().getDescription());
            minimizedAlbum.setImageUrl(object.getAlbum().getImageUrl());
            minimizedAlbum.setReleasedDate(object.getAlbum().getReleasedDate());
        }

        //Genre settings
        MinimizedGenre minimizedGenre = null;
        if(object.getGenre() != null && object.getGenre().getActive()){
            minimizedGenre = new MinimizedGenre();
            minimizedGenre.setId(object.getGenre().getId());
            minimizedGenre.setDescription((object.getGenre().getDescription()));
            minimizedGenre.setName(object.getGenre().getName());
        }
        List<MinimizedArtistInfo> minimizedArtistInfos = new ArrayList<>();

        //Artists settings
        object.getArtistInfoList().forEach(artist -> {
            if(artist.getActive()){
                MinimizedArtistInfo artistInfo = new MinimizedArtistInfo();
                artistInfo.setId(artist.getId());
                artistInfo.setArtistName(artist.getArtistName());
                artistInfo.setImage(artist.getImage());

                minimizedArtistInfos.add(artistInfo);
            }
        });

        //Song settings
        minimizedSong.setId(object.getId());
        minimizedSong.setName(object.getName());
        minimizedSong.setImageUrl(object.getImageUrl());
        minimizedSong.setAlbum(minimizedAlbum);
        minimizedSong.setGenre(minimizedGenre);
        minimizedSong.setLyrics(object.getLyrics());
        minimizedSong.setAudioUrl(object.getAudioUrl());
        minimizedSong.setIsPremium(object.getIsPremium());
        minimizedSong.setReleaseDate(object.getReleaseDate());
        minimizedSong.setArtists(minimizedArtistInfos);
        minimizedSong.setAuthor(object.getAuthor());

        List<User> users = object.getUsers();
        if(users != null){
            minimizedSong.setFavourites(users.size());
        }

        return minimizedSong;
    }
}