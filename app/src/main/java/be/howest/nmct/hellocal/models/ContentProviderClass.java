package be.howest.nmct.hellocal.models;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

import be.howest.nmct.hellocal.contracts.ContentProviderContract;
import be.howest.nmct.hellocal.contracts.SqliteContract;
import be.howest.nmct.hellocal.helpers.SqliteHelper;

/**
 * Created by Arno on 26/01/2017.
 */

public class ContentProviderClass extends ContentProvider {


    private static final int TOURS = 1;

    private static HashMap<String, String> TOURS_PROJECTION_MAP;
    private static SqliteHelper dbHelper;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContentProviderContract.AUTHORITY, "products", TOURS);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new SqliteHelper(getContext());
        TOURS_PROJECTION_MAP = new HashMap<>();
        TOURS_PROJECTION_MAP.put(SqliteContract.MyBookings.COLUMN_DATEFROM, SqliteContract.MyBookings.COLUMN_DATEFROM);
        TOURS_PROJECTION_MAP.put(SqliteContract.MyBookings.COLUMN_FIREBASEID, SqliteContract.MyBookings.COLUMN_FIREBASEID);
        TOURS_PROJECTION_MAP.put(SqliteContract.MyBookings.COLUMN_COUNTRY, SqliteContract.MyBookings.COLUMN_COUNTRY);
        TOURS_PROJECTION_MAP.put(SqliteContract.MyBookings.COLUMN_MYBOOKING, SqliteContract.MyBookings.COLUMN_MYBOOKING);
        TOURS_PROJECTION_MAP.put(SqliteContract.MyBookings.COLUMN_MAXPOEPLE, SqliteContract.MyBookings.COLUMN_MAXPOEPLE);
        TOURS_PROJECTION_MAP.put(SqliteContract.MyBookings.COLUMN_TRANSPORT, SqliteContract.MyBookings.COLUMN_TRANSPORT);
        TOURS_PROJECTION_MAP.put(SqliteContract.MyBookings.COLUMN_DATETILL, SqliteContract.MyBookings.COLUMN_DATETILL);
        TOURS_PROJECTION_MAP.put(SqliteContract.MyBookings.COLUMN_PRICE, SqliteContract.MyBookings.COLUMN_PRICE);
        TOURS_PROJECTION_MAP.put(SqliteContract.MyBookings.COLUMN_LOCATION, SqliteContract.MyBookings.COLUMN_LOCATION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case TOURS:
                queryBuilder.setTables(SqliteContract.MyBookings.TABLE_NAME);
                queryBuilder.setProjectionMap(TOURS_PROJECTION_MAP);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor data = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        data.getCount();

        data.setNotificationUri(getContext().getContentResolver(), uri);
        return data;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TOURS:
                return ContentProviderContract.TOURS_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
        //geen insert vanuit de content provider toelaten
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
