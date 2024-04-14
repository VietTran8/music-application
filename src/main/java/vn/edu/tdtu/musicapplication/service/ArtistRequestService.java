package vn.edu.tdtu.musicapplication.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.MailDetails;
import vn.edu.tdtu.musicapplication.dtos.request.SubmitArtistRequest;
import vn.edu.tdtu.musicapplication.dtos.request.admin.AdApprovalRequest;
import vn.edu.tdtu.musicapplication.dtos.request.admin.ArtistRequestApprovalRequest;
import vn.edu.tdtu.musicapplication.dtos.response.ArtistInfoDetails;
import vn.edu.tdtu.musicapplication.dtos.response.ArtistRequestDetails;
import vn.edu.tdtu.musicapplication.enums.EArtistRequestStatus;
import vn.edu.tdtu.musicapplication.enums.ERole;
import vn.edu.tdtu.musicapplication.mappers.request.SubmitArtistRequestMapper;
import vn.edu.tdtu.musicapplication.mappers.response.ArtistRequestDetailsMapper;
import vn.edu.tdtu.musicapplication.models.Role;
import vn.edu.tdtu.musicapplication.models.User;
import vn.edu.tdtu.musicapplication.models.advertisement.Advertisement;
import vn.edu.tdtu.musicapplication.models.artist_request.ArtistRequest;
import vn.edu.tdtu.musicapplication.repository.ArtistRequestRepository;
import vn.edu.tdtu.musicapplication.repository.RoleRepository;
import vn.edu.tdtu.musicapplication.service.mail.MailService;
import vn.edu.tdtu.musicapplication.utils.PrincipalUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArtistRequestService {
    private final ArtistRequestRepository artistRequestRepository;
    private final SubmitArtistRequestMapper submitArtistRequestMapper;
    private final UserService userService;
    private final MailService mailService;
    private final ArtistRequestDetailsMapper artistRequestDetailsMapper;
    private final RoleRepository roleRepository;
    private final PrincipalUtils principalUtils;
    private final ArtistService artistService;

    public BaseResponse<?> submitArtistRequest(Principal principal, SubmitArtistRequest request){
        BaseResponse<Map<String, Long>> response = new BaseResponse<>();

        if(principal != null){
            User user = principalUtils.loadUserFromPrincipal(principal);
            if(user.getRoles().stream().filter(role -> role.getName() == ERole.ROLE_ARTIST).toList().isEmpty()){
                ArtistRequest artistRequest = submitArtistRequestMapper.mapToObject(request);
                artistRequest.setUser(user);

                artistRequestRepository.save(artistRequest);

                Map<String, Long> data = new HashMap<>();
                data.put("requestId", artistRequest.getId());

                response.setData(data);
                response.setCode(HttpServletResponse.SC_CREATED);
                response.setMessage("Yêu cầu đã được gửi!");
                response.setStatus(true);
            }else{
                response.setData(null);
                response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                response.setMessage("Người dùng này đã là nghệ sĩ");
                response.setStatus(true);
            }
        }else{
            response.setData(null);
            response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
            response.setMessage("Chưa đăng nhập!");
            response.setStatus(false);
        }

        return response;
    }

    public BaseResponse<?> artistRequestApproval(ArtistRequestApprovalRequest request){
        Map<String, Object> data = new HashMap<>();
        BaseResponse<Map<String, Object>> response = new BaseResponse<>();

        ArtistRequest artistRequest = artistRequestRepository.findById(request.getRequestId()).orElse(null);

        if(artistRequest != null && artistRequest.getActive()) {
            EArtistRequestStatus needUpdateStatus = EArtistRequestStatus.valueOf(request.getStatus());
            if(needUpdateStatus != artistRequest.getStatus()
                    && artistRequest.getStatus() == EArtistRequestStatus.PENDING){
                if(needUpdateStatus == EArtistRequestStatus.APPROVED){
                    Role role = roleRepository.findByName(ERole.ROLE_ARTIST).get();
                    artistRequest.getUser().getRoles().add(role);
                    artistRequest.getUser().setArtistInfo(artistRequest.getArtistInfo());
                    artistRequest.getUser().setUsername(artistRequest.getArtistInfo().getArtistName());
                    artistRequest.getUser().setAvatar(artistRequest.getArtistInfo().getImage());

                    userService.saveUser(artistRequest.getUser());
                }else{
                    artistRequest.getUser().setArtistInfo(null);
                    userService.saveUser(artistRequest.getUser());
                }
                artistRequest.setStatus(needUpdateStatus);
                artistRequestRepository.save(artistRequest);

                sendMailToUser(artistRequest.getUser(), needUpdateStatus, request);

                data.put("requestId", artistRequest.getId());
                data.put("status", needUpdateStatus);

                response.setMessage("Artist request approved successfully");
                response.setData(data);
                response.setCode(HttpServletResponse.SC_OK);
                response.setStatus(false);

            }else{
                response.setMessage("Artist request is only approved when in a pending state");
                response.setData(data);
                response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                response.setStatus(false);
            }

        }else{
            response.setMessage("Artist request not found");
            response.setData(data);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setStatus(false);
        }

        return response;
    }

    public BaseResponse<?> revokeArtistRole(Long artistRequest){
        BaseResponse<String> response = new BaseResponse<>();
        artistRequestRepository.findById(artistRequest).ifPresentOrElse(
            request -> {
                User foundUser = request.getUser();
                if(foundUser != null){
                    if(foundUser.getRoles().stream().map(Role::getName).toList().contains(ERole.ROLE_ARTIST)){
                        roleRepository.findByName(ERole.ROLE_ARTIST).ifPresent(role -> {
                            foundUser.getRoles().remove(role);
                            foundUser.getArtistInfo().setPersonalInfo(null);
                            artistService.save(foundUser.getArtistInfo());
                            foundUser.setArtistInfo(null);
                            userService.saveUser(foundUser);
                            request.setActive(false);
                            artistRequestRepository.save(request);

                            response.setCode(200);
                            response.setData(null);
                            response.setMessage("Thu hồi quyền thành công");
                            response.setStatus(true);
                        });
                    }else{
                        response.setCode(200);
                        response.setData(null);
                        response.setMessage("Người dùng này không có quyền nhạc sĩ");
                        response.setStatus(true);
                    }
                }else{
                    response.setCode(200);
                    response.setData(null);
                    response.setMessage("Không tìm thấy người dùng");
                    response.setStatus(true);
                }
            }, () -> {
                    response.setCode(400);
                    response.setData(null);
                    response.setMessage("Không tìm thấy yêu cầu");
                    response.setStatus(false);
            }
        );

        return response;
    }

    public BaseResponse<?> getAllArtistRequest(int page, int limit){
        BaseResponse<List<ArtistRequestDetails>> response = new BaseResponse<>();
        List<ArtistRequest> artistRequests = artistRequestRepository
                .findByActive(true, PageRequest.of(page - 1, limit))
                .get()
                .toList();

        List<ArtistRequestDetails> requestDetails = artistRequests.stream().map(
                artistRequestDetailsMapper::mapToDto
        ).toList();

        response.setMessage("Artist requests fetched successfully");
        response.setStatus(true);
        response.setData(requestDetails);
        response.setCode(HttpServletResponse.SC_OK);


        return response;
    }

    public List<ArtistRequestDetails> getAllArtistRequest(){
        List<ArtistRequest> artistRequests = artistRequestRepository
                .findByActiveOrderByRequestedDateDesc(true);

        return artistRequests.stream().map(
                artistRequestDetailsMapper::mapToDto
        ).toList();
    }

    public BaseResponse<?> deleteArtistRequest(Principal principal, Long requestId){
        BaseResponse<Map<String, Object>> response = new BaseResponse<>();
        ArtistRequest request = artistRequestRepository.findById(requestId).orElse(null);

        if(request != null && (request.getActive() == null || request.getActive()) && request.getStatus() == EArtistRequestStatus.PENDING) {
            if(principal != null){
                if(principal.getName().equals(request.getUser().getEmail())){
                    request.setActive(false);

                    artistRequestRepository.save(request);

                    Map<String, Object> data = new HashMap<>();
                    data.put("requestId", request.getId());

                    response.setStatus(true);
                    response.setCode(HttpServletResponse.SC_OK);
                    response.setData(data);
                    response.setMessage("Artist submission deleted successfully!");
                }else{
                    response.setStatus(false);
                    response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                    response.setData(null);
                    response.setMessage("Can not delete other's artist request");
                }
            }else{
                response.setStatus(false);
                response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
                response.setData(null);
                response.setMessage("You are not authenticated");
            }
        }else {
            response.setStatus(false);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);
            response.setMessage("No suitable artist submission found with id: " + requestId);
        }

        return response;
    }

    public BaseResponse<?> getArtistRequestByUser(Principal principal){
        BaseResponse<List<ArtistRequestDetails>> response = new BaseResponse<>();
        if(principal != null){
            User foundUser = userService.findByEmail(principal.getName());
            List<ArtistRequest> artistRequests = foundUser.getArtistRequests();

            List<ArtistRequestDetails> artistRequestDetails = artistRequests.stream().map(
                    artistRequestDetailsMapper::mapToDto
            ).toList();

            response.setData(artistRequestDetails);
            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("Artist requests fetched successfully");
            response.setStatus(true);
        }else{
            response.setCode(HttpServletResponse.SC_UNAUTHORIZED);
            response.setMessage("You are not authenticated");
            response.setStatus(false);
            response.setData(null);
        }
        return response;
    }

    public BaseResponse<?> getArtistRequestById(Long id){
        BaseResponse<ArtistRequestDetails> response = new BaseResponse<>();
        ArtistRequest request = artistRequestRepository.findById(id).orElse(null);

        if(request != null && request.getActive()){
            response.setCode(HttpServletResponse.SC_OK);
            response.setData(artistRequestDetailsMapper.mapToDto(request));
            response.setMessage("Artist request fetched successfully");
            response.setStatus(true);
        }else{
            response.setStatus(true);
            response.setData(null);
            response.setMessage("Artist request not found with id: " + id);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        }

        return response;
    }

    private void sendMailToUser(User user, EArtistRequestStatus status, ArtistRequestApprovalRequest request){
        MailDetails mailDetails = new MailDetails();
        mailDetails.setSubject("Kết quả phê duyệt tài khoản nghệ sĩ".toUpperCase());
        mailDetails.setSendTo(user.getEmail());

        switch (status){
            case NOT_APPROVED -> {
                mailDetails.setText(String.format(
                        "Xin chào %s, yêu cầu trở thành nghệ sĩ của bạn đã bị từ chối bởi quản trị viên.<br>" +
                                "Lý do: %s. <br>" +
                                "Nếu có thắc mắc, vui lòng liên hệ đường dây nóng: 1999.2923.<br>" +
                                "Cảm ơn bạn đã liên hệ với chúng tôi!",
                        user.getUsername(), request.getReason()
                ));
            }
            case APPROVED -> {
                mailDetails.setText(String.format(
                        "Xin chào %s, yêu cầu trở thành nghệ sĩ của bạn đã được phê duyệt bởi quản trị viên.<br>" +
                                "Nếu có thắc mắc, vui lòng liên hệ đường dây nóng: 19992923.<br>" +
                                "Cảm ơn bạn đã liên hệ với chúng tôi!<br>",
                        user.getUsername()
                ));
            }
        }
        mailService.sendMail(mailDetails);
    }

}
