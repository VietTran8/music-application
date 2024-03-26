package vn.edu.tdtu.musicapplication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.response.*;
import vn.edu.tdtu.musicapplication.enums.EPackageType;
import vn.edu.tdtu.musicapplication.enums.ERole;
import vn.edu.tdtu.musicapplication.models.*;
import vn.edu.tdtu.musicapplication.models.Package;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.service.*;
import vn.edu.tdtu.musicapplication.utils.PrincipalUtils;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final UserService userService;
    private final SongService songService;
    private final AlbumService albumService;
    private final PrincipalUtils principalUtils;
    private final ArtistService artistService;
    private final GenreService genreService;
    private final PlaylistService playlistService;
    private final PackageService packageService;
    @GetMapping({"/{action}/component"})
    public String homePage(Principal principal, Model model,
                           @PathVariable(name = "action") String action,
                           @RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "limit", defaultValue = "18") int limit,
                           @RequestParam(name = "playlist_id", required = false) Long playlistId,
                           @RequestParam(name = "song_id", required = false) Long songId,
                           @RequestParam(name = "genre_id", required = false) Long genreId,
                           @RequestParam(name = "artist_id", required = false) Long artistId){
        User currentUser;
        model.addAttribute("packages", new ArrayList<>());

        if(principal != null){
            currentUser = principalUtils.loadUserFromPrincipal(principal);
            model.addAttribute("user", currentUser);
        } else {
            currentUser = null;
        }
        model.addAttribute("action", action);

        if(action != null){
            switch (action){
                case "home" -> {
                    List<MinimizedSong> minimizedSongs = songService.getAllSongsForView(1, 12);
                    model.addAttribute("songs", minimizedSongs);
                    model.addAttribute("canPlay", currentUser != null && currentUser.getIsPremium());

                    BaseResponse<List<MinimizedAlbum>> albums = albumService.getAllAlbums(1, 12);
                    List<MinimizedAlbum> minimizedAlbums = albums.getData();
                    model.addAttribute("albums", minimizedAlbums);

                    BaseResponse<List<MinimizedArtistInfo>> data = artistService.getAllArtists(1, 12);
                    List<MinimizedArtistInfo> minimizedArtistInfos = data.getData();
                    model.addAttribute("artists", minimizedArtistInfos);
                }
                case "albums" -> {
                    BaseResponse<List<MinimizedAlbum>> albums = albumService.getAllAlbums(page, limit);
                    List<MinimizedAlbum> minimizedAlbums = albums.getData();
                    model.addAttribute("albums", minimizedAlbums);
                    model.addAttribute("nextPage", page + 1 <= albumService.getTotalPages() ? page + 1 : 1);
                    model.addAttribute("prevPage", page - 1 > 0 ? page - 1 : albumService.getTotalPages());
                }
                case "artists" -> {
                    BaseResponse<List<MinimizedArtistInfo>> data = artistService.getAllArtists(page, limit);
                    List<MinimizedArtistInfo> minimizedArtistInfos = data.getData();
                    model.addAttribute("artists", minimizedArtistInfos);
                    model.addAttribute("nextPage", page + 1 <= artistService.getTotalPages() ? page + 1 : 1);
                    model.addAttribute("prevPage", page - 1 > 0 ? page - 1 : artistService.getTotalPages());
                }
                case "genres" -> {
                    model.addAttribute("selected", "Chọn thể loại");
                    if(genreId != null){
                        Genre foundGenre = genreService.findById(genreId);
                        model.addAttribute("genreGroups", songService.findAllSongsByGenre(foundGenre));
                        model.addAttribute("selected", foundGenre != null ? foundGenre.getName() : "Chọn thể loại");
                    }else{
                        model.addAttribute("genreGroups", songService.findAllSongsGroupByGenre());
                    }
                    model.addAttribute("genres", genreService.getAllGenres().getData());
                    model.addAttribute("canPlay", currentUser != null && currentUser.getIsPremium());
                }
                case "my-playlist" -> {
                    List<MinimizedPlaylist> playlists = playlistService.getAllPlaylists(principal);
                    model.addAttribute("playlists", playlists);
                }
                case "playlist-details" -> {
                    if(playlistId != null) {
                        Playlist playlist = playlistService.findById(playlistId);
                        List<MinimizedSong> songs = playlistService.getSongsFromPlaylist(playlistId).stream().filter(song -> {
                            return !song.getIsPremium() || currentUser.getIsPremium();
                        }).toList();
                        model.addAttribute("songs", songs);
                        model.addAttribute("favouriteSongs", currentUser != null ? currentUser.getFavouriteSongs().stream().map(songService::toMinimized).filter(song -> !song.getIsPremium() && !songs.contains(song) || (song.getIsPremium() && !songs.contains(song) && currentUser.getIsPremium())).toList() : new ArrayList<>());
                        model.addAttribute("noOfThumbs", playlistService.toMinimized(playlist).getThumbnails().size());
                        model.addAttribute("playlist", playlistService.toMinimized(playlist));
                    }else {
                        return "redirect:/home";
                    }
                }
                case "song-details" -> {
                    if(songId != null) {
                        Song song = songService.findById(songId);
                        List<MinimizedPlaylist> playlists = playlistService.getAllPlaylists(principal);
                        boolean saved = playlists.stream().anyMatch(p -> p.getSongs().contains(songService.toMinimized(song)));
                        model.addAttribute("song", song.getActive() ? song : null);
                        model.addAttribute("playlists", playlists);
                        model.addAttribute("saved", saved);
                        model.addAttribute("canPlay", currentUser != null && currentUser.getIsPremium());
                        model.addAttribute("liked", currentUser != null && currentUser.getFavouriteSongs().contains(song));
                    }else {
                        return "redirect:/home";
                    }
                }
                case "favourite-songs" -> {
                    List<MinimizedPlaylist> playlists = playlistService.getAllPlaylists(principal);
                    model.addAttribute("playlists", playlists);
                    model.addAttribute("songs", currentUser != null ? currentUser.getFavouriteSongs() : new ArrayList<>());
                }
                case "artist-details" -> {
                    if(artistId != null) {
                        ArtistInfo artistInfo = artistService.findById(artistId);
                        User user = userService.findByArtistInfo(artistInfo);
                        List<MinimizedSong> songs = artistInfo
                                .getSongs()
                                .stream()
                                .filter(song -> song.getActive() == null || song.getActive())
                                .map(songService::toMinimized)
                                .limit(12)
                                .toList();
                        List<MinimizedAlbum> albums = artistInfo
                                .getAlbums()
                                .stream()
                                .map(albumService::toMinimized)
                                .limit(12).toList();

                        List<FollowerResponse> followers = new ArrayList<>();

                        model.addAttribute("userEmail", "");
                        if(user != null){
                            followers = userService.getFollowersByUserId(user.getId())
                                    .stream()
                                    .map(f -> {
                                        FollowerResponse response = new FollowerResponse();
                                        response.setUser(f);
                                        boolean isArtist = f.getRoles().stream().anyMatch(r -> r.getName().equals(ERole.ROLE_ARTIST));
                                        String redirectUrl = "user-details?user_id=" + f.getId();
                                        if(isArtist){
                                            redirectUrl = "artist-details?artist_id=" + f.getArtistInfo().getId();
                                        }
                                        response.setRedirectUrl(redirectUrl);
                                        response.setArtist(isArtist);

                                        return response;
                                    }).toList();
                            model.addAttribute("userEmail", user.getEmail());
                        }

                        model.addAttribute("artist", artistInfo);

                        model.addAttribute("fewAlbums", albums.stream().limit(12).toList());
                        model.addAttribute("fewFollowers", followers.stream().limit(12).toList());
                        model.addAttribute("fewSongs", songs.stream().limit(12).toList());

                        model.addAttribute("allSongs", songs);
                        model.addAttribute("allAlbums", albums);
                        model.addAttribute("allFollowers", followers);

                        model.addAttribute("isFollowed", followers.stream().map(FollowerResponse::getUser).toList().contains(currentUser));

                        model.addAttribute("createdByAdmin", artistInfo.getPersonalInfo() == null);
                        model.addAttribute("canPlay", currentUser != null && currentUser.getIsPremium());
                        model.addAttribute("isMyAccount", currentUser != null && Objects.equals(currentUser.getId(), user != null ? user.getId() : -1));
                    }else {
                        return "redirect:/home";
                    }
                }
            }
        }

        return action != null ? action : "home";
    }

    @GetMapping({"", "/{action}"})
    public String main(Principal principal, Model model, @PathVariable(value = "action", required = false) String action){
        model.addAttribute("displayPreAd", true);
        model.addAttribute("displayNorAd", true);
        model.addAttribute("loggedIn", principal != null);
        model.addAttribute("packages", new ArrayList<>());

        if(principal != null){
            User currentUser = principalUtils.loadUserFromPrincipal(principal);

            boolean displayPreAd = true;
            boolean displayNorAd = true;
            if(currentUser.getAccountType() == EPackageType.TYPE_PREMIUM){
                displayPreAd = false;
            }
            if(currentUser.getIsPremium()){
                displayNorAd = false;
            }

            List<UserPackageBought> userPackages = packageService.findUserPackageByUser(currentUser);
            model.addAttribute("packages", userPackages);

            model.addAttribute("user", currentUser);
            model.addAttribute("userAvatar", currentUser.getAvatar());
            model.addAttribute("displayPreAd", displayPreAd);
            model.addAttribute("displayNorAd", displayNorAd);

            boolean isArtist = currentUser.getRoles().stream().anyMatch(role -> role.getName().equals(ERole.ROLE_ARTIST));

            if(isArtist){
                model.addAttribute("artistId", currentUser.getArtistInfo().getId());
            }
        }
        return "main";
    }

    @GetMapping("/service/{view}")
    public String service(@PathVariable("view") String view, Model model){
        List<Package> packages = packageService.getAllPackages().getData();
        model.addAttribute("packages", packages);
        return view;
    }
}