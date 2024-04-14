package vn.edu.tdtu.musicapplication.controller.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.SubmitArtistRequest;
import vn.edu.tdtu.musicapplication.dtos.request.admin.ArtistRequestApprovalRequest;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistRequest;
import vn.edu.tdtu.musicapplication.service.ArtistRequestService;

import java.security.Principal;

@RestController
@RequestMapping("/api/artist-req")
@RequiredArgsConstructor
public class ArtistRequestController {
    private final ArtistRequestService artistRequestService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitArtistRequest(Principal principal, @RequestBody  SubmitArtistRequest request){
        BaseResponse<?> response = artistRequestService.submitArtistRequest(principal, request);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/approval")
    public ResponseEntity<?> artistRequestApproval(@RequestBody ArtistRequestApprovalRequest request){
        BaseResponse<?> response = artistRequestService.artistRequestApproval(request);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/revoke/{id}")
    public ResponseEntity<?> revokeArtistRequest(@PathVariable("id") Long artistRequestId){
        BaseResponse<?> response = artistRequestService.revokeArtistRole(artistRequestId);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteRequest(Principal principal, @PathVariable("id") Long requestId){
        BaseResponse<?> response = artistRequestService.deleteArtistRequest(principal, requestId);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllArtistRequests(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "limit", defaultValue = "10", required = false) Integer limit
    ){
        BaseResponse<?> response = artistRequestService.getAllArtistRequest(page, limit);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArtistRequestById(@PathVariable("id") Long id){
        BaseResponse<?> response = artistRequestService.getArtistRequestById(id);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getArtistRequestByUser(Principal principal){
        BaseResponse<?> response = artistRequestService.getArtistRequestByUser(principal);

        return ResponseEntity.status(response.getCode()).body(response);
    }
}