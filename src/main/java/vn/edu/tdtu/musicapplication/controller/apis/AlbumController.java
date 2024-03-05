package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddAlbumRequest;
import vn.edu.tdtu.musicapplication.dtos.request.AddSongToAlbumRequest;
import vn.edu.tdtu.musicapplication.dtos.request.DeleteSongFromAlbumRequest;
import vn.edu.tdtu.musicapplication.service.AlbumService;

import java.security.Principal;

@RestController
@RequestMapping("/api/album")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;
    @PostMapping("/add")
    public ResponseEntity<?> addAlbum(@RequestBody AddAlbumRequest requestBody){
        BaseResponse<?> response = albumService.saveAlbum(requestBody);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/add-song")
    public ResponseEntity<?> addSongToAlbum(@RequestBody AddSongToAlbumRequest request){
        BaseResponse<?> response = albumService.addSongToAlbum(request);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete-song")
    public ResponseEntity<?> deleteSongFromAlbum(@RequestBody DeleteSongFromAlbumRequest request){
        BaseResponse<?> response = albumService.deleteSongFromAlbum(request);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateAlbum(@PathVariable Long id, @RequestBody AddAlbumRequest requestBody){
        BaseResponse<?> response = albumService.updateAlbum(id, requestBody);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAlbumsByName(@RequestParam(name = "key", required = true) String name){
        BaseResponse<?> response = albumService.getAlbumsByName(name);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteAlbum(@PathVariable Long id){
        BaseResponse<?> response = albumService.deleteAlbum(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/favourite/{id}")
    public ResponseEntity<?> favouriteAlbum(Principal principal, @PathVariable("id") Long id){
        BaseResponse<?> response = albumService.favourite(principal, id);
        return ResponseEntity.status(response.getCode()).body(response);
    }


    @GetMapping("")
    public ResponseEntity<?> getAllAlbumsByArtist(@RequestParam(name = "artistId") Long artistId){
        BaseResponse<?> response = albumService.getAllAlbumsByArtistId(artistId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAlbumById(@PathVariable("id") Long albumId){
        BaseResponse<?> response = albumService.getAlbumById(albumId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
