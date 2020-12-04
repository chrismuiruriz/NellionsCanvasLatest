package com.nellions.nellionscanvas;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nellions.nellionscanvas.adapters.RoomSpinnerAdapter;
import com.nellions.nellionscanvas.database.DatabaseHelper;
import com.nellions.nellionscanvas.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class NewItemActivity extends AppCompatActivity {

    Spinner categorySpinner;
    List<AppModel> appModelList = new ArrayList<AppModel>();
    RoomSpinnerAdapter roomSpinnerAdapter;
    String selectedType;
    EditText itemName, itemVolume;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(NewItemActivity.this);

        itemName = findViewById(R.id.name);
        itemVolume = findViewById(R.id.volume);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpSpinner();
        getRooms();
    }

    /* set up category  spinner */
    public void setUpSpinner() {
        categorySpinner = findViewById(R.id.rooms_spinner);

        roomSpinnerAdapter = new RoomSpinnerAdapter(NewItemActivity.this,
                android.R.layout.simple_spinner_item, appModelList);

        categorySpinner.setAdapter(roomSpinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AppModel appModel = roomSpinnerAdapter.getItem(i);
                selectedType = appModel.getRoomId();
                /*selectedSubjectName = studentDataModel.getSubjectName(); */
                Toast.makeText(getApplicationContext(), "\nItem Id: " + selectedType, Toast.LENGTH_LONG).show();
                /*studentDataModelListTopics.clear();
                getTopics(selectedSubjectId);
                */
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //get rooms
    public void getRooms() {
        for (int i = 0; i < databaseHelper.getAllCategories().size(); i++) {
            AppModel appModel = new AppModel();
            appModel.setRoomName(databaseHelper.getAllCategories().get(i).getC_name());
            appModel.setRoomId(databaseHelper.getAllCategories().get(i).getC_id());
            appModelList.add(appModel);
        }
        roomSpinnerAdapter.notifyDataSetChanged();
    }

    //save item
    public void saveItem() {

        if (itemName.getText().toString().equals("")) {
            Toast.makeText(NewItemActivity.this, "Name is missing!", Toast.LENGTH_LONG).show();
            return;
        }

        if (itemVolume.getText().toString().equals("")) {
            Toast.makeText(NewItemActivity.this, "Volume is missing!", Toast.LENGTH_LONG).show();
            return;
        }

        if (databaseHelper.saveNewItem(itemName.getText().toString(), Double.parseDouble(itemVolume.getText().toString()), Integer.parseInt(selectedType), 1)) {
            Toast.makeText(NewItemActivity.this, itemName.getText().toString() + " saved successfully", Toast.LENGTH_LONG).show();
        }

    }
}
