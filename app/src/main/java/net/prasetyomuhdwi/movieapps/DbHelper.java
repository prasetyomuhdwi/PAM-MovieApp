package net.prasetyomuhdwi.movieapps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbpam";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_STD = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_STD + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USERNAME + " TEXT, "
            + KEY_FULLNAME + " TEXT, " + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT );";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_STD + "'");
        onCreate(db);
    }

    public long addUserDetail(int id,String username, String fullname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_USERNAME, username);
        values.put(KEY_FULLNAME, fullname);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        long insert = db.insert(TABLE_STD, null, values);

        return insert;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> userModelArrayList = new ArrayList<User>();

        String selectQuery = "SELECT * FROM " + TABLE_STD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                User std = new User();
                std.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                std.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
                std.setFullname(c.getString(c.getColumnIndex(KEY_FULLNAME)));
                std.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                std.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));
                // adding to Users list
                userModelArrayList.add(std);
            } while (c.moveToNext());
        }
        return userModelArrayList;
    }

    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STD, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public int updateUser(int id, String username, String fullname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_FULLNAME, fullname);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);

        return db.update(TABLE_STD, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
}
