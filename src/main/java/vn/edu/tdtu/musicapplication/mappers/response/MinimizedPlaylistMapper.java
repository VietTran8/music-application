package vn.edu.tdtu.musicapplication.mappers.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedPlaylist;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedSong;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Playlist;
import vn.edu.tdtu.musicapplication.models.Song;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MinimizedPlaylistMapper implements Mapper<Playlist, MinimizedPlaylist> {
    private final MinimizedSongMapper minimizedSongMapper;

    @Override
    public Playlist mapToObject(MinimizedPlaylist dto) {
        return null;
    }

    @Override
    public MinimizedPlaylist mapToDto(Playlist object) {
        MinimizedPlaylist minimizedPlaylist = new MinimizedPlaylist();
        minimizedPlaylist.setId(object.getId());
        minimizedPlaylist.setTitle(object.getTitle());
        minimizedPlaylist.setCreatedDate(object.getCreatedDate());
        minimizedPlaylist.setThumbnails(object.getSongs() != null ? object.getSongs().stream().map(Song::getImageUrl).limit(4).toList() : new ArrayList<>());
        minimizedPlaylist.setNoOfThumbs(minimizedPlaylist.getThumbnails().size());

        List<MinimizedSong> songs = object.getSongs()
                .stream()
                .filter(Song::getActive)
                .map(minimizedSongMapper::mapToDto)
                .toList();

        minimizedPlaylist.setSongs(songs);

        return minimizedPlaylist;
    }
}
