package com.mhmdawad.inventoryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mhmdawad.inventoryapp.database.phoneContract.PhoneEntry;

public class phoneDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electronics.db";

    private static final int DATABASE_VERSION = 1;

    public phoneDbHelper(  Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PHONES_TABLE ="CREATE TABLE " + PhoneEntry.TABLE_NAME + "("
                + PhoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +PhoneEntry.COLUMN_COMPANY_PHONE_NAME + " TEXT NOT NULL, "
                +PhoneEntry.COLUMN_PHONE_MODEL + " TEXT NOT NULL, "
                +PhoneEntry.COLUMN_PHONE_PRICE + " REAL NOT NULL, "
                +PhoneEntry.COLUMN_PHONE_QUANTITY + " INTEGER NOT NULL, "
                +PhoneEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                +PhoneEntry.COLUMN_SUPPLIER_NUMBER + " TEXT NOT NULL, "
                +PhoneEntry.COLUMN_PRODUCT_IMAGE + " TEXT NOT NULL )";

        db.execSQL(SQL_CREATE_PHONES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
