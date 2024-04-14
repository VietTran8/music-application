package vn.edu.tdtu.musicapplication.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AddArtistRequest;
import vn.edu.tdtu.musicapplication.dtos.response.ArtistDetails;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedAlbum;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedArtistInfo;
import vn.edu.tdtu.musicapplication.dtos.response.MinimizedSong;
import vn.edu.tdtu.musicapplication.mappers.request.AdminAddArtistMapper;
import vn.edu.tdtu.musicapplication.mappers.response.MinimizedArtistInfoMapper;
import vn.edu.tdtu.musicapplication.models.Album;
import vn.edu.tdtu.musicapplication.models.Song;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistInfo;
import vn.edu.tdtu.musicapplication.repository.ArtistInfoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Getter
public class ArtistService {
    private final ArtistInfoRepository artistInfoRepository;
    private final AdminAddArtistMapper adminAddArtistMapper;
    private final MinimizedArtistInfoMapper minimizedArtistInfoMapper;
    private int totalPages;

    public ArtistInfo findById(Long id){
        return artistInfoRepository.findById(id).orElse(null);
    }

    public MinimizedArtistInfo toMinimized(ArtistInfo artistInfo) {
        return minimizedArtistInfoMapper.mapToDto(artistInfo);
    }

    public long countTotalArtists(){
        return artistInfoRepository.findAllByActive(true)
                .stream().filter(a -> a.getPersonalInfo() != null)
                .count();
    }

    public List<ArtistInfo> findAllByIds(List<Long> ids){
        if(ids != null) {
            List<ArtistInfo> result = new ArrayList<>();
            artistInfoRepository.findAll().forEach(artist -> {
                if(ids.contains(artist.getId())){
                    result.add(artist);
                }
            });
            return result;
        }
        return new ArrayList<>();
    }

    public ArtistInfo save(ArtistInfo artistInfo){
        return artistInfoRepository.save(artistInfo);
    }

    public BaseResponse<?> saveArtistInfo(AddArtistRequest request){
        ArtistInfo artistInfo = adminAddArtistMapper.mapToObject(request);
        MinimizedArtistInfo minimizedArtistInfo = minimizedArtistInfoMapper.mapToDto(artistInfoRepository.save(artistInfo));

        BaseResponse<MinimizedArtistInfo> response = new BaseResponse<>();
        response.setStatus(true);
        response.setMessage("Artist added successfully");
        response.setData(minimizedArtistInfo);
        response.setCode(HttpServletResponse.SC_CREATED);

        return response;
    }

    public BaseResponse<?> getArtistsByName(String name){
        List<ArtistInfo> artistInfos = artistInfoRepository.findByArtistNameContainingIgnoreCase(name);
        List<MinimizedArtistInfo> minimizedArtistInfos = new ArrayList<>();
        BaseResponse<List<MinimizedArtistInfo>> response = new BaseResponse<>();

        artistInfos.forEach(artist -> {
            if(artist.getActive()){
                minimizedArtistInfos.add(minimizedArtistInfoMapper.mapToDto(artist));
            }
        });

        response.setData(minimizedArtistInfos);
        response.setCode(HttpServletResponse.SC_OK);
        response.setStatus(true);
        response.setMessage("Artist fetched successfully");

        return response;
    }

    public BaseResponse<?> updateArtistInfo(Long id, AddArtistRequest request){
        ArtistInfo foundArtistInfo = findById(id);
        BaseResponse<MinimizedArtistInfo> response = new BaseResponse<>();
        response.setMessage("Artist not found with id: " + id);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setData(null);
        response.setStatus(false);

        if(foundArtistInfo != null && foundArtistInfo.getActive()){
            adminAddArtistMapper.bindFromDto(foundArtistInfo, request);
            artistInfoRepository.save(foundArtistInfo);

            response.setMessage("Artist updated successfully");
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setData(minimizedArtistInfoMapper.mapToDto(foundArtistInfo));
            response.setStatus(true);
        }

        return response;
    }

    public BaseResponse<?> deleteArtist(Long id){
        ArtistInfo foundArist = findById(id);
        BaseResponse<MinimizedArtistInfo> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_NOT_ACCEPTABLE);
        response.setStatus(false);
        response.setMessage("Artist not found with id: " + id);

        if(foundArist != null && foundArist.getActive()){
            foundArist.setActive(false);
            artistInfoRepository.save(foundArist);

            response.setData(null);
            response.setCode(HttpServletResponse.SC_ACCEPTED);
            response.setStatus(true);
            response.setMessage("Artist deleted successfully");
        }

        return response;
    }

    public BaseResponse<List<MinimizedArtistInfo>> getAllArtists(int page, int limit){
        List<MinimizedArtistInfo> minimizedArtistInfos = new ArrayList<>();

        Page<ArtistInfo> artistInfoPage = artistInfoRepository.findByActive(true, PageRequest.of(page - 1, limit));
        totalPages = artistInfoPage.getTotalPages();

        artistInfoPage.get().forEach(artist -> {
            minimizedArtistInfos.add(minimizedArtistInfoMapper.mapToDto(artist));
        });

        BaseResponse<List<MinimizedArtistInfo>> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setStatus(true);
        response.setMessage("Artists fetched successfully");
        response.setData(minimizedArtistInfos);

        return response;
    }

    public BaseResponse<List<MinimizedArtistInfo>> getAllArtists() {
        List<MinimizedArtistInfo> minimizedArtistInfos = new ArrayList<>();

        List<ArtistInfo> artistInfoPage = artistInfoRepository.findAllByActive(true);

        artistInfoPage.forEach(artist -> {
            minimizedArtistInfos.add(minimizedArtistInfoMapper.mapToDto(artist));
        });

        BaseResponse<List<MinimizedArtistInfo>> response = new BaseResponse<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setStatus(true);
        response.setMessage("Artists fetched successfully");
        response.setData(minimizedArtistInfos);

        return response;
    }

    public BaseResponse<?> getArtistById(Long id){
        ArtistInfo artistInfo = artistInfoRepository.findById(id).orElse(null);
        BaseResponse<ArtistDetails> response = new BaseResponse<>();
        response.setData(null);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setStatus(false);
        response.setMessage("Artist not found with id: " + id);

        if(artistInfo != null && artistInfo.getActive()) {
            ArtistDetails artistDetails = minimizedArtistInfoMapper.mapToArtistDetails(artistInfo);

            response.setData(artistDetails);
            response.setCode(HttpServletResponse.SC_OK);
            response.setStatus(true);
            response.setMessage("Artist fetched successfully!");
        }

        return response;
    }
}
