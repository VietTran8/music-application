package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddSongRequest;
import vn.edu.tdtu.musicapplication.dtos.request.UpdateSongImgReq;
import vn.edu.tdtu.musicapplication.dtos.request.UpdateSongLyricsReq;
import vn.edu.tdtu.musicapplication.dtos.request.UpdateSongNameReq;
import vn.edu.tdtu.musicapplication.dtos.response.FavouriteResponse;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedSong;
import vn.edu.tdtu.musicapplication.enums.ERole;
import vn.edu.tdtu.musicapplication.enums.EUploadFolder;
import vn.edu.tdtu.musicapplication.mappers.request.AddSongRequestMapper;
import vn.edu.tdtu.musicapplication.mappers.response.MinimizedSongMapper;
import vn.edu.tdtu.musicapplication.models.Genre;
import vn.edu.tdtu.musicapplication.models.Role;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.repository.SongRepository;
import vn.edu.tdtu.musicapplication.service.cloudinary.FileServiceImpl;
import vn.edu.tdtu.musicapplication.utils.PrincipalUtils;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongService {
    private final SongRepository songRepository;
    private final AddSongRequestMapper addSongRequestMapper;
    private final MinimizedSongMapper minimizedSongMapper;
    private final UserService userService;
    private final PrincipalUtils principalUtils;
    private final FileServiceImpl fileService;

    public MinimizedSong toMinimized(Song song){
        return minimizedSongMapper.mapToDto(song);
    }
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

    public Long countTotalSongs(){
        return songRepository.countAllByActive(true);
    }

    public BaseResponse<?> uploadSongImage(Long songId, MultipartFile file){
        BaseResponse<Map<String, Object>> response = new BaseResponse<>();
        Song foundSong = findById(songId);
        try{
            String url = fileService.uploadFile(file, EUploadFolder.FOLDER_IMG);

            foundSong.setImageUrl(url);

            songRepository.save(foundSong);
            Map<String, Object> data = new HashMap<>();
            data.put("url", url);

            response.setMessage("Hình ảnh đã được cập nhật!");
            response.setData(data);
            response.setStatus(true);
            response.setCode(HttpServletResponse.SC_OK);

            return response;
        }catch (IOException ex){
            log.error(ex.getMessage());
            response.setMessage("Something went wrong: " + ex.getMessage());
        }
        response.setData(null);
        response.setStatus(false);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);

        return response;
    }

    public Map<String, List<MinimizedSong>> findAllSongsGroupByGenre(){
        List<Song> songs = songRepository.findByActive(true);
        return songs.stream().filter(song -> song.getGenre() != null)
                .map(minimizedSongMapper::mapToDto)
                .collect(Collectors.groupingBy(song -> song.getGenre().getName()));
    }

    public Map<String, List<MinimizedSong>> findAllSongsByGenre(Genre genre){
        List<Song> songs = songRepository.findByActiveAndGenre(true, genre);
        return songs.stream().filter(song -> song.getGenre() != null)
                .map(minimizedSongMapper::mapToDto)
                .collect(Collectors.groupingBy(song -> song.getGenre().getName()));
    }

    public BaseResponse<?> favourite(Principal principal, Long songId){
        BaseResponse<FavouriteResponse> response = new BaseResponse<>();
        response.setMessage("Bạn chưa đăng nhập");
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setData(null);
        response.setStatus(false);

        if(principal != null){
            Song foundSong = findById(songId);
            User foundUser = principalUtils.loadUserFromPrincipal(principal);
            response.setMessage("Không tìm thấy nhạc với id: " + songId);
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

                response.setMessage("Đã thích bài hát!");
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

    public BaseResponse<?> updateSongName(UpdateSongNameReq request){
        Long songId = request.getSongId();
        Song foundSong = songRepository.findById(songId).orElse(null);
        BaseResponse<MinimizedSong> response = new BaseResponse<>();
        response.setMessage("Song not found with id: " + songId);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setData(null);
        response.setStatus(false);

        if(foundSong != null && foundSong.getActive()){
            foundSong.setName(request.getNewSongName());
            Song updatedSong = songRepository.save(foundSong);

            response.setMessage("Song updated successfully");
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setData(minimizedSongMapper.mapToDto(updatedSong));
            response.setStatus(true);
        }

        return response;
    }

    public BaseResponse<?> updateSongLyrics(UpdateSongLyricsReq request){
        Long songId = request.getSongId();
        Song foundSong = songRepository.findById(songId).orElse(null);
        BaseResponse<MinimizedSong> response = new BaseResponse<>();
        response.setMessage("Song not found with id: " + songId);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setData(null);
        response.setStatus(false);

        if(foundSong != null && foundSong.getActive()){
            foundSong.setLyrics(request.getNewSongLyrics());
            Song updatedSong = songRepository.save(foundSong);

            response.setMessage("Song updated successfully");
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setData(minimizedSongMapper.mapToDto(updatedSong));
            response.setStatus(true);
        }

        return response;
    }

    public BaseResponse<?> addPlaySong(Long songId){
        Song foundSong = findById(songId);
        BaseResponse<MinimizedSong> response = new BaseResponse<>();
        if(foundSong != null && foundSong.getActive()){
            foundSong.setPlays(foundSong.getPlays() + 1);
            save(foundSong);
            response.setCode(HttpServletResponse.SC_OK);
            response.setStatus(true);
            response.setData(minimizedSongMapper.mapToDto(foundSong));
            response.setMessage("Plays added successfully!");

            return response;
        }
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(false);
        response.setData(null);
        response.setMessage("Song not found with id: " + songId);

        return response;
    }

    public BaseResponse<List<MinimizedSong>> getAllSongs(Principal principal, int page, int limit){
        List<MinimizedSong> minimizedSongs = new ArrayList<>();
        if(principal != null){
            User foundUser = principalUtils.loadUserFromPrincipal(principal);
            if(foundUser.getIsPremium()){
                songRepository.findByActive(true, PageRequest.of(page - 1, limit)).forEach(song -> {
                    minimizedSongs.add(minimizedSongMapper.mapToDto(song));
                });
            }else{
                songRepository.findByActiveAndIsPremium(true, false, PageRequest.of(page - 1, limit)).forEach(song -> {
                    minimizedSongs.add(minimizedSongMapper.mapToDto(song));
                });
            }
        }else{
            songRepository.findByActiveAndIsPremium(true, false, PageRequest.of(page - 1, limit)).forEach(song -> {
                minimizedSongs.add(minimizedSongMapper.mapToDto(song));
            });
        }

        BaseResponse<List<MinimizedSong>> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setStatus(true);
        response.setMessage("Songs fetched successfully");
        response.setData(minimizedSongs);

        return response;
    }

    public List<MinimizedSong> getAllSongsForView(int page, int limit){
        List<MinimizedSong> minimizedSongs = new ArrayList<>();
        songRepository.findByActive(true, PageRequest.of(page - 1, limit))
                .stream()
                .forEach(song -> {
                    minimizedSongs.add(minimizedSongMapper.mapToDto(song));
                });

        return minimizedSongs;
    }

    public Map<String, Object> getAllSongsForAdminView(int page, int limit){
        List<MinimizedSong> minimizedSongs = new ArrayList<>();

        Page<Song> songPage = songRepository.findByActive(true, PageRequest.of(page - 1, limit));
        songPage.forEach(song -> {
            minimizedSongs.add(minimizedSongMapper.mapToDto(song));
        });

        Map<String, Object> result = new HashMap<>();
        result.put("totalPages", songPage.getTotalPages());
        result.put("songs", minimizedSongs);

        return result;
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
        List<Song> foundSongs = songRepository.findBySongNameOrArtistName(name);
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
