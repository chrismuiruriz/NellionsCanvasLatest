package com.nellions.nellionscanvas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nellions.nellionscanvas.adapters.CategoryTypeSpinnerAdapter;
import com.nellions.nellionscanvas.database.DatabaseHelper;
import com.nellions.nellionscanvas.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class NewRoomActivity extends AppCompatActivity {

    Spinner categoryTypeSpinner;
    List<AppModel> appModelListType = new ArrayList<AppModel>();
    CategoryTypeSpinnerAdapter categoryTypeSpinnerAdapter;
    String selectedType;
    EditText roomName;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHelper = new DatabaseHelper(NewRoomActivity.this);

        roomName = (EditText) findViewById(R.id.name);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRoom();
            }
        });
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpTypeSpinner();
        getTypes();
    }

    /* set up category type spinner */
    public void setUpTypeSpinner() {
        categoryTypeSpinner = (Spinner) findViewById(R.id.type);

        categoryTypeSpinnerAdapter = new CategoryTypeSpinnerAdapter(NewRoomActivity.this,
                android.R.layout.simple_spinner_item, appModelListType);

        categoryTypeSpinner.setAdapter(categoryTypeSpinnerAdapter);

        categoryTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AppModel appModel = categoryTypeSpinnerAdapter.getItem(i);
                selectedType = appModel.getCategoryTypeNumber();
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

    /* get types */
    public void getTypes() {
        AppModel appModel = new AppModel();
        appModel.setCategoryType("House Move");
        appModel.setCategoryTypeNumber("1");
        appModelListType.add(appModel);

        AppModel appModel1 = new AppModel();
        appModel1.setCategoryType("Office Move");
        appModel1.setCategoryTypeNumber("2");
        appModelListType.add(appModel1);
        categoryTypeSpinnerAdapter.notifyDataSetChanged();
    }

    public void saveRoom() {

        if (roomName.getText().toString().equals("")) {
            Toast.makeText(NewRoomActivity.this, "Name is missing!", Toast.LENGTH_LONG).show();
            return;
        }

        if (databaseHelper.saveNewRoom(selectedType, roomName.getText().toString(), 1)) {
            Toast.makeText(NewRoomActivity.this, "Room/Department Saved successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(NewRoomActivity.this, "oops! Room exists!", Toast.LENGTH_LONG).show();
        }

    }

}
