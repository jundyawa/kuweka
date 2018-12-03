package methods;

public class Constants {

    // All the final constants used in the software are found here

    // DEVELOPMENT
    public static final boolean DEVMODE = true;
    public static final boolean GHOSTMODE = true;

    // Variables Updated on Login
    public static String ACCOUNT_NAME;

    // --- Local Host ---
    public static final String LOCAL_DB_NAME = "kuwekaDB";
    public static final String LOCAL_PREFERENCES_NAME = "kuwekaSP";

    // Cloud Synced
    public static final int UPDATED = 1;
    public static final int UNSUCCESSFUL_CREATE = 2;
    public static final int UNSUCCESSFUL_UPDATE = 3;
    public static final int PENDING = 4;

    // Keyboard Setup
    public static final int LETTERS_NO_EXTRA = 1;
    public static final int LETTERS_WITH_EXTRA = 2;
    public static final int NUMBERS_NO_EXTRA = 3;
    public static final int NUMBERS_WITH_PLUS = 4;
    public static final int NUMBERS_WITH_POINT = 5;
    public static final int NO_KEYBOARD = 6;

    // Request Codes
    public static final int REQUEST_CODE_SIGNUP = 42;
    public static final int REQUEST_CODE_ADD_SUPPLIER = 43;
    public static final int REQUEST_CODE_ADD_TRANSACTION = 44;
    public static final int REQUEST_CODE_EDIT_TRANSACTION = 45;
}
