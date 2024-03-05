package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.AddSongRequest;
import vn.edu.tdtu.musicapplication.service.SongService;

import java.security.Principal;

@RestController
@RequestMapping("/api/song")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateSong(@PathVariable("id") Long id, @RequestBody AddSongRequest requestBody){
        BaseResponse<?> response = songService.updateSong(id, requestBody);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSong(@RequestBody AddSongRequest requestBody){
        BaseResponse<?> response = songService.addSong(requestBody);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/favourite/{id}")
    public ResponseEntity<?> favouriteSong(Principal principal, @PathVariable("id") Long id){
        BaseResponse<?> response = songService.favourite(principal, id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id){
        BaseResponse<?> response = songService.deleteSongById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllSongs(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "limit", defaultValue = "10", required = false) Integer limit
    ){
        BaseResponse<?> response = songService.getAllSongs(page, limit);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getSongsByName(@RequestParam(name = "key", required = true) String name){
        BaseResponse<?> response = songService.getSongsByName(name);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable("id") Long id){
        BaseResponse<?> response = songService.getSongById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
