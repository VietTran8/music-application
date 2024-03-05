package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddArtistRequest;
import vn.edu.tdtu.musicapplication.service.ArtistService;

@RequestMapping("/api/artist-info")
@RestController
@RequiredArgsConstructor
public class ArtistInfoController {
    private final ArtistService artistService;
    @PostMapping("/add")
    public ResponseEntity<?> addArtistInfo(@RequestBody AddArtistRequest requestBody){
        BaseResponse<?> response = artistService.saveArtistInfo(requestBody);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getArtistsByName(@RequestParam(name = "key", required = true) String name){
        BaseResponse<?> response = artistService.getArtistsByName(name);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateArtistInfo(@PathVariable Long id, @RequestBody AddArtistRequest requestBody){
        BaseResponse<?> response = artistService.updateArtistInfo(id, requestBody);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteArtistInfo(@PathVariable Long id){
        BaseResponse<?> response = artistService.deleteArtist(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllArtistInfo(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "page", required = false, defaultValue = "10") Integer limit
    ){
        BaseResponse<?> response = artistService.getAllArtists(page, limit);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArtistInfoById(@PathVariable("id") Long artistId){
        BaseResponse<?> response = artistService.getArtistById(artistId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
