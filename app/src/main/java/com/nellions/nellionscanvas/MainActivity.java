package com.nellions.nellionscanvas;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.nellions.nellionscanvas.adapters.MoveSurveyAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //private RecyclerView.Adapter moveSurveyAdapter;
    public MoveSurveyAdapter moveSurveyAdapter;
    private List<AppModel> appModelList = new ArrayList<AppModel>();
    Preferences preferences;
    String userId, userName;
    ProgressBar progressBar;
    AppUrl restGetMoves;
    public SearchView mSearchView;
    public MenuItem searchMenuItem;
    ListView listView;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = new Preferences();
        databaseHelper = new DatabaseHelper(MainActivity.this);

        if (preferences.getPreferences(MainActivity.this, Util.USER_PREF, Util.USER_ID) == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        restGetMoves = new AppUrl("apiGetMoves");

        userId = preferences.getPreferences(MainActivity.this, Util.USER_PREF, Util.USER_ID);
        userName = preferences.getPreferences(MainActivity.this, Util.USER_PREF, Util.USER_NAME);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(userName);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pullData();
            }
        });

        listView = (ListView) findViewById(R.id.move_survey_recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        //recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true);

        moveSurveyAdapter = new MoveSurveyAdapter(MainActivity.this, appModelList);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setTextFilterEnabled(true);

        getMoves();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppModel appModel = moveSurveyAdapter.getItem(i);
                Log.i("NELLIONS", "ITEM CLICKED");
                Toast.makeText(MainActivity.this, "Move Id " + appModel.getM_moveId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("MOVE_ID", appModel.getM_moveId());
                intent.putExtra("MOVE_TYPE", appModel.getM_jobType());
                intent.putExtra("CLIENT_NAME", appModel.getM_clientName());
                startActivity(intent);
            }
        });

        listView.setAdapter(moveSurveyAdapter);
    }


    //fetch moves from server
    public void getMoves() {

        String tag_json_obj = "get_moves"; /* tag used to cancel the request */
        String getMoves_url = restGetMoves.getCompleteUrl();
        progressBar.setVisibility(View.VISIBLE);

        /* param to post to the rest */
        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("secret_code", "nellions");
        mParam.put("user_id", userId);

        /* error response listener, will be called if any response error occurs */
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handle error
                progressBar.setVisibility(View.GONE);

                Snackbar.make(progressBar, "Check your Internet connections!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.e("NELLIONS", "response error on get moves", error);
            }
        };

        /* used to retrieve the server response */
        Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Parsing json
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject obj = response.getJSONObject(i);
                        AppModel appModel = new AppModel();
                        appModel.setM_moveId(obj.getString("moveid"));
                        appModel.setM_clientCode(obj.getString("clientcode"));
                        appModel.setM_clientName(obj.getString("clientname"));
                        appModel.setM_description("Origin: " + obj.getString("origin_") + ", Destination: " + obj.getString("dest_") + "...");
                        appModel.setM_surveyDate(obj.getString("surveydate"));
                        appModel.setM_moveRep(obj.getString("moverep"));
                        appModel.setM_jobType(obj.getString("jobtype"));
                        appModelList.add(appModel);
                    } catch (JSONException error) {
                        Log.e("NELLIONS", "json exception on get moves", error);
                        Toast.makeText(MainActivity.this, "Check your Internet connections!", Toast.LENGTH_LONG).show();
                    }
                }

                progressBar.setVisibility(View.GONE);
                moveSurveyAdapter.notifyDataSetChanged();
            }
        };

        CustomJsonRequest aReq = new CustomJsonRequest(Request.Method.POST, getMoves_url, mParam, jsonArrayListener, errorListener);

        /* EXTENDS THE REQUEST TIMEOUT */
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        aReq.setRetryPolicy(policy);
        /* add the request to the request queue */
        VolleyApplication.getInstance().addToRequestQueue(aReq, tag_json_obj);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchMenuItem = menu.findItem(R.id.menu_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        //mSearchView.setOnQueryTextListener(listener);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("")) {
                    moveSurveyAdapter.resetData();
                }
                moveSurveyAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    moveSurveyAdapter.resetData();
                }
                moveSurveyAdapter.getFilter().filter(newText);
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
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_refresh) {
            appModelList.clear();
            getMoves();
            return true;
        }

        if (id == R.id.action_new_item) {
            Intent intent = new Intent(MainActivity.this, NewItemActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_sync) {
            Intent intent = new Intent(MainActivity.this, SyncronizeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* update local database */
    public void updateDatabase() {
        String tag_json_obj = "download_tests"; /* tag used to cancel the request */
        //String getMoves_url = "http://192.168.43.95/canvas/nellions_canvas_backend/database/sync.php";
        String getMoves_url = "https://nellions.co.ke/canvas/nellions_canvas_backend/database/sync.php";
        final ProgressDialog loading = ProgressDialog.show(this, "Updating...", "Please wait...", false, false);

        /* param to post to the rest */
        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("PULL", "1");

        /* error response listener, will be called if any response error occurs */
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handle error
                loading.dismiss();
                Log.e("NELLIONS", "response error on update database", error);
                Toast.makeText(MainActivity.this, "Check your internet connection!", Toast.LENGTH_LONG).show();
            }
        };

        /* used to retrieve the server response */
        Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject test = response.getJSONObject(i);
                        if (databaseHelper.insertNewRoom(test.getString("category_id"), test.getString("category_type"), test.getString("category_name"), 3)) {
                            for (int q = 0; q < test.getJSONArray("ITEMS").length(); q++) {
                                JSONObject item = test.getJSONArray("ITEMS").getJSONObject(q);
                                Log.i("NELLIONS", "ITEMS IS OK");
                                if (databaseHelper.insertNewItem(item.getString("item_id"), item.getString("item_name"), Double.parseDouble(item.getString("item_volume")), Integer.parseInt(item.getString("category_id")),item.getString("category_name"), 3)) {
                                    Log.i("ITEM INSERTED", "YES");
                                } else {
                                    Log.i("INSERT ITEM ERR", "NOT");
                                }
                            }
                        } else {
                            Log.i("ITEM INSERTED", "ROOM EXISTS");
                            /* add items */
                            for (int q = 0; q < test.getJSONArray("ITEMS").length(); q++) {
                                JSONObject item = test.getJSONArray("ITEMS").getJSONObject(q);
                                Log.i("NELLIONS", "ITEMS IS OK");
                                if (databaseHelper.insertNewItem(item.getString("item_id"), item.getString("item_name"), Double.parseDouble(item.getString("item_volume")), Integer.parseInt(item.getString("category_id")),item.getString("category_name"), 3)) {
                                    Log.i("ITEM INSERTED", "YES");
                                } else {
                                    Log.i("INSERT ITEM ERR", "NOT");
                                }
                            }
                        }
                    } catch (JSONException error) {
                        Log.e("NELLIONS", "json exception on update database", error);
                        Toast.makeText(MainActivity.this, "Nothing to update!", Toast.LENGTH_LONG).show();
                    }
                }
                loading.dismiss();

                Toast.makeText(MainActivity.this, "Database Updated...", Toast.LENGTH_LONG).show();
            }
        };

        CustomJsonRequest aReq = new CustomJsonRequest(Request.Method.POST, getMoves_url, mParam, jsonArrayListener, errorListener);

        /* EXTENDS THE REQUEST TIMEOUT */
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        aReq.setRetryPolicy(policy);
        /* add the request to the request queue */
        VolleyApplication.getInstance().addToRequestQueue(aReq, tag_json_obj);

    }

    public void pullData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Update Database");

        builder.setMessage("This action might take a while, do you want to proceed?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDatabase();
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

