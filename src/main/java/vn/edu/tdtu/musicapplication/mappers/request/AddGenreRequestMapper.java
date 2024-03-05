package vn.edu.tdtu.musicapplication.mappers.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddGenreRequest;
import vn.edu.tdtu.musicapplication.mappers.Mapper;
import vn.edu.tdtu.musicapplication.models.Genre;

@Component
public class AddGenreRequestMapper implements Mapper<Genre, AddGenreRequest> {
    @Override
    public Genre mapToObject(AddGenreRequest dto) {
        Genre genre = new Genre();
        genre.setActive(true);
        genre.setName(dto.getName());
        genre.setDescription(dto.getDescription());

        return genre;
    }

    @Override
    public AddGenreRequest mapToDto(Genre object) {
        return null;
    }

    public void bindFromDto(Genre genre, AddGenreRequest dto){
        genre.setName(dto.getName());
        genre.setDescription(dto.getDescription());
    }
}
