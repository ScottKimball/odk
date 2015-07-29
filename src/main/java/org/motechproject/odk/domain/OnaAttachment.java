package org.motechproject.odk.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class OnaAttachment {

    private String mimeType;
    private String meduimDownloadUrl;
    private String downloadUrl;
    private String fileName;
    private int instance;
    private String smallDownloadUrl;
    private int id;
    private int xform;

    @JsonProperty("mimetype")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JsonProperty("medium_download_url")
    public String getMeduimDownloadUrl() {
        return meduimDownloadUrl;
    }

    public void setMeduimDownloadUrl(String meduimDownloadUrl) {
        this.meduimDownloadUrl = meduimDownloadUrl;
    }

    @JsonProperty("download_url")
    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @JsonProperty("filename")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getInstance() {
        return instance;
    }

    public void setInstance(int instance) {
        this.instance = instance;
    }

    @JsonProperty("small_download_url")
    public String getSmallDownloadUrl() {
        return smallDownloadUrl;
    }

    public void setSmallDownloadUrl(String smallDownloadUrl) {
        this.smallDownloadUrl = smallDownloadUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getXform() {
        return xform;
    }

    public void setXform(int xform) {
        this.xform = xform;
    }
}
