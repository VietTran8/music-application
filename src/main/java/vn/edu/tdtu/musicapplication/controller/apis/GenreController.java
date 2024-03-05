package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddGenreRequest;
import vn.edu.tdtu.musicapplication.service.GenreService;

@RequestMapping("/api/genre")
@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    @PostMapping("/add")
    public ResponseEntity<?> addGenre(@RequestBody AddGenreRequest requestBody){
        BaseResponse<?> response = genreService.saveGenre(requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable("id") Long id, @RequestBody AddGenreRequest requestBody){
        BaseResponse<?> response = genreService.updateGenre(id, requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable("id") Long id){
        BaseResponse<?> response = genreService.deleteGenre(id);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping()
    public ResponseEntity<?> getAllGenres(){
        BaseResponse<?> response = genreService.getAllGenres();

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable("id") Long genreId){
        BaseResponse<?> response = genreService.getGenreById(genreId);

        return ResponseEntity.status(response.getCode()).body(response);
    }

}
