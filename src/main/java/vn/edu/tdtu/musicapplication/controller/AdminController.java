package vn.edu.tdtu.musicapplication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedArtistInfo;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedGenre;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;
import vn.edu.tdtu.musicapplication.service.*;
import vn.edu.tdtu.musicapplication.utils.PrincipalUtils;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final PrincipalUtils principalUtils;
    private final SongService songService;
    private final UserService userService;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final GenreService genreService;
    private final ArtistRequestService artistRequestService;
    private final PackageService packageService;
    private final AdPackageService adPackageService;

    @GetMapping({"/{view}", ""})
    public String admin(Principal principal,
                        @PathVariable(name = "view", required = false) String view,
                        Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                        @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        model.addAttribute("user", principalUtils.loadUserFromPrincipal(principal));
        view = Objects.requireNonNullElse(view, "home");
        model.addAttribute("view", view);

        switch (view){
            case "home" -> {
                model.addAttribute("countUsers", userService.countTotalUsers());
                model.addAttribute("countSongs", songService.countTotalSongs());
                model.addAttribute("countAlbums", albumService.countTotalAlbums());
                model.addAttribute("countArtists", artistService.countTotalArtists());
            }
            case "accounts" -> {
                Map<String, Object> data = userService.getAllUsers(page, size);

                int nextPage = page + 1;
                int prevPage = page - 1;
                int totalPages = (int) data.get("totalPages");

                model.addAttribute("data", data);
                model.addAttribute("prevPage", Math.max(prevPage, 1));
                model.addAttribute("nextPage", nextPage <= totalPages ? nextPage : 1);
            }
            case "song" -> {
                Map<String, Object> data = songService.getAllSongsForAdminView(page, size);

                int nextPage = page + 1;
                int prevPage = page - 1;
                int totalPages = (int) data.get("totalPages");

                model.addAttribute("genres", genreService.getAllGenres().getData());
                model.addAttribute("artists", artistService.getAllArtists().getData());
                model.addAttribute("albums", albumService.getAllAlbums().getData());

                model.addAttribute("data", data);
                model.addAttribute("prevPage", Math.max(prevPage, 1));
                model.addAttribute("nextPage", nextPage <= totalPages ? nextPage : 1);
            }
            case "artists" -> {
                List<MinimizedArtistInfo> artistInfoList = artistService.getAllArtists().getData().stream().filter(
                        MinimizedArtistInfo::isCreatedByAdmin
                ).toList();

                model.addAttribute("artists", artistInfoList);
                model.addAttribute("genres", genreService.getAllGenres().getData());
                model.addAttribute("artistRequests", artistRequestService.getAllArtistRequest());
            }
            case "genre" -> {
                Map<String, Object> data = genreService.getAllAdminGenres(page, size);

                int nextPage = page + 1;
                int prevPage = page - 1;
                int totalPages = (int) data.get("totalPages");

                model.addAttribute("data", data);
                model.addAttribute("prevPage", Math.max(prevPage, 1));
                model.addAttribute("nextPage", nextPage <= totalPages ? nextPage : 1);
            }
            case "packages" -> {
                Map<String, Object> data = packageService.getAllPackagesForView(page, size).getData();

                int nextPage = page + 1;
                int prevPage = page - 1;
                int totalPages = (int) data.get("totalPages");

                model.addAttribute("data", data);
                model.addAttribute("prevPage", Math.max(prevPage, 1));
                model.addAttribute("nextPage", nextPage <= totalPages ? nextPage : 1);
            }
            case "ads-packages" -> {
                Map<String, Object> data = adPackageService.getAllPackages(page, size);
                List<Advertisement> advertisements = adPackageService.getAllAds().getData().stream().filter(
                        advertisement -> {
                            return advertisement.getUser() != null;
                        }
                ).toList();

                int nextPage = page + 1;
                int prevPage = page - 1;
                int totalPages = (int) data.get("totalPages");

                model.addAttribute("requests", advertisements);
                model.addAttribute("data", data);
                model.addAttribute("prevPage", Math.max(prevPage, 1));
                model.addAttribute("nextPage", nextPage <= totalPages ? nextPage : 1);
            }
        }

        return "admin/main";
    }
}
