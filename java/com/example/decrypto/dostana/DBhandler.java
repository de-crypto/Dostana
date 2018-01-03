package com.example.decrypto.dostana;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBhandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userdata.db";
    private static final String TABLE_NAME= "mytable";
    private static final String TABLE_NAME1= "phonebook";

    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_IMAGEPATH= "imagepath";

    private static final String COLUMN_USERNAME1 = "username";
    private static final String COLUMN_PHONE1 = "phonenum";
    //Constructor for Database
    public DBhandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String query = "CREATE TABLE " + TABLE_NAME + "(" +
               COLUMN_USERNAME + " TEXT, " +
               COLUMN_PASSWORD + " TEXT, " +
               COLUMN_IMAGEPATH + " TEXT " + " DEFAULT " + "'img'," +
               " PRIMARY KEY (" + COLUMN_USERNAME + ")" +
               ");";
        db.execSQL(query);
        String query1 = "CREATE TABLE " + TABLE_NAME1 + "(" +
                COLUMN_USERNAME1 + " TEXT, " +
                COLUMN_PHONE1 + " TEXT, " +
                " PRIMARY KEY (" + COLUMN_USERNAME1 + ")" +
                ");";
        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Adding a new row to the Database
    public void addNewuser(String username, String password , String number){
        /*ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME,username);
        values.put(COLUMN_PASSWORD,password);*/
        String query = "INSERT INTO " + TABLE_NAME + "(username,password,imagepath) " +
                        " VALUES (" + "'" + username + "'" + " , " + "'" + password + "'" +
                         " , " + " 'acb' " + ");";
        String query1 = "INSERT INTO " + TABLE_NAME1 + "(username,phonenum) " +
                " VALUES (" + "'" + username + "'" + " , " + "'" + number + "'" + ");";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.execSQL(query1);
        db.close();
    }

    //Adding image to the existing user
    public void addimage(String username, String imagepath ){
        String query = "UPDATE " + TABLE_NAME +
                       " SET " + COLUMN_IMAGEPATH + " = '" + imagepath +
                       "' WHERE " + COLUMN_USERNAME + " = '" + username + "';";
         SQLiteDatabase db = getWritableDatabase();
         db.execSQL(query);
        db.close();
    }

    //Getting all the username from the Database
    public String[] get_username(){
       SQLiteDatabase db = getReadableDatabase();
        Cursor crs = db.rawQuery("SELECT * FROM mytable", null);
        String[] array = new String[crs.getCount()];
        int i = 0;
        while(crs.moveToNext()){
            String uname = crs.getString(crs.getColumnIndex("username"));
            array[i] = uname;
            i++;
        }
        crs.close();
        db.close();
        return array;
    }

    public String[] get_imagepath(String[] users){
        SQLiteDatabase db = getReadableDatabase();
        Cursor crs = db.rawQuery("SELECT * FROM mytable", null);
        String[] array = new String[crs.getCount()];
        int i = 0;
        for(int j=0;j<users.length;j++) {
            String str =  check_image(users[j]);
            array[i] = str;
            i++;
        }
        crs.close();
        db.close();
        return array;
    }

    //Delete a row from the database
    public void deleteuser(String username){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = '" + username + "';");
        db.close();
    }

    public String databasetostring(){
        String dbstring="";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        //cursor point to a location result
        Cursor c = db.rawQuery(query, null);
        //move to first row
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("username"))!=null){
                dbstring+=c.getString(c.getColumnIndex("username"));
                dbstring+="\n";
            }
        }
        db.close();
        return dbstring;
    }

    public  boolean CheckIsDataAlreadyInDBorNot(String username){
        SQLiteDatabase db = getWritableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COLUMN_USERNAME + " = '" + username + "';";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    //To check the database for the user credentials
     public boolean check_database(String username, String password) {
         SQLiteDatabase db = getWritableDatabase();
         String query = "Select * from " + TABLE_NAME + " where " + COLUMN_USERNAME + " = '" + username + "';";
         Cursor c = db.rawQuery(query, null);
         boolean chk = c.moveToFirst();
         if (!chk) {
             c.close();
             return false;
         }
         else {
             if (c.getString(c.getColumnIndex("username")) == null) {
                 c.close();
                 return false;
             } else {
                 if (c.getString(c.getColumnIndex("password")).compareTo(password) != 0) {
                     c.close();
                     return false;
                 }
             }
             return true;
         }
     }

    public String check_image(String username) {
        String image = "acb",path;
        SQLiteDatabase db = getWritableDatabase();
        String query = "Select * from " + TABLE_NAME + " where " + COLUMN_USERNAME + " = '" + username + "';";
        Cursor c = db.rawQuery(query, null);
        boolean chk = c.moveToFirst();
        if (!chk) {
            return null;
        }
        else {
            if (c.getString(c.getColumnIndex("username")) == null) {
                c.close();
                return null;
            } else {
                if (c.getString(c.getColumnIndex("imagepath")).compareTo(image) ==0 ) {
                    c.close();
                    return null;
                }
                else
                {
                  path= c.getString(c.getColumnIndex("imagepath"));
                }
            }
            db.close();
            return path;
        }
    }

    public String get_phonenum(String username) {
        String path,DEFAULT="1234567890";
        SQLiteDatabase db = getWritableDatabase();
        String query = "Select * from " + TABLE_NAME1 + " where " + COLUMN_USERNAME1 + " = '" + username + "';";
        Cursor c = db.rawQuery(query, null);
        boolean chk = c.moveToFirst();
        if (!chk) {
            return null;
        }
        else {
            if (c.getString(c.getColumnIndex("username")) == null) {
                c.close();
                return DEFAULT;
            } else {
                    path= c.getString(c.getColumnIndex("phonenum"));
            }
            db.close();
            return path;
        }
    }

}
