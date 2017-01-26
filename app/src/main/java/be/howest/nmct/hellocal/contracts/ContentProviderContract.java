package be.howest.nmct.hellocal.contracts;

import android.net.Uri;

/**
 * Created by Arno on 26/01/2017.
 */

public class ContentProviderContract {

    public static final String AUTHORITY = "be.howest.nmct.hellocal";

    public static final Uri TOURS_URI = Uri.parse("content://" + AUTHORITY + "/tours");

    public static final String TOURS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hellocal.tour";
}
