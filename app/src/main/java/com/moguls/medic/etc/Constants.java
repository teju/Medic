package com.moguls.medic.etc;

import java.net.HttpURLConnection;

public class Constants {
    public static int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 911;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_TIMEOUT = 401;
    public static final int STATUS_NO_DATA = HttpURLConnection.HTTP_NO_CONTENT;
    public static final int STATUS_NO_CONNECTION = HttpURLConnection.HTTP_NOT_ACCEPTABLE;
    public static final String GET = "get";
    public static final String POST = "post";
    public static int TIMEOUT = 30000;
    public static final String MIME_JPEG = "image/JPEG";
    public static final String MIME_PNG = "image/PNG";
    public static final String MIME_CSV = "text/csv";
    public static final String PIN_FROM_CERT_PEM_FOR_PINNING_INFO_JSON = "497c6868e484ccf0ba0601a6c40b7f10072c6a3c";

}
