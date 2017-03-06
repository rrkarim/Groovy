package appClasses;

/**
 * Created by YoAtom on 11/6/2016.
 */

public class AppInfo {
    public static String serverUri = "http://10.0.2.2";
    public static String serverImageUri = "http://10.0.2.2/images/";
    public static String serverRequestGetPost = "getPosts.php";
    public static String serverRequestGetCountries = "getCountries.php";
    public static String serverRequestLike = "like.php";
    public static String serverRequestPost = "post.php";
    public static String sertverAddConn = "addNewConnection.php";
    public static String serverGetLikes = "getUserLikes.php";
    public static String serverSearchNews = "search.php";
    public static String serverUsers = "checkUser.php";

    public static String sharedPrefernce = "USERINFO_";


    //last fm api
    public static String lastFmApiKey = "9e55a8eaead6ab6215f19da79abadc39";
    public static String lastFmGetArtistInfo = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=Cher&api_key=" + lastFmApiKey + "&format=json";
    //
    public static String USER_ID = "1";

    // Notifications
    public static int NOTIFY_ID = 1; // music info change

    public static String[] genres = {"All", "Hip Hop", "Rock", "R'n'B", "Classical", "Jazz", "Pop", "Blues", "Electronic", "Indie"};
    public static int genresSize = 10;
}
