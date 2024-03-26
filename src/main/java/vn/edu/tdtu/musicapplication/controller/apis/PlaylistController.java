package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddPlaylistRequest;
import vn.edu.tdtu.musicapplication.dtos.request.AddSongToPlaylistRequest;
import vn.edu.tdtu.musicapplication.dtos.request.DeleteSongFromPlaylistRequest;
import vn.edu.tdtu.musicapplication.dtos.request.RenamePlaylistRequest;
import vn.edu.tdtu.musicapplication.service.PlaylistService;

import java.security.Principal;

@RestController
@RequestMapping("/api/playlist")
@RequiredArgsConstructor
@Slf4j
public class PlaylistController {
    private final PlaylistService playlistService;

    @PostMapping("/add")
    public ResponseEntity<?> addPlaylist(Principal principal, @RequestBody AddPlaylistRequest requestBody){
        BaseResponse<?> response = playlistService.savePlaylist(principal, requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/add-song")
    public ResponseEntity<?> addSongToPlaylist(@RequestBody AddSongToPlaylistRequest request){
        BaseResponse<?> response = playlistService.addSongToPlaylist(request);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete-song")
    public ResponseEntity<?> deleteSongFromPlaylist(@RequestBody DeleteSongFromPlaylistRequest request){
        BaseResponse<?> response = playlistService.deleteSongFromPlaylist(request);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/rename/{id}")
    public ResponseEntity<?> updatePlaylist(Principal principal, @PathVariable("id") Long id, @RequestBody RenamePlaylistRequest requestBody){
        BaseResponse<?> response = playlistService.updatePlaylist(principal, id, requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable("id") Long id){
        BaseResponse<?> response = playlistService.deletePlaylist(id);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}/songs")
    public ResponseEntity<?> getSongsFromPlaylist(@PathVariable("id") Long playlistId){
        BaseResponse<?> response = playlistService.getSongsFromPlaylistResp(playlistId);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping()
    public ResponseEntity<?> getAllPlaylists(){
        BaseResponse<?> response = playlistService.getAllPlaylists();

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable("id") Long PlaylistId){
        BaseResponse<?> response = playlistService.getPlaylistById(PlaylistId);

        return ResponseEntity.status(response.getCode()).body(response);
    }
}