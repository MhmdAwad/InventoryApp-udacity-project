package com.mhmdawad.inventoryapp.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class phoneContract {

    public phoneContract() {
    }

    public static final String AUTHORITY = "com.mhmdawad.inventoryapp";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PHONE_PATH = "phone";


    public static final class PhoneEntry implements BaseColumns {

        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PHONE_PATH);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                AUTHORITY + "/" + PHONE_PATH;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + AUTHORITY + "/" + PHONE_PATH;

        public static final String TABLE_NAME = "phone";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_COMPANY_PHONE_NAME = "name";

        public static final String COLUMN_PHONE_MODEL = "model";

        public static final String COLUMN_PHONE_QUANTITY = "quantity";

        public static final String COLUMN_PHONE_PRICE = "price";

        public static final String COLUMN_SUPPLIER_NAME = "nsupplier";

        public static final String COLUMN_SUPPLIER_NUMBER = "psupplier";

        public static final String COLUMN_PRODUCT_IMAGE = "image";



    }


}
