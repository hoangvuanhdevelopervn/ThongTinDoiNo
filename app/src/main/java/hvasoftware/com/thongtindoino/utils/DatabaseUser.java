package hvasoftware.com.thongtindoino.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hvasoftware.com.thongtindoino.model.User;

/**
 * Created by HoangVuAnh on 3/14/18.
 */

public class DatabaseUser extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseUser";
    private static final String DB_NAME = "USER.DB";
    private static final String DB_TABLE = "Users";
    private static final int DB_VERSION = 1;

    /*
    private String objectID;
    private String documentId;
    private String displayName;
    private String account;
    private String password;
    private String address;
    private String phone;
    private String role;
     */

    private static final String USER_OBJECT_ID = "objectId";
    private static final String USER_DOCUMENT_ID = "documentId";
    private static final String USER_DISPLAY_NAME = "displayName";
    private static final String USER_ACCOUNT = "account";
    private static final String USER_PASSWORD = "password";
    private static final String USER_ADDRESS = "address";
    private static final String USER_PHONE = "phone";
    private static final String USER_ROLE = "role";
    @SuppressLint("StaticFieldLeak")
    private static DatabaseUser databaseUser;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;


    public DatabaseUser(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public static DatabaseUser newInstance(Context mContext) {
        if (databaseUser == null) {
            databaseUser = new DatabaseUser(mContext);
        }
        return databaseUser;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists '" + DB_TABLE + "' (id integer primary key,'" + USER_OBJECT_ID + "' VARCHAR(255),  '" + USER_ROLE + "' VARCHAR(255), '" + USER_DOCUMENT_ID + "' VARCHAR(255), '" + USER_DISPLAY_NAME + "' VARCHAR(255), '" + USER_ACCOUNT + "' VARCHAR(255), '" + USER_PASSWORD + "' VARCHAR(255), '" + USER_ADDRESS + "' TEXT, " +
                " '" + USER_PHONE + "' VARCHAR(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @SuppressLint("Assert")
    public boolean deleteAllData() {
        SQLiteDatabase database = getWritableDatabase();
        assert false;
        database.execSQL("delete from " + DB_TABLE);
        return true;
    }

    public boolean insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(USER_OBJECT_ID, user.getObjectID());
            contentValues.put(USER_DOCUMENT_ID, user.getDocumentId());
            contentValues.put(USER_DISPLAY_NAME, user.getDisplayName());
            contentValues.put(USER_ACCOUNT, user.getAccount());
            contentValues.put(USER_PASSWORD, user.getPassword());
            contentValues.put(USER_ADDRESS, user.getAddress());
            contentValues.put(USER_PHONE, user.getPhone());
            contentValues.put(USER_ROLE, user.getRole());
            sqLiteDatabase.insert(DB_TABLE, null, contentValues);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void removeQuote(String objectId) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try {
            String[] strArr = new String[DB_VERSION];
            strArr[0] = objectId;
            sqLiteDatabase.delete(DB_TABLE, "objectId = ?", strArr);
        } catch (SQLException e) {
            Log.d("remove", e.toString());
        }
    }

    public List<User> getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        List<User> userList = new ArrayList<>();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery("select * from '" + DB_TABLE + "' order by id desc", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    User user = new User();
                    user.setObjectID(cursor.getString(cursor.getColumnIndex(USER_OBJECT_ID)));
                    user.setDocumentId(cursor.getString(cursor.getColumnIndex(USER_DOCUMENT_ID)));
                    user.setAccount(cursor.getString(cursor.getColumnIndex(USER_ACCOUNT)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(USER_PASSWORD)));
                    user.setAddress(cursor.getString(cursor.getColumnIndex(USER_ADDRESS)));
                    user.setDisplayName(cursor.getString(cursor.getColumnIndex(USER_DISPLAY_NAME)));
                    user.setPhone(cursor.getString(cursor.getColumnIndex(USER_PHONE)));
                    user.setRole(cursor.getString(cursor.getColumnIndex(USER_ROLE)));
                    userList.add(user);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            sqLiteDatabase.close();
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.wtf(TAG, "===========================> getAllQuotes:" + e.getMessage());
            return null;
        }
    }
}
