package com.mhmdawad.inventoryapp.database;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mhmdawad.inventoryapp.EditorActivity;
import com.mhmdawad.inventoryapp.R;
import com.mhmdawad.inventoryapp.ViewActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class phoneCursorAdapter extends CursorAdapter {


    private final Context context;
    public String supplierNumberValue;

    public phoneCursorAdapter(ViewActivity context, Cursor c) {
        super(context, c, 0);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView companyName = view.findViewById(R.id.company);
        TextView modelName = view.findViewById(R.id.model);
        TextView quantity = view.findViewById(R.id.quantity);
        Button sellButton = view.findViewById(R.id.sellButton);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView price = view.findViewById(R.id.price);

        int idColumnIndex = cursor.getColumnIndex(phoneContract.PhoneEntry._ID);
        int companyNameColumnIndex = cursor.getColumnIndex(phoneContract.PhoneEntry.COLUMN_COMPANY_PHONE_NAME);
        int modelNameColumnIndex = cursor.getColumnIndex(phoneContract.PhoneEntry.COLUMN_PHONE_MODEL);
        int quantityColumnIndex = cursor.getColumnIndex(phoneContract.PhoneEntry.COLUMN_PHONE_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(phoneContract.PhoneEntry.COLUMN_PHONE_PRICE);
        int imageColumnIndex = cursor.getColumnIndex(phoneContract.PhoneEntry.COLUMN_PRODUCT_IMAGE);


        final int idValue = cursor.getInt(idColumnIndex);
        final int quantityValue = cursor.getInt(quantityColumnIndex);

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity(idValue, quantityValue);
            }
        });




        companyName.setText(cursor.getString(companyNameColumnIndex));
        modelName.setText(cursor.getString(modelNameColumnIndex));
        quantity.setText(cursor.getString(quantityColumnIndex));
        price.setText(cursor.getString(priceColumnIndex) + " $");
        imageView.setImageBitmap(showImage(cursor, imageColumnIndex));

        if (quantity.getText().toString().equals("0")) {
            quantity.setTextColor(Color.parseColor("#FA0A12"));
        }
    }

    private Bitmap showImage(Cursor cursor, int imageColumnIndex) {
        InputStream inputStream;
        Uri imageUri = Uri.parse(cursor.getString(imageColumnIndex));

        try {
            inputStream = context.getContentResolver().openInputStream(imageUri);
            return BitmapFactory.decodeStream(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }




    private void decreaseQuantity(int columnId, int quantity) {

        if (quantity > 0)
            quantity--;

        else{
            quantity = 0;
            Toast.makeText(context, "This Product is finished!", Toast.LENGTH_SHORT).show();
        }

        ContentValues values = new ContentValues();
        values.put(phoneContract.PhoneEntry.COLUMN_PHONE_QUANTITY, quantity);

        Uri id = ContentUris.withAppendedId(phoneContract.PhoneEntry.CONTENT_URI, columnId);
        context.getContentResolver().update(id, values, null, null);

    }

}
