package vn.edu.tdtu.musicapplication.mappers.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedGenre;
import vn.edu.tdtu.musicapplication.dtos.response.GenreDetails;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedSong;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Genre;
import vn.edu.tdtu.musicapplication.models.Song;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MinimizedGenreMapper implements Mapper<Genre, MinimizedGenre> {
    private final MinimizedSongMapper minimizedSongMapper;


    @Override
    public Genre mapToObject(MinimizedGenre dto) {
        return null;
    }

    @Override
    public MinimizedGenre mapToDto(Genre object) {
        MinimizedGenre minimizedGenre = new MinimizedGenre();
        minimizedGenre.setId(object.getId());
        minimizedGenre.setDescription((object.getDescription()));
        minimizedGenre.setName(object.getName());

        return minimizedGenre;
    }

    public GenreDetails mapToGenreDetails(Genre object) {
        GenreDetails minimizedGenre = new GenreDetails();
        minimizedGenre.setId(object.getId());
        minimizedGenre.setDescription((object.getDescription()));
        minimizedGenre.setName(object.getName());

        List<MinimizedSong> minimizedSongs = object.getSongs()
                .stream()
                .filter(Song::getActive)
                .map(minimizedSongMapper::mapToDto).toList();

        minimizedGenre.setSongs(minimizedSongs);

        return minimizedGenre;
    }
}
