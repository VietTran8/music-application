package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddPlaylistRequest;
import vn.edu.tdtu.musicapplication.dtos.request.AddSongToPlaylistRequest;
import vn.edu.tdtu.musicapplication.dtos.request.DeleteSongFromPlaylistRequest;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedPlaylist;
import vn.edu.tdtu.musicapplication.mappers.request.AddPlaylistRequestMapper;
import vn.edu.tdtu.musicapplication.mappers.response.MinimizedPlaylistMapper;
import vn.edu.tdtu.musicapplication.models.Playlist;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.repository.PlaylistRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final UserService userService;
    private final SongService songService;
    private final PlaylistRepository playlistRepository;
    private final AddPlaylistRequestMapper addPlaylistRequestMapper;
    private final MinimizedPlaylistMapper minimizedPlaylistMapper;

    public Playlist findById(Long id){
        return playlistRepository.findById(id).orElse(null);
    }

    public List<Playlist> findAllByIds(List<Long> ids){
        List<Playlist> result = new ArrayList<>();
        playlistRepository.findAll().forEach(playlist -> {
            if(ids.contains(playlist.getId()) && playlist.getActive()){
                result.add(playlist);
            }
        });
        return result;
    }

    public Playlist save(Playlist playlist){
        return playlistRepository.save(playlist);
    }

    public BaseResponse<?> savePlaylist(Principal principal, AddPlaylistRequest request){
        BaseResponse<MinimizedPlaylist> response = new BaseResponse<>();
        response.setStatus(false);
        response.setMessage("You are not authenticated");
        response.setData(null);
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);

        if(principal != null){
            User foundUser = userService.findByEmail(principal.getName());
            if(foundUser != null && foundUser.getActive()){
                Playlist playlist = addPlaylistRequestMapper.mapToObject(request);
                MinimizedPlaylist minimizedPlaylist = null;

                response.setStatus(false);
                response.setMessage("This playlist has already existed");
                response.setData(null);
                response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);

                if(!playlistRepository.existsByTitleAndActive(playlist.getTitle(), true)){
                    playlist.setUser(foundUser);
                    minimizedPlaylist = minimizedPlaylistMapper.mapToDto(playlistRepository.save(playlist));
                    response.setStatus(true);
                    response.setMessage("Playlist added successfully");
                    response.setData(minimizedPlaylist);
                    response.setCode(HttpServletResponse.SC_CREATED);
                }
            }else{
                response.setStatus(false);
                response.setMessage(String.format("The user %s is not exists", principal.getName()));
                response.setData(null);
                response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
        }

        return response;
    }

    public BaseResponse<?> updatePlaylist(Long id, AddPlaylistRequest request){
        Playlist playlist = findById(id);
        BaseResponse<MinimizedPlaylist> response = new BaseResponse<>();
        response.setMessage("Playlist not found with id: " + id);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setData(null);
        response.setStatus(false);

        if(playlist != null && playlist.getActive()){
            if(!playlistRepository.existsByTitleAndActive(request.getTitle(), true)) {
                addPlaylistRequestMapper.bindFromDto(playlist, request);
                playlistRepository.save(playlist);

                response.setMessage("playlist updated successfully");
                response.setCode(HttpServletResponse.SC_ACCEPTED);
                response.setData(minimizedPlaylistMapper.mapToDto(playlist));
                response.setStatus(true);
            }else{
                response.setStatus(false);
                response.setMessage("This playlist has already existed");
                response.setData(null);
                response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
        }

        return response;
    }

    public BaseResponse<?> deletePlaylist(Long id){
        Playlist foundPlaylist = findById(id);
        BaseResponse<MinimizedPlaylist> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setStatus(false);
        response.setMessage("Playlist not found with id: " + id);

        if(foundPlaylist != null && foundPlaylist.getActive()){
            foundPlaylist.setActive(false);
            playlistRepository.save(foundPlaylist);

            response.setData(null);
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setStatus(true);
            response.setMessage("Playlist deleted successfully");
        }

        return response;
    }

    public BaseResponse<?> getAllPlaylists(){
        List<Playlist> playlists = playlistRepository.findByActive(true);
        List<MinimizedPlaylist> minimizedPlaylists = new ArrayList<>();
        playlists.forEach(playlist -> {
            minimizedPlaylists.add(minimizedPlaylistMapper.mapToDto(playlist));
        });

        BaseResponse<List<MinimizedPlaylist>> response = new BaseResponse<>();
        response.setMessage("Playlists fetched successfully");
        response.setData(minimizedPlaylists);
        response.setStatus(true);
        response.setCode(HttpServletResponse.SC_OK);

        return response;
    }

    public BaseResponse<?> getPlaylistById(Long id){
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        BaseResponse<MinimizedPlaylist> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(false);
        response.setMessage("Playlist not found with id: " + id);

        if(playlist != null && playlist.getActive()) {
            MinimizedPlaylist minimizedplaylist = minimizedPlaylistMapper.mapToDto(playlist);

            response.setData(minimizedplaylist);
            response.setCode(HttpServletResponse.SC_OK);
            response.setStatus(true);
            response.setMessage("Playlist fetched successfully");
        }

        return response;
    }

    public BaseResponse<?> addSongToPlaylist(AddSongToPlaylistRequest addSongToPlaylistRequest){
        List<Song> songs = songService.findAllByIds(addSongToPlaylistRequest.getSongIds());
        Playlist foundPlaylist = findById(addSongToPlaylistRequest.getPlaylistId());
        BaseResponse<MinimizedPlaylist> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setMessage("Playlist not found with id: " + addSongToPlaylistRequest.getPlaylistId());
        response.setData(null);
        response.setStatus(true);

        if(foundPlaylist != null && foundPlaylist.getActive()){
            songs.forEach(song -> {
                List<Song> playlistSong = foundPlaylist.getSongs();
                if(!playlistSong.contains(song)){
                    playlistSong.add(song);
                }
            });
            MinimizedPlaylist minimizedPlaylist = minimizedPlaylistMapper.mapToDto(save(foundPlaylist));
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setMessage("Song added successfully");
            response.setData(minimizedPlaylist);
            response.setStatus(true);
        }

        return response;
    }

    public BaseResponse<?> deleteSongFromPlaylist(DeleteSongFromPlaylistRequest request){
        Playlist foundPlaylist = findById(request.getPlaylistId());
        BaseResponse<MinimizedPlaylist> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setMessage("Playlist not found with id: " + request.getPlaylistId());
        response.setData(null);
        response.setStatus(true);

        if(foundPlaylist != null && foundPlaylist.getActive()){
            List<Song> songs = songService.findAllByIds(request.getSongIds());
            foundPlaylist.getSongs().removeAll(songs);

            MinimizedPlaylist minimizedPlaylist = minimizedPlaylistMapper.mapToDto(save(foundPlaylist));
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setMessage("Song deleted successfully");
            response.setData(minimizedPlaylist);
            response.setStatus(true);
        }

        return response;
    }
}