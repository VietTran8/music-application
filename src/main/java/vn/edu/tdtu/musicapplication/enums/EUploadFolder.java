package vn.edu.tdtu.musicapplication.enums;

public enum EUploadFolder {
    FOLDER_AUDIO("music_application/audio"), FOLDER_IMG("music_application/img");
    private final String folderName;
    EUploadFolder(String folderName){
        this.folderName = folderName;
    }
    public String getFolderName() {
        return folderName;
    }
}
