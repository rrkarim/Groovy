package SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Classes.User;

/**
 * Created by YoAtom on 12/21/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";
    // Database Info
    private static final String DATABASE_NAME = "UrlHistory";
    private static final int DATABASE_VERSION = 1;

    //Table Names
    private static final String TABLE_USERdETAIL = "userinfo";


    //userdetail Table Columns
    private static final String UID = "id";
    private static final String UNAME = "name";
    private static final String USURNAME = "surname";
    private static final String UIMAGE = "image";
    private static final String UIMAGES = "image_small";
    private static final String UDATE = "date";
    private static final String UFOL = "fol_count";


    private static String CREATE_USERDETAIL_TABLE = "CREATE TABLE " + TABLE_USERdETAIL +
            "(" +
            UID + " NUMBER ," +
            UNAME + " TEXT," +
            USURNAME + " TEXT," +
            UIMAGE + " TEXT," +
            UIMAGES + " TEXT," +
            UDATE + " TEXT," +
            UFOL + " TEXT" +
            ")";
    private static DBHelper mDbHelper;

    public static synchronized DBHelper getInstance(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new DBHelper(context.getApplicationContext());
        }
        return mDbHelper;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERDETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERdETAIL);
        onCreate(db);
    }

    public void insertUserDetail(User userData) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();

            values.put(UID, userData.getId());
            values.put(UNAME, userData.getName());
            values.put(USURNAME, userData.getSurname());
            values.put(UIMAGE, userData.getImage());
            values.put(UIMAGES, userData.getImage_small());
            values.put(UDATE, userData.getDate());
            values.put(UFOL, userData.getFol_cont());

            db.insertOrThrow(TABLE_USERdETAIL, null, values);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d(TAG, "Error while trying to add post to database");
        } finally {

            db.endTransaction();
        }
    }



    public boolean isUserSectionExist() {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_USERdETAIL;

        Cursor cursor = db.rawQuery(selectString, null);

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;
        }
        cursor.close();
        db.close();
        return hasObject;
    }

    public User returnUser() {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_USERdETAIL;
        String userName = null, userSurname = null, userImage = null;
        int userId = 0, userFolCont = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectString, null);
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                userId = cursor.getInt(cursor.getColumnIndex(UID));
                userFolCont  = cursor.getInt(cursor.getColumnIndex(UFOL));
                userName = cursor.getString(cursor.getColumnIndex(UNAME));
                userSurname = cursor.getString(cursor.getColumnIndex(USURNAME));
                userImage = cursor.getString(cursor.getColumnIndex(UIMAGE));
            }
        }
        finally {
            cursor.close();
        }
        User ret = new User();
        ret.setId(userId);
        ret.setFol_cont(userFolCont);
        ret.setName(userName);
        ret.setSurname(userSurname);
        ret.setImage(userImage);
        return ret;
    }

}