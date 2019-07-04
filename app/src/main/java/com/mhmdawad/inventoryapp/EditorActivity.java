package com.mhmdawad.inventoryapp;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mhmdawad.inventoryapp.database.phoneContract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mCompanyNameEditText;
    private EditText mModelNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierNumberEditText;
    ImageButton decreaseQuantityImageButton;
    ImageButton increaseQuantityImageButton;
    Uri currentUri;
    int numberOfQuantity;
    private ImageView browseImageView;
    private TextView textLoad;
    private String imagePath;
    private static final int INTENT_REQUEST = 100;
    private boolean mProductHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    private static final int EXISTING_PET_LOADER = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && requestCode == INTENT_REQUEST && data != null) {
            Uri imageUri = data.getData();
            InputStream inputStream;
            imagePath = String.valueOf(imageUri);

            try {
                assert imageUri != null;
                inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                browseImageView.setImageBitmap(image);
                textLoad.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Cannot find image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        currentUri = intent.getData();
        if (currentUri == null) {
            setTitle("Add new Product");
        } else {
            setTitle("Edit Product");
            invalidateOptionsMenu();
        }

        textLoad = findViewById(R.id.loadTextView);
        browseImageView = findViewById(R.id.imageButton);
        browseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                File imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String imageDirectoryPath = imageDirectory.getPath();
                Uri data = Uri.parse(imageDirectoryPath);
                intent.setDataAndType(data, "image/*");
                startActivityForResult(intent, INTENT_REQUEST);
            }
        });

        mCompanyNameEditText = findViewById(R.id.cName);
        mModelNameEditText = findViewById(R.id.mName);
        mQuantityEditText = findViewById(R.id.nQuantity);
        mPriceEditText = findViewById(R.id.price);
        mSupplierNameEditText = findViewById(R.id.sName);
        mSupplierNumberEditText = findViewById(R.id.sNumber);
        increaseQuantityImageButton = findViewById(R.id.inc);
        decreaseQuantityImageButton = findViewById(R.id.dec);



        increaseQuantityImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfQuantity = Integer.parseInt(mQuantityEditText.getText().toString().trim());
                mQuantityEditText.setText(String.valueOf(++numberOfQuantity));

            }
        });
        decreaseQuantityImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfQuantity = Integer.parseInt(mQuantityEditText.getText().toString().trim());
                mQuantityEditText.setText(String.valueOf(--numberOfQuantity));

            }
        });


        mCompanyNameEditText.setOnTouchListener(mTouchListener);
        mModelNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierNumberEditText.setOnTouchListener(mTouchListener);
        browseImageView.setOnTouchListener(mTouchListener);

        getLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
    }


    private void saveProduct() {
        String sCompanyName = mCompanyNameEditText.getText().toString().trim();
        String sModelName = mModelNameEditText.getText().toString().trim();
        String sQuantity = mQuantityEditText.getText().toString().trim();
        String sPrice = mPriceEditText.getText().toString().trim();
        String sSupplierName = mSupplierNameEditText.getText().toString().trim();
        String sSupplierNumber = mSupplierNumberEditText.getText().toString().trim();

        if (TextUtils.isEmpty(sCompanyName) || TextUtils.isEmpty(sModelName) || TextUtils.isEmpty(sQuantity) ||
                TextUtils.isEmpty(sPrice) || TextUtils.isEmpty(sSupplierName) || TextUtils.isEmpty(sSupplierNumber)) {
            Toast.makeText(this, "Please Filling up All Fields First!", Toast.LENGTH_SHORT).show();
        } else {
            if (currentUri == null && TextUtils.isEmpty(sCompanyName) && TextUtils.isEmpty(sModelName)
                    && TextUtils.isEmpty(sQuantity) && TextUtils.isEmpty(sPrice)
                    && TextUtils.isEmpty(sSupplierName) && TextUtils.isEmpty(sSupplierNumber)) {
                return;
            }
            ContentValues values = new ContentValues();
            values.put(phoneContract.PhoneEntry.COLUMN_COMPANY_PHONE_NAME, sCompanyName);
            values.put(phoneContract.PhoneEntry.COLUMN_PHONE_MODEL, sModelName);
            values.put(phoneContract.PhoneEntry.COLUMN_PHONE_QUANTITY, sQuantity);
            values.put(phoneContract.PhoneEntry.COLUMN_PHONE_PRICE, sPrice);
            values.put(phoneContract.PhoneEntry.COLUMN_SUPPLIER_NAME, sSupplierName);
            values.put(phoneContract.PhoneEntry.COLUMN_SUPPLIER_NUMBER, sSupplierNumber);
            values.put(phoneContract.PhoneEntry.COLUMN_PRODUCT_IMAGE, imagePath);


            if (currentUri == null) {

                Uri newUri = getContentResolver().insert(phoneContract.PhoneEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, "Error with saving Product!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Saving!", Toast.LENGTH_SHORT).show();
                }
            } else {

                int rowNumber = getContentResolver().update(currentUri, values, null, null);
                if (rowNumber == 0) {
                    Toast.makeText(this, "Error with updating Product!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        }

    }

    private void deleteProduct() {
        getContentResolver().delete(currentUri, null, null);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (currentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.deleteItem);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveItem:
                saveProduct();
                return true;
            case R.id.deleteItem:
                deleteProduct();
                finish();
                return true;
            case android.R.id.home:

                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }


                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };


                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }


        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };


        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                phoneContract.PhoneEntry._ID,
                phoneContract.PhoneEntry.COLUMN_COMPANY_PHONE_NAME,
                phoneContract.PhoneEntry.COLUMN_PHONE_MODEL,
                phoneContract.PhoneEntry.COLUMN_PHONE_PRICE,
                phoneContract.PhoneEntry.COLUMN_PHONE_QUANTITY,
                phoneContract.PhoneEntry.COLUMN_SUPPLIER_NAME,
                phoneContract.PhoneEntry.COLUMN_SUPPLIER_NUMBER,
                phoneContract.PhoneEntry.COLUMN_PRODUCT_IMAGE
        };

        CursorLoader cursorLoader;
        if (currentUri == null) {
            cursorLoader = new CursorLoader(this,
                    phoneContract.PhoneEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
        } else {
            cursorLoader = new CursorLoader(this,
                    currentUri,
                    projection,
                    null,
                    null,
                    null);
        }

        return cursorLoader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (currentUri != null) {
            if (data.moveToFirst()) {

                int companyNameColumnIndex = data.getColumnIndex(phoneContract.PhoneEntry.COLUMN_COMPANY_PHONE_NAME);
                int modelNameColumnIndex = data.getColumnIndex(phoneContract.PhoneEntry.COLUMN_PHONE_MODEL);
                int quantityColumnIndex = data.getColumnIndex(phoneContract.PhoneEntry.COLUMN_PHONE_QUANTITY);
                int priceColumnIndex = data.getColumnIndex(phoneContract.PhoneEntry.COLUMN_PHONE_PRICE);
                int supplierNameColumnIndex = data.getColumnIndex(phoneContract.PhoneEntry.COLUMN_SUPPLIER_NAME);
                int supplierNumberColumnIndex = data.getColumnIndex(phoneContract.PhoneEntry.COLUMN_SUPPLIER_NUMBER);
                int imageColumnIndex = data.getColumnIndex(phoneContract.PhoneEntry.COLUMN_PRODUCT_IMAGE);


                String companyName = data.getString(companyNameColumnIndex);
                String modelName = data.getString(modelNameColumnIndex);
                numberOfQuantity = data.getInt(quantityColumnIndex);
                double price = data.getInt(priceColumnIndex);
                String supplierName = data.getString(supplierNameColumnIndex);
                String supplierNumber = data.getString(supplierNumberColumnIndex);
                imagePath = data.getString(imageColumnIndex);

                InputStream inputStream;
                Uri imageUri = Uri.parse(imagePath);

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap bitmapImage = BitmapFactory.decodeStream(inputStream);

                    browseImageView.setImageBitmap(bitmapImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                mCompanyNameEditText.setText(companyName);
                mModelNameEditText.setText(modelName);
                mQuantityEditText.setText(String.valueOf(numberOfQuantity));
                mPriceEditText.setText(String.valueOf(price));
                mSupplierNameEditText.setText(supplierName);
                mSupplierNumberEditText.setText(supplierNumber);
            }

        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
