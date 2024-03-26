package vn.edu.tdtu.musicapplication.mappers.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.AddPlaylistRequest;
import vn.edu.tdtu.musicapplication.dtos.request.RenamePlaylistRequest;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Playlist;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.service.SongService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddPlaylistRequestMapper implements Mapper<Playlist, AddPlaylistRequest> {
    private final SongService songService;

    @Override
    public Playlist mapToObject(AddPlaylistRequest dto) {
        Playlist playlist = new Playlist();
        playlist.setActive(true);
        playlist.setTitle(dto.getTitle());
        playlist.setCreatedDate(LocalDateTime.now());

        List<Song> songs = songService.findAllByIds(dto.getSongIds());
        playlist.setSongs(songs);

        return playlist;
    }

    @Override
    public AddPlaylistRequest mapToDto(Playlist object) {
        return null;
    }

    public void bindFromDto(Playlist playlist, RenamePlaylistRequest dto) {
        playlist.setActive(true);
        playlist.setTitle(dto.getTitle());
        playlist.setCreatedDate(LocalDateTime.now());
    }
}
