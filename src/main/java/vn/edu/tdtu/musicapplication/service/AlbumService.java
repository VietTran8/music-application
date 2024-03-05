package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddAlbumRequest;
import vn.edu.tdtu.musicapplication.dtos.request.AddSongToAlbumRequest;
import vn.edu.tdtu.musicapplication.dtos.request.DeleteSongFromAlbumRequest;
import vn.edu.tdtu.musicapplication.dtos.response.AlbumDetails;
import vn.edu.tdtu.musicapplication.dtos.response.FavouriteResponse;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedAlbum;
import vn.edu.tdtu.musicapplication.mappers.request.AddAlbumRequestMapper;
import vn.edu.tdtu.musicapplication.mappers.response.MinimizedAlbumMapper;
import vn.edu.tdtu.musicapplication.models.Album;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.repository.AlbumRepository;
import vn.edu.tdtu.musicapplication.repository.SongRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final MinimizedAlbumMapper minimizedAlbumMapper;
    private final AddAlbumRequestMapper addAlbumRequestMapper;
    private final ArtistService artistService;
    private final SongRepository songRepository;
    private final UserService userService;

    public Album findById(Long id){
        return id != null ? albumRepository.findById(id).orElse(null) : null;
    }

    public Album save(Album album){
        return albumRepository.save(album);
    }

    public BaseResponse<?> saveAlbum(AddAlbumRequest request){
        Album album = addAlbumRequestMapper.mapToObject(request);
        MinimizedAlbum minimizedAlbum = minimizedAlbumMapper.mapToDto(albumRepository.save(album));

        BaseResponse<MinimizedAlbum> response = new BaseResponse<>();
        response.setStatus(true);
        response.setMessage("Album added successfully");
        response.setData(minimizedAlbum);
        response.setCode(HttpServletResponse.SC_CREATED);

        return response;
    }

    public BaseResponse<?> getAlbumsByName(String name){
        List<Album> albums = albumRepository.findByTitleContainingIgnoreCase(name);
        List<MinimizedAlbum> minimizedAlbums = new ArrayList<>();
        BaseResponse<List<MinimizedAlbum>> response = new BaseResponse<>();

        albums.forEach(album -> {
            if(album.getActive()){
                minimizedAlbums.add(minimizedAlbumMapper.mapToDto(album));
            }
        });

        response.setData(minimizedAlbums);
        response.setCode(HttpServletResponse.SC_OK);
        response.setStatus(true);
        response.setMessage("Albums fetched successfully");

        return response;
    }

    public BaseResponse<?> addSongToAlbum(AddSongToAlbumRequest request){
        Album foundAlbum = findById(request.getAlbumId());
        BaseResponse<MinimizedAlbum> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setMessage("Album not found with id: " + request.getAlbumId());
        response.setData(null);
        response.setStatus(true);

        if(foundAlbum != null && foundAlbum.getActive()){
            //Find songs by ids
            List<Song> songs = new ArrayList<>();
            songRepository.findAll().forEach(song -> {
                if(request.getSongIds().contains(song.getId()) && song.getActive()){
                    songs.add(song);
                    song.setAlbum(foundAlbum);
                }
            });

            songs.forEach(song -> {
                List<Song> albumSongs = foundAlbum.getSongs();
                if(!albumSongs.contains(song)){
                    albumSongs.add(song);
                }
            });
            MinimizedAlbum minimizedAlbum = minimizedAlbumMapper.mapToDto(save(foundAlbum));
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setMessage("Song added successfully");
            response.setData(minimizedAlbum);
            response.setStatus(true);
        }

        return response;
    }

    public BaseResponse<?> deleteSongFromAlbum(DeleteSongFromAlbumRequest request){
        Album foundAlbum = findById(request.getAlbumId());
        BaseResponse<MinimizedAlbum> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setMessage("Album not found with id: " + request.getAlbumId());
        response.setData(null);
        response.setStatus(true);

        if(foundAlbum != null && foundAlbum.getActive()){
            //Find songs by ids
            List<Song> songs = new ArrayList<>();
            songRepository.findAll().forEach(song -> {
                if(request.getSongIds().contains(song.getId())
                        && song.getActive()
                        && foundAlbum.getSongs().contains(song)){
                    songs.add(song);
                    song.setAlbum(null);
                }
            });

            foundAlbum.getSongs().removeAll(songs);
            MinimizedAlbum minimizedAlbum = minimizedAlbumMapper.mapToDto(save(foundAlbum));
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setMessage("Song deleted successfully");
            response.setData(minimizedAlbum);
            response.setStatus(true);
        }

        return response;
    }

    public BaseResponse<?> favourite(Principal principal, Long albumId){
        BaseResponse<FavouriteResponse> response = new BaseResponse<>();
        response.setMessage("You are not authenticated");
        response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setData(null);
        response.setStatus(false);

        if(principal != null){
            Album foundAlbum = findById(albumId);
            User foundUser = userService.findByEmail(principal.getName());
            response.setMessage("Album not found with id: " + albumId);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);
            response.setStatus(false);

            if(foundAlbum != null && foundAlbum.getActive()) {
                FavouriteResponse favourite = new FavouriteResponse();

                if(!foundUser.getFavouriteAlbums().contains(foundAlbum)){
                    foundUser.getFavouriteAlbums().add(foundAlbum);
                    favourite.setStatus(false);
                    response.setMessage("Unliked this album: " + albumId);
                }
                else{
                    foundUser.getFavouriteAlbums().remove(foundAlbum);
                    favourite.setStatus(true);
                    response.setMessage("Liked this album: " + albumId);
                }

                userService.saveUser(foundUser);

                favourite.setFavourites(foundAlbum.getUsers().size());

                response.setCode(HttpServletResponse.SC_OK);
                response.setData(favourite);
                response.setStatus(true);
            }
        }

        return response;
    }

    public BaseResponse<?> updateAlbum(Long id, AddAlbumRequest request){
        Album foundAlbum = findById(id);
        BaseResponse<MinimizedAlbum> response = new BaseResponse<>();
        response.setMessage("Album not found with id: " + id);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setData(null);
        response.setStatus(false);

        if(foundAlbum != null && foundAlbum.getActive()){
            addAlbumRequestMapper.bindFromDto(foundAlbum, request);
            albumRepository.save(foundAlbum);

            response.setMessage("Album updated successfully");
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setData(minimizedAlbumMapper.mapToDto(foundAlbum));
            response.setStatus(true);
        }

        return response;
    }

    public BaseResponse<?> deleteAlbum(Long id){
        Album foundAlbum = findById(id);
        BaseResponse<MinimizedAlbum> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setStatus(false);
        response.setMessage("Album not found with id: " + id);

        if(foundAlbum != null && foundAlbum.getActive()){
            foundAlbum.setActive(false);
            albumRepository.save(foundAlbum);

            response.setData(null);
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setStatus(true);
            response.setMessage("Album deleted successfully");
        }

        return response;
    }

    public BaseResponse<?> getAllAlbumsByArtistId(Long artistId){
        ArtistInfo artistInfo = artistService.findById(artistId);
        BaseResponse<List<MinimizedAlbum>> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(false);
        response.setMessage("Artist not found with id: " + artistId);
        response.setData(null);

        if(artistInfo != null){
            List<MinimizedAlbum> minimizedArtistInfos = new ArrayList<>();
            albumRepository.findByActiveAndArtistInfo(true, artistInfo).forEach(album -> {
                minimizedArtistInfos.add(minimizedAlbumMapper.mapToDto(album));
            });

            response.setCode(HttpServletResponse.SC_OK);
            response.setStatus(true);
            response.setMessage("Albums fetched successfully");
            response.setData(minimizedArtistInfos);
        }

        return response;
    }

    public BaseResponse<?> getAlbumById(Long id){
        Album foundAlbum = albumRepository.findById(id).orElse(null);
        BaseResponse<AlbumDetails> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(false);
        response.setMessage("Album not found with id: " + id);

        if(foundAlbum != null && foundAlbum.getActive()) {
            AlbumDetails albumDetails = minimizedAlbumMapper.mapToAlbumDetails(foundAlbum);

            response.setData(albumDetails);
            response.setCode(HttpServletResponse.SC_OK);
            response.setStatus(true);
            response.setMessage("Album fetched successfully");
        }

        return response;
    }
}