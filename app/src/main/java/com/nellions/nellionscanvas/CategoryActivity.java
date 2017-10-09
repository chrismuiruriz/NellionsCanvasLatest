package com.nellions.nellionscanvas;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.nellions.nellionscanvas.adapters.NewRoomAdapter;
import com.nellions.nellionscanvas.database.DatabaseHelper;
import com.nellions.nellionscanvas.model.AppModel;
import com.nellions.nellionscanvas.model.AppUrl;
import com.nellions.nellionscanvas.model.Util;
import com.nellions.nellionscanvas.utils.CustomJsonRequest;
import com.nellions.nellionscanvas.utils.Preferences;
import com.nellions.nellionscanvas.utils.VolleyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    //private RecyclerView.Adapter moveCategoryAdapter;
    NewRoomAdapter moveCategoryAdapter;
    ListView listView;
    private List<AppModel> appModelList = new ArrayList<AppModel>();
    Preferences preferences;
    String userId, userName, moveId, moveType;
    ProgressBar progressBar;
    AppUrl restGetCategories;
    public DatabaseHelper databaseHelper;
    Menu menu;
    public MenuItem searchMenuItem;
    RelativeLayout relativeLayout;
    Button nextRoomBtn, arrivedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        preferences = new Preferences();
        databaseHelper = new DatabaseHelper(CategoryActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("CLIENT_NAME"));

        userId = preferences.getPreferences(CategoryActivity.this, Util.USER_PREF, Util.USER_ID);
        userName = preferences.getPreferences(CategoryActivity.this, Util.USER_PREF, Util.USER_NAME);

        moveId = getIntent().getStringExtra("MOVE_ID");
        moveType = getIntent().getStringExtra("MOVE_TYPE");
        restGetCategories = new AppUrl("apiGetCategories");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotepadDialog();
            }
        });

        listView = (ListView) findViewById(R.id.move_category_listview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        relativeLayout = (RelativeLayout) findViewById(R.id.linear);
        arrivedBtn = (Button) findViewById(R.id.btn_arrived);
        arrivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save timestamp
                DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm", Locale.ENGLISH);
                String date = df.format(Calendar.getInstance().getTime());
                databaseHelper.saveStatusTime(moveId,date,"ARRIVED");
                relativeLayout.setVisibility(View.GONE);
            }
        });

        if(databaseHelper.getStatusTime(moveId, "ARRIVED") == null) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.GONE);
        }

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        //recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true);

        moveCategoryAdapter = new NewRoomAdapter(CategoryActivity.this, appModelList);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setTextFilterEnabled(true);

        //getRooms();
        getCategories();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppModel appModel = moveCategoryAdapter.getItem(i);
                Intent intent = new Intent(CategoryActivity.this, ItemActivity.class);
                intent.putExtra("CATEGORY_ID", appModel.getC_id());
                intent.putExtra("CATEGORY_NAME", appModel.getC_name());
                intent.putExtra("CATEGORY_CODE", appModel.getC_code());
                intent.putExtra("MOVE_ID", moveId);
                intent.putExtra("CLIENT_NAME", getIntent().getStringExtra("CLIENT_NAME"));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AppModel appModel = moveCategoryAdapter.getItem(position);
                showDuplicateRoomDialog(appModel.getC_code(), appModel.getC_name());
                return true;
            }
        });


        listView.setAdapter(moveCategoryAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    //get the categories
    public void getCategories() {
        String jobType = "1";
        if (moveType.equals("Office Move")) {
            jobType = "2";
        }
        for (int i = 0; i < databaseHelper.getCategories(jobType).size(); i++) {
            AppModel appModel = new AppModel();
            appModel.setC_name(databaseHelper.getCategories(jobType).get(i).getC_name());
            appModel.setC_id(databaseHelper.getCategories(jobType).get(i).getC_id());
            appModel.setC_code(databaseHelper.getCategories(jobType).get(i).getC_code());
            appModelList.add(appModel);
        }
        moveCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        this.menu = menu;
        final MenuItem totalVolumeMenu = menu.findItem(R.id.action_total_volume);
        totalVolumeMenu.setTitle(Util.round(String.valueOf(databaseHelper.getTotalVolume(moveId)), 2));

        searchMenuItem = menu.findItem(R.id.menu_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("")) {
                    moveCategoryAdapter.resetData();
                }
                moveCategoryAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    moveCategoryAdapter.resetData();
                }
                moveCategoryAdapter.getFilter().filter(newText);
                return true;
            }
        });
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
            showBackPressPrompt();
            return true;
        }

        if (id == R.id.action_category) {
            showNewRoomDialog();
        }

        if (id == R.id.action_submmit) {
            Intent intent = new Intent(CategoryActivity.this, ViewSummaryActivity.class);
            intent.putExtra("MOVE_ID", moveId);
            intent.putExtra("CLIENT_NAME", getIntent().getStringExtra("CLIENT_NAME"));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //notepad dialog
    public void showNotepadDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(CategoryActivity.this);
        View promptView = layoutInflater.inflate(R.layout.notepad_custom_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CategoryActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText softIssues = (EditText) promptView.findViewById(R.id.soft_issues);
        final EditText hardIssues = (EditText) promptView.findViewById(R.id.hard_issues);

        if (databaseHelper.checkIfNotesExists(Integer.parseInt(moveId)) >= 1) {
            softIssues.append(databaseHelper.getNotes(Integer.parseInt(moveId)).get(0).getN_softIssues());
            hardIssues.setText(databaseHelper.getNotes(Integer.parseInt(moveId)).get(0).getN_hardIssues());
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        databaseHelper.saveNotes(Integer.parseInt(moveId), softIssues.getText().toString(), hardIssues.getText().toString());
                        Toast.makeText(getApplicationContext(), "Note Saved", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //show new room dialog
    public void showNewRoomDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(CategoryActivity.this);
        View promptView = layoutInflater.inflate(R.layout.new_category_item_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CategoryActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText roomName = (EditText) promptView.findViewById(R.id.room_name);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!roomName.getText().toString().equals("")) {
                            AppModel appModel = new AppModel();
                            appModel.setC_name(roomName.getText().toString());
                            appModel.setC_id(Integer.toString(appModelList.size() + (2 * 100)));
                            appModelList.add(appModel);
                            moveCategoryAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), roomName.getText().toString() + " Added", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //show duplicate room dialog
    public void showDuplicateRoomDialog(final String categoryCode, String categoryName) {
        LayoutInflater layoutInflater = LayoutInflater.from(CategoryActivity.this);
        View promptView = layoutInflater.inflate(R.layout.new_category_item_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CategoryActivity.this);
        alertDialogBuilder.setView(promptView);

        final TextView title = (TextView) promptView.findViewById(R.id.title);
        title.setText("DUPLICATE '"+categoryName+"'");
        final EditText roomName = (EditText) promptView.findViewById(R.id.room_name);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!roomName.getText().toString().equals("")) {
                            AppModel appModel = new AppModel();
                            appModel.setC_name(roomName.getText().toString().toUpperCase());
                            appModel.setC_id(Integer.toString(appModelList.size() + (2 * 100)));
                            appModel.setC_code(categoryCode);
                            appModelList.add(appModel);
                            moveCategoryAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), roomName.getText().toString() + " Added", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //get gamez


    @Override
    public void onBackPressed() {
        showBackPressPrompt();
    }

    //back press prompt
    public void showBackPressPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);

        builder.setTitle("Warning!");

        builder.setMessage("This action requires INTERNET, do you want to proceed?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CategoryActivity.this.finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
