package be.howest.nmct.hellocal.contracts;

import android.provider.BaseColumns;

public class SqliteContract {

    private SqliteContract() {}


    public static class MyBookings implements BaseColumns {
        public static String TABLE_NAME = "myBookings";
        public static String COLUMN_FIREBASEID = "firebaseId";
        public static String COLUMN_COUNTRY = "country";
        public static String COLUMN_DATEFROM = "dateFrom";
        public static String COLUMN_DATETILL = "dateTill";
        public static String COLUMN_LOCATION = "location";
        public static String COLUMN_MAXPOEPLE = "maxPoeple";
        public static String COLUMN_PRICE  = "price";
        public static String COLUMN_TRANSPORT = "transport";
        public static String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_COUNTRY + " text, "
                + COLUMN_DATEFROM + " Date, "
                + COLUMN_DATETILL + " Date, "
                + COLUMN_FIREBASEID + " Text, "
                + COLUMN_LOCATION + " Text, "
                + COLUMN_MAXPOEPLE + " integer, "
                + COLUMN_PRICE + " integer, "
                + COLUMN_TRANSPORT + " text "
                + ");";
        public static  String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
