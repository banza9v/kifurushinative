package ridc.sduma.kifurushi.Utils;

public class Constants {
    private static final String DOWNLOADS = "downloads";
    private static final String UPLOADS = "uploads";
    private static final String TOTAL_BYTES = "totalBytes";
    private static final String DOWNLOADS_UNIT = "downloadUnit";
    private static final String UPLOADS_UNIT = "uploadUnit";
    private static final String TOTAL_UNIT = "totalUnit";
    private static final String TYPE_MOBILE = "mobile";
    private static final String TYPE_WIFI = "wifi";
    private static final String APPLICATIONS = "apps";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String PERCENTAGE = "percentage";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    private static final long ONE_HOUR_MILLIS = 3600000;


    public static String getDOWNLOADS() {
        return DOWNLOADS;
    }

    public static String getUPLOADS() {
        return UPLOADS;
    }

    public static String getTotalBytes() {
        return TOTAL_BYTES;
    }

    public static String getDownloadsUnit() {
        return DOWNLOADS_UNIT;
    }

    public static String getUploadsUnit() {
        return UPLOADS_UNIT;
    }

    public static String getTotalUnit() {
        return TOTAL_UNIT;
    }

    public static String getTypeMobile() {
        return TYPE_MOBILE;
    }

    public static String getTypeWifi() {
        return TYPE_WIFI;
    }

    public static String getAPPLICATIONS() {
        return APPLICATIONS;
    }

    public static String getStartDate() {
        return START_DATE;
    }

    public static String getEndDate() {
        return END_DATE;
    }

    public static String getPERCENTAGE() {
        return PERCENTAGE;
    }

    public static long getOneHourMillis() {
        return ONE_HOUR_MILLIS;
    }
}
