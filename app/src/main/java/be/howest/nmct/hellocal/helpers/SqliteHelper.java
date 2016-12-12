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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqliteContract.MyBookings.DELETE_TABLE);
        onCreate(db);
    }
}