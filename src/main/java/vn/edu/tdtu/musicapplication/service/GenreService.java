package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddGenreRequest;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedGenre;
import vn.edu.tdtu.musicapplication.dtos.response.GenreDetails;
import vn.edu.tdtu.musicapplication.mappers.request.AddGenreRequestMapper;
import vn.edu.tdtu.musicapplication.mappers.response.MinimizedGenreMapper;
import vn.edu.tdtu.musicapplication.models.Genre;
import vn.edu.tdtu.musicapplication.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final AddGenreRequestMapper addGenreRequestMapper;
    private final MinimizedGenreMapper minimizedGenreMapper;
    public Genre findById(Long id){
        return genreRepository.findById(id).orElse(null);
    }

    public List<Genre> findAllByIds(List<Long> ids){
        List<Genre> result = new ArrayList<>();
        genreRepository.findAll().forEach(genre -> {
            if(ids.contains(genre.getId()) && genre.getActive()){
                result.add(genre);
            }
        });
        return result;
    }

    public Genre save(Genre genre){
        return genreRepository.save(genre);
    }

    public BaseResponse<?> saveGenre(AddGenreRequest request){
        Genre genre = addGenreRequestMapper.mapToObject(request);
        MinimizedGenre minimizedGenre = null;

        BaseResponse<MinimizedGenre> response = new BaseResponse<>();
        response.setStatus(false);
        response.setMessage("This genre has already existed");
        response.setData(minimizedGenre);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);

        if(!genreRepository.existsByNameAndActive(genre.getName(), true)){
            minimizedGenre = minimizedGenreMapper.mapToDto(genreRepository.save(genre));
            response.setStatus(true);
            response.setMessage("Genre added successfully");
            response.setData(minimizedGenre);
            response.setCode(HttpServletResponse.SC_CREATED);
        }
        
        return response;
    }

    public BaseResponse<?> updateGenre(Long id, AddGenreRequest request){
        Genre genre = findById(id);
        BaseResponse<MinimizedGenre> response = new BaseResponse<>();
        response.setMessage("Genre not found with id: " + id);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setData(null);
        response.setStatus(false);

        if(genre != null && genre.getActive()){
            if(!genreRepository.existsByNameAndActive(request.getName(), true)) {
                addGenreRequestMapper.bindFromDto(genre, request);
                genreRepository.save(genre);

                response.setMessage("Genre updated successfully");
                response.setCode(HttpServletResponse.SC_ACCEPTED);
                response.setData(minimizedGenreMapper.mapToDto(genre));
                response.setStatus(true);
            }else{
                response.setStatus(false);
                response.setMessage("This genre has already existed");
                response.setData(null);
                response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
        }

        return response;
    }

    public BaseResponse<?> deleteGenre(Long id){
        Genre foundGenre = findById(id);
        BaseResponse<MinimizedGenre> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setStatus(false);
        response.setMessage("Genre not found with id: " + id);

        if(foundGenre != null && foundGenre.getActive()){
            foundGenre.setActive(false);
            genreRepository.save(foundGenre);

            response.setData(null);
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setStatus(true);
            response.setMessage("Genre deleted successfully");
        }

        return response;
    }

    public BaseResponse<?> getAllGenres(){
        List<Genre> genres = genreRepository.findByActive(true);
        List<MinimizedGenre> minimizedGenres = new ArrayList<>();
        genres.forEach(genre -> {
            minimizedGenres.add(minimizedGenreMapper.mapToDto(genre));
        });

        BaseResponse<List<MinimizedGenre>> response = new BaseResponse<>();
        response.setMessage("Genres fetched successfully");
        response.setData(minimizedGenres);
        response.setStatus(true);
        response.setCode(HttpServletResponse.SC_OK);

        return response;
    }

    public BaseResponse<?> getGenreById(Long id){
        Genre genre = genreRepository.findById(id).orElse(null);
        BaseResponse<GenreDetails> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(false);
        response.setMessage("Genre not found with id: " + id);

        if(genre != null && genre.getActive()) {
            GenreDetails minimizedGenre = minimizedGenreMapper.mapToGenreDetails(genre);

            response.setData(minimizedGenre);
            response.setCode(HttpServletResponse.SC_OK);
            response.setStatus(true);
            response.setMessage("Genre fetched successfully");
        }

        return response;
    }
}