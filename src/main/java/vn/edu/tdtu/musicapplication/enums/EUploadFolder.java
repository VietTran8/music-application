package vn.edu.tdtu.musicapplication.enums;

public enum EUploadFolder {
    FOLDER_AUDIO("music_application/audio", "video"),
    FOLDER_IMG("music_application/img", "image"),
    FOLDER_OTHERS("music_application/others", "raw");
    private final String folderName;
    private final String resourceType;
    EUploadFolder(String folderName, String resourceType){
        this.folderName = folderName;
        this.resourceType = resourceType;
    }
    public String getFolderName() {
        return folderName;
    }

    public String getResourceType() {
        return resourceType;
    }
}
