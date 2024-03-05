package vn.edu.tdtu.musicapplication.service.cloudinary;

import org.springframework.web.multipart.MultipartFile;
import vn.edu.tdtu.musicapplication.enums.EUploadFolder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IFileService {
    String uploadFile(MultipartFile multipartFile, EUploadFolder folder) throws IOException;
    List<String> uploadMultipleFile(MultipartFile[] multipartFiles, EUploadFolder folder) throws IOException;
    boolean deleteFile(String url, EUploadFolder folder) throws IOException;
    String updateFile(String oldUrl, MultipartFile newFile, EUploadFolder folder) throws IOException;
}
