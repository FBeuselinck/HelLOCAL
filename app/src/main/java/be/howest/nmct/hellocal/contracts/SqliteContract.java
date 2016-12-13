package be.howest.nmct.hellocal.contracts;

import android.provider.BaseColumns;

public class SqliteContract {

    private SqliteContract() {}

    public static class UserDetails implements BaseColumns{
        public static String TABLE_NAME = "userDetails";
        public static String COLUMN_AVAILABLE = "available";
        public static String COLUMN_BIRTHDATE = "birthDate";
        public static String COLUMN_DESCRIPTION = "description";
        public static String COLUMN_GENDER = "gender";
        public static String COLUMN_HOMETOWN = "hometown";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_PHONENUMBER = "phoneNumber";
        public static String COLUMN_PHOTOURI = "photoUri";
        public static String COLUMN_PROFILEID = "profileId";
        public static String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_AVAILABLE + " text,"
                + COLUMN_BIRTHDATE + " text,"
                + COLUMN_DESCRIPTION + " text,"
                + COLUMN_GENDER + " text,"
                + COLUMN_HOMETOWN + " text,"
                + COLUMN_NAME + " text,"
                + COLUMN_PHONENUMBER + " text,"
                + COLUMN_PHOTOURI + " text,"
                + COLUMN_PROFILEID + " text);";
        public static String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    public static class BookingRequests implements BaseColumns{
        public static String TABLE_NAME = "bookingRequests";
        public static String COLUMN_AVAILABEGUIDESADAPTERID = "AvailabeGuidesAdapterId";
        public static String COLUMN_CONFIRMED = "confirmed";
        public static String COLUMN_DATE = "date";
        public static String COLUMN_GUIDEID = "guideId";
        public static String COLUMN_REQUESTUSERID = "requestUserId";
        public static String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_AVAILABEGUIDESADAPTERID + " text, "
                + COLUMN_CONFIRMED + " text, "
                + COLUMN_DATE + " date, "
                + COLUMN_GUIDEID + " text, "
                + COLUMN_REQUESTUSERID + " text);";
        public static String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


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
        public static String COLUMN_MYBOOKING = "myBooking";
        public static String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID + " integer primary key autoincrement, "
                + COLUMN_COUNTRY + " text, "
                + COLUMN_DATEFROM + " Date, "
                + COLUMN_DATETILL + " Date, "
                + COLUMN_FIREBASEID + " Text, "
                + COLUMN_LOCATION + " Text, "
                + COLUMN_MAXPOEPLE + " integer, "
                + COLUMN_PRICE + " integer, "
                + COLUMN_TRANSPORT + " text, "
                + COLUMN_MYBOOKING + " text "
                + ");";
        public static  String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
