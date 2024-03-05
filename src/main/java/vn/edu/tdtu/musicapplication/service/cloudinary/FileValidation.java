package vn.edu.tdtu.musicapplication.service.cloudinary;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.tdtu.musicapplication.enums.EFileUploadStatus;

import java.util.Objects;

@Component
public class FileValidation {
    public EFileUploadStatus validate(MultipartFile[] files){
        if((files.length == 1 && Objects.requireNonNull(files[0].getOriginalFilename()).isEmpty())){
            return EFileUploadStatus.STATUS_EMPTY_FILE;
        }
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            if(name != null && !name.isEmpty()) {
                if(!isCorrectFormat(file)) {
                    return EFileUploadStatus.STATUS_WRONG_EXT;
                }
            }
        }
        return EFileUploadStatus.STATUS_OK;
    }

    public boolean isCorrectFormat(MultipartFile file){
        String name = file.getOriginalFilename();
        if(name != null && !name.isEmpty()) {
            String[] splited = name.split("\\.");
            String ext = splited[splited.length - 1];

            return ext.equals("png") || ext.equals("jpg") ||
                    ext.equals("jpeg") || ext.equals("gif") ||
                    ext.equals("mp3") || ext.equals("wav") ||
                    ext.equals("aac") || ext.equals("flac") ||
                    ext.equals("m4a") || ext.equals("ogg") ||
                    ext.equals("wma") || ext.equals("aiff");
        }

        return true;
    }
}
