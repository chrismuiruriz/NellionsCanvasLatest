package com.nellions.nellionscanvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nellions.nellionscanvas.adapters.PhotoViewAdapter;
import com.nellions.nellionscanvas.database.DatabaseHelper;
import com.nellions.nellionscanvas.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class PhotosViewActivity extends AppCompatActivity {


    List<AppModel> appModelList = new ArrayList<AppModel>();
    RecyclerView.Adapter photoViewAdapter;
    DatabaseHelper databaseHelper;
    String imageName, categoryId, moveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(PhotosViewActivity.this);
        moveId = getIntent().getStringExtra("MOVE_ID");
        categoryId = getIntent().getStringExtra("CATEGORY_ID");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        photoViewAdapter = new PhotoViewAdapter(appModelList);
        recyclerView.setAdapter(photoViewAdapter);

        //databaseHelper.deletePhotos();
        getPhotos();
    }

    public void getPhotos() {
        for (int i = 0; i < databaseHelper.getPhotos(moveId).size(); i++) {
            AppModel appModel = new AppModel();
            appModel.setPhotoImage(base64ToBitmap(databaseHelper.getPhotos(moveId).get(i).getPhotoBase64()));
            appModel.setPhotoName(databaseHelper.getPhotos(moveId).get(i).getPhotoName());
            appModelList.add(appModel);
            //Log.i("NELLIONS_PHOTOS", databaseHelper.getPhotos().get(i).getPhotoBase64()+" name"+databaseHelper.getPhotos().get(i).getPhotoName());
        }
        photoViewAdapter.notifyDataSetChanged();
    }

    public Bitmap base64ToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, 0);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_view, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
