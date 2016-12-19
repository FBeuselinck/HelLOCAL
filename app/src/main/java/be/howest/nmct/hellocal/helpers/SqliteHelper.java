package be.howest.nmct.hellocal.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.howest.nmct.hellocal.contracts.SqliteContract;

/**
 * Created by Arno on 12/12/2016.
 */

public class SqliteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Hellocal.db";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqliteContract.MyBookings.CREATE_TABLE);
        db.execSQL(SqliteContract.BookingRequests.CREATE_TABLE);
        db.execSQL(SqliteContract.UserDetails.CREATE_TABLE);
        db.execSQL(SqliteContract.ProfileDetailsMessages.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == 0 && newVersion ==1)
        {
            db.execSQL(SqliteContract.MyBookings.DELETE_TABLE);
            db.execSQL(SqliteContract.MyBookings.CREATE_TABLE);
            db.execSQL(SqliteContract.BookingRequests.DELETE_TABLE);
            db.execSQL(SqliteContract.BookingRequests.CREATE_TABLE);
            db.execSQL(SqliteContract.UserDetails.DELETE_TABLE);
            db.execSQL(SqliteContract.UserDetails.CREATE_TABLE);
        }
        if(oldVersion == 0 && newVersion == 2){
            db.execSQL(SqliteContract.ProfileDetailsMessages.DELETE_TABLE);
            db.execSQL(SqliteContract.ProfileDetailsMessages.CREATE_TABLE);
            db.execSQL(SqliteContract.Messages.DELETE_TABLE);
            db.execSQL(SqliteContract.Messages.CREATE_TABLE);
        }


    }
}
