package com.mhmdawad.inventoryapp.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class phoneContentProvider extends ContentProvider {

    public static final String LOG_TAG = phoneContentProvider.class.getSimpleName();
    private static final int PHONE = 100;
    private static final int PHONE_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(phoneContract.AUTHORITY, phoneContract.PHONE_PATH, PHONE);

        uriMatcher.addURI(phoneContract.AUTHORITY, phoneContract.PHONE_PATH + "/#", PHONE_ID);

    }


    private phoneDbHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new phoneDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case PHONE:
                cursor = database.query(phoneContract.PhoneEntry.TABLE_NAME, projection, selection
                        , selectionArgs, null, null, sortOrder);
                break;
            case PHONE_ID:
                selection = phoneContract.PhoneEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(phoneContract.PhoneEntry.TABLE_NAME, projection, selection
                        , selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match) {
            case PHONE:
                return phoneContract.PhoneEntry.CONTENT_LIST_TYPE;
            case PHONE_ID:
                return phoneContract.PhoneEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PHONE:
                return insertPhone(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPhone(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        long id = sqLiteDatabase.insert(phoneContract.PhoneEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PHONE:
                rowsDeleted = sqLiteDatabase.delete(phoneContract.PhoneEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PHONE_ID:
                selection = phoneContract.PhoneEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = sqLiteDatabase.delete(phoneContract.PhoneEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Error With Updating Product" + uri);
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
            Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
        }
        return rowsDeleted;
    }




    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case PHONE:
                return updateProduct(uri,values,selection,selectionArgs);

            case PHONE_ID:
                selection = phoneContract.PhoneEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);

        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs){

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        int rowsUpdated = sqLiteDatabase.update(phoneContract.PhoneEntry.TABLE_NAME, values , selection , selectionArgs);
        if(rowsUpdated !=0){
            getContext().getContentResolver().notifyChange(uri,null );
        }
        return rowsUpdated;
    }

}
