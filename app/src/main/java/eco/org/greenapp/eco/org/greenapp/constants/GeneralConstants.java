package eco.org.greenapp.eco.org.greenapp.constants;

import java.text.SimpleDateFormat;

/**
 * Created by danan on 3/24/2018.
 */

public class GeneralConstants {
    public static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    public static final String SESSION = "user_session";
    public static final String TOKEN = "token";
    public static final String PASSWORD = "password";
    public static final int ABOUT_RESULT_CODE = 1;
    public static final int LAST_NAME_RESULT_CODE = 2;
    public static final int FIRST_NAME_RESULT_CODE = 3;
    public static final int LOCATION_RESULT_CODE = 4;
    public static final int PHONE_RESULT_CODE = 5;
    public static final int EMAIL_RESULT_CODE  = 6;
    public static final int USERNAME_RESULT_CODE = 7;
    public static final String RESULT_OK = "OK";
    public static final String RESULT_NOT_OK = "fail";
public static final String INVALID = "Acest email/username nu corespunde niciunui cont.";
    public static final String INSERT_ADD = "insert_add" ;
    public static final String INSERT_DEMAND = "demand" ;
    public static final String SUCCESS = "success";

    public static final String INSERT_TRANSACTION  = "insert_transaction";
    public static final int PICK_IMAGE = 10;
    public static final String UPLOAD_KEY = "image";

}
