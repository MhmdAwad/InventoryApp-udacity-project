package com.mhmdawad.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mhmdawad.inventoryapp.database.phoneContract.PhoneEntry;

import com.mhmdawad.inventoryapp.database.phoneContract;
import com.mhmdawad.inventoryapp.database.phoneCursorAdapter;

public class ViewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    phoneCursorAdapter phoneCursorAdapter;
    static final int LOADER_INT = 0;
    TextView quantityTextView;
int aa = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        FloatingActionButton fab = findViewById(R.id.FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewActivity.this, EditorActivity.class));
            }
        });

        ListView listView = findViewById(R.id.listView);
        quantityTextView = findViewById(R.id.quantity);

        phoneCursorAdapter = new phoneCursorAdapter(this, null);
        View emptyView = findViewById(R.id.emptyListView);
        listView.setEmptyView(emptyView);
        listView.setAdapter(phoneCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String sNumber = cursor.getString(cursor.getColumnIndexOrThrow(PhoneEntry.COLUMN_SUPPLIER_NUMBER));

                editOrOrder(id,sNumber);
            }
        });
        getLoaderManager().initLoader(LOADER_INT, null, this);

    }

    private void editOrOrder(final long id , final String number ){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setItems(R.array.order_edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                        startActivity(call);
                        break;
                    case 1:
                        Uri currentId = ContentUris.withAppendedId(phoneContract.PhoneEntry.CONTENT_URI , id);
                        Intent intent = new Intent(ViewActivity.this , EditorActivity.class);
                        intent.setData(currentId);
                        startActivity(intent);


                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insertt_dummy_data:
                insertDummy();
                return true;
            case R.id.delteAll:
                deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        if(PhoneEntry.CONTENT_URI == null){
            Toast.makeText(this, "You don't have any items to delete!", Toast.LENGTH_SHORT).show();
        }else{
            getContentResolver().delete(PhoneEntry.CONTENT_URI,null,null);
        }
    }

    private void insertDummy() {

        ContentValues values = new ContentValues();

        values.put(phoneContract.PhoneEntry.COLUMN_COMPANY_PHONE_NAME, "SAMSUNG");
        values.put(phoneContract.PhoneEntry.COLUMN_PHONE_MODEL, "Note 8");
        values.put(phoneContract.PhoneEntry.COLUMN_PHONE_QUANTITY, "120");
        values.put(phoneContract.PhoneEntry.COLUMN_PHONE_PRICE, "260");
        values.put(phoneContract.PhoneEntry.COLUMN_SUPPLIER_NAME,"Nasser");
        values.put(phoneContract.PhoneEntry.COLUMN_SUPPLIER_NUMBER, "005521647");
        values.put(PhoneEntry.COLUMN_PRODUCT_IMAGE,"content://media/external/images/media/59610");

        getContentResolver().insert(PhoneEntry.CONTENT_URI, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PhoneEntry._ID,
                PhoneEntry.COLUMN_COMPANY_PHONE_NAME,
                PhoneEntry.COLUMN_PHONE_MODEL,
                PhoneEntry.COLUMN_PHONE_QUANTITY,
                PhoneEntry.COLUMN_PHONE_PRICE,
                PhoneEntry.COLUMN_PRODUCT_IMAGE,
                PhoneEntry.COLUMN_SUPPLIER_NUMBER

        };
        return new CursorLoader(this,
                PhoneEntry.CONTENT_URI,
                projection
                , null
                , null
                , null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        phoneCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        phoneCursorAdapter.swapCursor(null);

    }

}
