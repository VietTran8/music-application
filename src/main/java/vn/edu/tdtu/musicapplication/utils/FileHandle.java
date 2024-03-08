package vn.edu.tdtu.musicapplication.utils;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileHandle {
    public static MultipartFile convertHtmlToPdfAsMultipartFile(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes()), outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            return new MockMultipartFile("file", null, "application/pdf", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File downloadFile(String fileURL) {
        File tempFile = null;
        try {
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // Check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Create a temporary file
                tempFile = File.createTempFile("downloadedFile", ".pdf");
                tempFile.deleteOnExit();

                try (InputStream inputStream = httpConn.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    int bytesRead;
                    byte[] buffer = new byte[4096];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                return tempFile;
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (tempFile != null) {
                tempFile.delete();
            }
            return null;
        }
    }
}
