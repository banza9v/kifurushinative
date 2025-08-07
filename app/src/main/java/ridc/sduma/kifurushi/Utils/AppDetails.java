package ridc.sduma.kifurushi.Utils;

import android.graphics.drawable.Drawable;

public class AppDetails {
    private int UID;
    private String appName;
    private Drawable appIcon;
    private String downloadUnit;
    private String uploadUnit;
    private String totalUnit;
    private double downloads;
    private double uploads;
    private double totalBytes;
    private long totalInBytes;
    private long downloadsInBytes;
    private long uploadsInBytes;

    public AppDetails() {
    }

    public AppDetails(int UID, String appName, Drawable appIcon, String downloadUnit, String uploadUnit,
                      String totalUnit, double downloads, double uploads, double totalBytes,
                      long totalInBytes, long downloadsInBytes, long uploadsInBytes) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.downloadUnit = downloadUnit;
        this.uploadUnit = uploadUnit;
        this.totalUnit = totalUnit;
        this.downloads = downloads;
        this.uploads = uploads;
        this.totalBytes = totalBytes;
        this.totalInBytes = totalInBytes;
        this.downloadsInBytes = downloadsInBytes;
        this.uploadsInBytes = uploadsInBytes;
        this.UID = UID;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public long getTotalInBytes() {
        return totalInBytes;
    }

    public void setTotalInBytes(long totalInBytes) {
        this.totalInBytes = totalInBytes;
    }

    public long getDownloadsInBytes() {
        return downloadsInBytes;
    }

    public void setDownloadsInBytes(long downloadsInBytes) {
        this.downloadsInBytes = downloadsInBytes;
    }

    public long getUploadsInBytes() {
        return uploadsInBytes;
    }

    public void setUploadsInBytes(long uploadsInBytes) {
        this.uploadsInBytes = uploadsInBytes;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getDownloadUnit() {
        return downloadUnit;
    }

    public void setDownloadUnit(String downloadUnit) {
        this.downloadUnit = downloadUnit;
    }

    public String getUploadUnit() {
        return uploadUnit;
    }

    public void setUploadUnit(String uploadUnit) {
        this.uploadUnit = uploadUnit;
    }

    public String getTotalUnit() {
        return totalUnit;
    }

    public void setTotalUnit(String totalUnit) {
        this.totalUnit = totalUnit;
    }

    public double getDownloads() {
        return downloads;
    }

    public void setDownloads(double downloads) {
        this.downloads = downloads;
    }

    public double getUploads() {
        return uploads;
    }

    public void setUploads(double uploads) {
        this.uploads = uploads;
    }

    public double getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(double totalBytes) {
        this.totalBytes = totalBytes;
    }
}
