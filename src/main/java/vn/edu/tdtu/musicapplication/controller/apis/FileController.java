package vn.edu.tdtu.musicapplication.controller.apis;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.tdtu.musicapplication.dtos.BaseResponse;
import vn.edu.tdtu.musicapplication.dtos.request.FileReq;
import vn.edu.tdtu.musicapplication.enums.EUploadFolder;
import vn.edu.tdtu.musicapplication.service.cloudinary.IFileService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final IFileService fileService;
    @PostMapping("/upload/{type}")
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file, @PathVariable("type") String type){
        BaseResponse<Map<String, String>> response = new BaseResponse<>();
        Map<String, String> data = new HashMap<>();
        data.put("url", null);
        response.setStatus(false);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setData(data);

        EUploadFolder folder = EUploadFolder.FOLDER_AUDIO;
        if(type.equals("img")){
            folder = EUploadFolder.FOLDER_IMG;
        }
        try {
            String fileName = fileService.uploadFile(file, folder);

            data.put("url", fileName);
            response.setStatus(true);
            response.setCode(HttpServletResponse.SC_CREATED);
            response.setData(data);
            response.setMessage("File uploaded successfully");
            return ResponseEntity.ok(response);
        }catch (IOException e){
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/delete/{type}")
    public ResponseEntity<?> deleteFile(@RequestBody FileReq request, @PathVariable("type") String type){
        BaseResponse<Map<String, String>> response = new BaseResponse<>();
        response.setStatus(false);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setData(null);

        EUploadFolder folder = EUploadFolder.FOLDER_AUDIO;
        if(type.equals("img")){
            folder = EUploadFolder.FOLDER_IMG;
        }
        try {
            boolean isOk = fileService.deleteFile(request.getUrl(), folder);

            response.setStatus(isOk);
            response.setCode(isOk ? HttpServletResponse.SC_ACCEPTED : HttpServletResponse.SC_BAD_REQUEST);
            response.setMessage(isOk ? "File deleted successfully" : "File not found to delete");

            return ResponseEntity.status(response.getCode()).body(response);

        } catch (IOException e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/update/{type}")
    public ResponseEntity<?> updateFile(@RequestParam("url") String oldUrl, @RequestParam("newFile") MultipartFile newFile, @PathVariable("type") String type){
        BaseResponse<Map<String, String>> response = new BaseResponse<>();
        Map<String, String> data = new HashMap<>();
        data.put("url", null);
        response.setStatus(false);
        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
        response.setData(data);

        EUploadFolder folder = EUploadFolder.FOLDER_AUDIO;
        if(type.equals("img")){
            folder = EUploadFolder.FOLDER_IMG;
        }
        try {
            String newUrl = fileService.updateFile(oldUrl, newFile, folder);

            data.put("url", newUrl);
            response.setStatus(true);
            response.setCode(HttpServletResponse.SC_OK);
            response.setData(data);
            response.setMessage("File updated successfully");
            return ResponseEntity.status(response.getCode()).body(response);
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.badRequest().body(response);
    }
}
