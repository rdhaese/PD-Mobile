package be.rdhaese.project.mobile.constants;

/**
 * Created by RDEAX37 on 21/04/2016.
 */
public class Constants {

    //Request codes
    public static final Integer SCAN_REQUEST_CODE = 0;
    public static final int PERMISSIONS_REQUEST_CODE = 1;

    //Scan constants
    public static final String PACKAGE_SCAN = "com.google.android.apps.unveil";
    public static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static final String EXTRA_SCAN_MODE = "SCAN_MODE";
    public static final String EXTRA_QR_CODE_MODE = "QR_CODE_MODE";
    public static final String SCAN_RESULT_KEY = "SCAN_RESULT";

    //Navigation constants
    public static final String PACKAGE_MAPS = "com.google.android.apps.maps";
    public static final Integer NAVIGATION_START_DELAY_MILLIS = 1000 * 3; //3 seconds

    //App id constants
    public static final String FILE_APP_ID = "app_id";

    //Bean key constants
    public static final String ROUND_SERVICE_KEY = "roundService";
    public static final String APP_SERVICE_KEY = "appService";
    public static final String DIALOG_TOOL_KEY = "dialogTool";
    public static final String TOAST_TOOL_KEY = "toastTool";
    public static final String APP_ID_TOOL_KEY = "appIdTool";
    public static final String NAVIGATION_TOOL_KEY = "navigationTool";
    public static final String SCANNED_HOLDER_KEY = "scannedHolder";

    //Invalid round id constant
    public static final Long INVALID_ROUND_ID = -1L;

    //Amount of packets per round boundary constants
    public static final int ROUND_MIN_AMOUNT_PACKETS = 1;
    public static final int ROUND_MAX_AMOUNT_PACKETS = 10;

    //Text length constants
    public static final int REMARK_MIN_LENGTH = 0;
    public static final int REMARK_MAX_LENGTH = 255;
    public static final int REASON_MIN_LENGTH = 0;
    public static final int REASON_MAX_LENGTH = 255;

    //Intent and Bundle key constants
    public static final String ROUND_ID_KEY = "roundId";
    public static final String PACKETS_KEY = "packets";
    public static final String ORIGINAL_AMOUNT_OF_PACKETS_KEY = "originalAmountOfPackets";
    public static final String NAVIGATION_STARTED_KEY = "navigationStarted";
    public static final String MESSAGE_KEY = "message";
    public static final String ROUND_FINISHED_KEY = "roundFinished";
    public static final String CURRENT_PACKET_INDEX_KEY = "currentPacketIndex";
    public static final String GOOGLE_NAVIGATION_QUERY_START = "google.navigation:q=";
    public static final String MESSAGE_SHOWN_KEY = "messageShown";
    public static final String SEARCH_PACKETS_LIST_KEY = "searchPacketsPacketDTOs";
    public static final String PACKETS_LIST_KEY = "packets";

    //Toast location constants
    public static final int TOAST_X_OFFSET = 0;
    public static final int TOAST_Y_OFFSET = 150;

    //Orientation
    public static final int ORIENTATION_LANDSCAPE = 2;
}
