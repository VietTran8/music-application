package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.tdtu.musicapplication.service.AlbumService;
import vn.edu.tdtu.musicapplication.service.ArtistService;
import vn.edu.tdtu.musicapplication.service.SongService;
import vn.edu.tdtu.musicapplication.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/search")
@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SongService songService;
    private final AlbumService albumService;
    private final ArtistService artistService;

    @GetMapping()
    public ResponseEntity<?> search(@RequestParam(name = "key", required = false, defaultValue = "") String key){
        Map<String, Object> data = new HashMap<>();

        data.put("songs", songService.getSongsByName(key).getData());
        data.put("artists", artistService.getArtistsByName(key).getData());
        data.put("albums", albumService.getAlbumsByName(key).getData());

        return ResponseEntity.status(200).body(data);
    }
}
