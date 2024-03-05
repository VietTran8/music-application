package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddSongRequest;
import vn.edu.tdtu.musicapplication.dtos.response.FavouriteResponse;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedSong;
import vn.edu.tdtu.musicapplication.mappers.request.AddSongRequestMapper;
import vn.edu.tdtu.musicapplication.mappers.response.MinimizedSongMapper;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.repository.SongRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final AddSongRequestMapper addSongRequestMapper;
    private final MinimizedSongMapper minimizedSongMapper;
    private final UserService userService;

    public Song save(Song song){
        return songRepository.save(song);
    }
    public BaseResponse<?> addSong(AddSongRequest addSongRequest){
        Song song = addSongRequestMapper.mapToObject(addSongRequest);
        MinimizedSong minimizedSong = minimizedSongMapper.mapToDto(songRepository.save(song));

        BaseResponse<MinimizedSong> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_CREATED);
        response.setData(minimizedSong);
        response.setMessage("Song added successfully");
        response.setStatus(true);

        return response;
    }

    public BaseResponse<?> favourite(Principal principal, Long songId){
        BaseResponse<FavouriteResponse> response = new BaseResponse<>();
        response.setMessage("You are not authenticated");
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setData(null);
        response.setStatus(false);

        if(principal != null){
            Song foundSong = findById(songId);
            User foundUser = userService.findByEmail(principal.getName());
            response.setMessage("Song not found with id: " + songId);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);
            response.setStatus(false);

            if(foundSong != null && foundSong.getActive()) {
                if(!foundUser.getFavouriteSongs().contains(foundSong))
                    foundUser.getFavouriteSongs().add(foundSong);
                else
                    foundUser.getFavouriteSongs().remove(foundSong);

                userService.saveUser(foundUser);

                FavouriteResponse favourite = new FavouriteResponse();
                favourite.setFavourites(foundSong.getUsers().size());

                response.setMessage("Liked this song: " + songId);
                response.setCode(HttpServletResponse.SC_OK);
                response.setData(favourite);
                response.setStatus(true);
            }
        }

        return response;
    }

    public List<Song> findAllByIds(List<Long> ids){
        List<Song> result = new ArrayList<>();
        songRepository.findAll().forEach(song -> {
            if(ids.contains(song.getId()) && song.getActive()){
                result.add(song);
            }
        });
        return result;
    }

    public Song findById(Long id){
        return songRepository.findById(id).orElse(null);
    }

    public BaseResponse<?> updateSong(Long songId, AddSongRequest requestBody){
        Song foundSong = songRepository.findById(songId).orElse(null);
        BaseResponse<MinimizedSong> response = new BaseResponse<>();
        response.setMessage("Song not found with id: " + songId);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setData(null);
        response.setStatus(false);

        if(foundSong != null && foundSong.getActive()){
            addSongRequestMapper.bindFromDto(foundSong, requestBody);
            Song updatedSong = songRepository.save(foundSong);

            response.setMessage("Song updated successfully");
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setData(minimizedSongMapper.mapToDto(updatedSong));
            response.setStatus(true);
        }

        return response;
    }

    public BaseResponse<?> getAllSongs(int page, int limit){
        List<MinimizedSong> minimizedSongs = new ArrayList<>();
        songRepository.findByActive(true, PageRequest.of(page - 1, limit)).forEach(song -> {
            minimizedSongs.add(minimizedSongMapper.mapToDto(song));
        });

        BaseResponse<List<MinimizedSong>> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setStatus(true);
        response.setMessage("Songs fetched successfully");
        response.setData(minimizedSongs);

        return response;
    }

    public BaseResponse<?> getSongById(Long id){
        Song foundSong = songRepository.findById(id).orElse(null);
        BaseResponse<MinimizedSong> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(false);
        response.setMessage("Song not found with id: " + id);

        if(foundSong != null && foundSong.getActive()) {
            MinimizedSong minimizedSong = minimizedSongMapper.mapToDto(foundSong);

            response.setData(minimizedSong);
            response.setCode(HttpServletResponse.SC_OK);
            response.setStatus(true);
            response.setMessage("Song fetched successfully");
        }

        return response;
    }

    public BaseResponse<?> getSongsByName(String name){
        List<Song> foundSongs = songRepository.findByNameContainingIgnoreCase(name);
        List<MinimizedSong> minimizedSongs = new ArrayList<>();
        BaseResponse<List<MinimizedSong>> response = new BaseResponse<>();

        foundSongs.forEach(song -> {
            if(song.getActive()){
                minimizedSongs.add(minimizedSongMapper.mapToDto(song));
            }
        });

        response.setData(minimizedSongs);
        response.setCode(HttpServletResponse.SC_OK);
        response.setStatus(true);
        response.setMessage("Song fetched successfully");

        return response;
    }

    public BaseResponse<?> deleteSongById(Long id){
        Song foundSong = songRepository.findById(id).orElse(null);
        BaseResponse<MinimizedSong> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setStatus(false);
        response.setMessage("Song not found with id: " + id);

        if(foundSong != null && foundSong.getActive()) {
            foundSong.setActive(false);
            songRepository.save(foundSong);

            response.setData(null);
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setStatus(true);
            response.setMessage("Song deleted successfully");
        }

        return response;
    }
}
