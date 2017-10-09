package com.nellions.nellionscanvas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nellions.nellionscanvas.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MediaActivity extends AppCompatActivity {

    private static int CAMERA_REQUEST = 1888;
    ImageView imageView;
    String imageName, categoryId, moveId, clientName;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(MediaActivity.this);

        moveId = getIntent().getStringExtra("MOVE_ID");
        categoryId = getIntent().getStringExtra("CATEGORY_ID");
        clientName = getIntent().getStringExtra("CLIENT_NAME");

        imageView = (ImageView) findViewById(R.id.imageView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNameFromUser();
            }
        });
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File nellionsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Nellions");
                if (!nellionsFolder.exists()) {
                    if (nellionsFolder.mkdirs()) {
                        Log.i("NELLIONS", "COULD NOT CREATE FOLDER");
                    }
                }
                File file = new File(nellionsFolder + File.separator + "image" + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });
    }

    //on activity results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (CAMERA_REQUEST == 1888) {
                setBigImage("image");
            }
        }
    }

    //set image in imageView
    private void setImage(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(photo);
    }

    //get form
    private void getFormImage(String imageName) {
    }

    private void setBigImage(String imageName) {
        String nellions = Environment.getExternalStorageDirectory() + File.separator + "Nellions";
        File file = new File(nellions + File.separator + imageName + ".jpg");
        Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 640, 640);
        imageView.setImageBitmap(bitmap);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        if (id == R.id.action_galley) {
            Intent intent = new Intent(MediaActivity.this, PhotosViewActivity.class);
            intent.putExtra("MOVE_ID", moveId);
            intent.putExtra("CATEGORY_ID", categoryId);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //get base64 string from bitmap
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    //show name dialog
    private void getNameFromUser() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Photo Name");
        final EditText imageName = new EditText(this);
        alert.setView(imageName);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = imageName.getText().toString().trim();
                MediaActivity.this.imageName = value;
                if (!value.equals("") || !value.equals(" ")) {
                    savePhoto(value);
                } else {
                    Toast.makeText(MediaActivity.this, "Not Allowed!", Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }

    //save photo
    public void savePhoto(String name) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bmap = imageView.getDrawingCache();
        //Bitmap bmap = Bitmap.createBitmap(imageView.getWidth(),imageView.getHeight(),Bitmap.Config.ARGB_8888);
        if (databaseHelper.savePhoto(clientName+" ("+name+")", Integer.parseInt(categoryId), getStringImage(bmap), Integer.parseInt(moveId))) {
            Toast.makeText(MediaActivity.this, "Photo saved successfully", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(MediaActivity.this, "Photo Exists!", Toast.LENGTH_LONG).show();
    }

}
