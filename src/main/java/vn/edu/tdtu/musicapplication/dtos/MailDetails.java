package vn.edu.tdtu.musicapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailDetails {
    private String subject;
    private String text;
    private String sendTo;
}
