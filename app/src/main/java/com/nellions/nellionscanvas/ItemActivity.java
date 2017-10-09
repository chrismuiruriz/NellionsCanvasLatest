package com.nellions.nellionscanvas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.nellions.nellionscanvas.adapters.MoveItemAdapter;
import com.nellions.nellionscanvas.database.DatabaseHelper;
import com.nellions.nellionscanvas.model.AppModel;
import com.nellions.nellionscanvas.model.AppUrl;
import com.nellions.nellionscanvas.model.Util;
import com.nellions.nellionscanvas.utils.CustomJsonRequest;
import com.nellions.nellionscanvas.utils.CustomRequest;
import com.nellions.nellionscanvas.utils.Preferences;
import com.nellions.nellionscanvas.utils.VolleyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemActivity extends AppCompatActivity {

    private RecyclerView.Adapter moveItemAdapter;
    private List<AppModel> appModelList = new ArrayList<AppModel>();
    Preferences preferences;
    String userId, userName, moveId, category_id, clientName, category_name, category_code;
    ProgressBar progressBar;
    AppUrl restGetItems, restGetTotalVolume, restSaveItems, restGetNextCategory, restGetPrevCategory;
    Menu menu;
    Button nextRoomBtn, arrivedBtn;
    public DatabaseHelper databaseHelper;
    static int CAMERA_REQUEST = 1888;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        preferences = new Preferences();
        databaseHelper = new DatabaseHelper(ItemActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("CLIENT_NAME") + "/" + getIntent().getStringExtra("CATEGORY_NAME"));

        restGetItems = new AppUrl("apiGetItems");
        restGetTotalVolume = new AppUrl("apiGetTotalVolume");
        restSaveItems = new AppUrl("apiSaveItems");
        restGetNextCategory = new AppUrl("apiGetNextCategoryItems");
        restGetPrevCategory = new AppUrl("apiGetPreviousCategoryItems");

        userId = preferences.getPreferences(ItemActivity.this, Util.USER_PREF, Util.USER_ID);
        userName = preferences.getPreferences(ItemActivity.this, Util.USER_PREF, Util.USER_NAME);

        moveId = getIntent().getStringExtra("MOVE_ID");
        category_id = getIntent().getStringExtra("CATEGORY_ID");
        clientName = getIntent().getStringExtra("CLIENT_NAME");
        category_name = getIntent().getStringExtra("CATEGORY_NAME");
        category_code = getIntent().getStringExtra("CATEGORY_CODE");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG.setAction("Action", null).show();
                showNewItemDialog();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.move_item_recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        moveItemAdapter = new MoveItemAdapter(appModelList, ItemActivity.this);
        recyclerView.setAdapter(moveItemAdapter);

        getItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    //get the categories
    public void getItems() {
        for (int i = 0; i < databaseHelper.getCategoryItems(Integer.parseInt(category_code)).size(); i++) {
            AppModel appModel = new AppModel();
            appModel.setI_moveId(moveId);
            appModel.setI_userId(userId);
            appModel.setI_name(databaseHelper.getCategoryItems(Integer.parseInt(category_code)).get(i).getI_name());
            appModel.setI_id(databaseHelper.getCategoryItems(Integer.parseInt(category_code)).get(i).getI_id());
            if (databaseHelper.getSurveyItemQuantityByName(databaseHelper.getCategoryItems(Integer.parseInt(category_code)).get(i).getI_name(), category_name, moveId) != null) {
                appModel.setI_vol(databaseHelper.getSurveyItemVolumeByName(databaseHelper.getCategoryItems(Integer.parseInt(category_code)).get(i).getI_name(), category_name, moveId));
                appModel.setI_quantity(databaseHelper.getSurveyItemQuantityByName(databaseHelper.getCategoryItems(Integer.parseInt(category_code)).get(i).getI_name(), category_name, moveId));
            } else {
                appModel.setI_vol(databaseHelper.getCategoryItems(Integer.parseInt(category_code)).get(i).getI_vol());
                appModel.setI_quantity("");
            }
            appModel.setI_total(databaseHelper.getCategoryItems(Integer.parseInt(category_code)).get(i).getI_total());
            appModel.setI_categoryId(databaseHelper.getCategoryItems(Integer.parseInt(category_code)).get(i).getI_categoryId());
            appModelList.add(appModel);
        }
        moveItemAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        this.menu = menu;
        final MenuItem totalVolumeMenu = menu.findItem(R.id.action_total_volume);
        totalVolumeMenu.setTitle(Util.round(String.valueOf(databaseHelper.getTotalVolume(moveId)), 2));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final MenuItem totalVolumeMenu = menu.findItem(R.id.action_total_volume);

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        if (id == R.id.action_save) {
            //Showing the progress dialog
            final ProgressDialog loading = ProgressDialog.show(this, "Saving...", "Please wait...", false, false);
            //loop through the items
            for (int i = 0; i < appModelList.size(); i++) {
                AppModel appModel = appModelList.get(i);
                if (!appModel.getI_quantity().equals("")) {
                    databaseHelper.saveSurveyItems(appModel.getI_moveId(), appModel.getI_id(), appModel.getI_name(), Double.parseDouble(appModel.getI_vol()), Integer.parseInt(appModel.getI_quantity()), Double.parseDouble(appModel.getI_total()), Integer.parseInt(appModel.getI_categoryId()), Integer.parseInt(appModel.getI_userId()), "2016-12-31", category_name);
                }
                if (i == appModelList.size() - 1) {
                    loading.dismiss();
                }
            }
            Toast.makeText(ItemActivity.this, "Saved", Toast.LENGTH_LONG).show();
            totalVolumeMenu.setTitle(Util.round(String.valueOf(databaseHelper.getTotalVolume(moveId)), 2));
        }

        if (id == R.id.action_submmit) {
            Intent intent = new Intent(ItemActivity.this, ViewSummaryActivity.class);
            intent.putExtra("MOVE_ID", moveId);
            intent.putExtra("CLIENT_NAME", getIntent().getStringExtra("CLIENT_NAME"));
            startActivity(intent);
        }

        if (id == R.id.action_media) {

            Intent intent = new Intent(ItemActivity.this, MediaActivity.class);
            intent.putExtra("MOVE_ID", moveId);
            intent.putExtra("CATEGORY_ID", category_id);
            intent.putExtra("CLIENT_NAME", clientName);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //new item dialog
    public void showNewItemDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ItemActivity.this);
        View promptView = layoutInflater.inflate(R.layout.new_item_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText name = (EditText) promptView.findViewById(R.id.new_item_name);
        final EditText volume = (EditText) promptView.findViewById(R.id.new_item_volume);
        final EditText number = (EditText) promptView.findViewById(R.id.new_item_qunatity);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (!name.getText().toString().equals("")) {
                            AppModel appModel = new AppModel();
                            appModel.setI_moveId(moveId);
                            appModel.setI_userId(userId);
                            appModel.setI_name(name.getText().toString());
                            appModel.setI_id(Double.toString(appModelList.size() + Math.random() * 100));
                            appModel.setI_vol(volume.getText().toString());
                            appModel.setI_quantity(number.getText().toString());
                            appModel.setI_total("0");
                            appModel.setI_categoryId(category_id);
                            appModelList.add(appModel);
                            moveItemAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Item Saved", Toast.LENGTH_LONG).show();
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

    //save items
    public void getTotalVolume() {
        final MenuItem totalVolumeMenu = menu.findItem(R.id.action_total_volume);

        String tag_json_obj = "get_total_request"; /* tag used to cancel the request */
        String getTotalUrl = restGetTotalVolume.getCompleteUrl(); /* login rest url */

        /* this are the params to post */
        Map<String, String> params = new HashMap<String, String>();
        params.put("move_id", moveId);
        params.put("secret_code", "nellions");

        /* use volley customRequest class to handle requests */
        CustomRequest getTotalVolRequest = new CustomRequest(Request.Method.POST, getTotalUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //handle the response in a new thread
                        try {
                            if (response.getString("total_volume").equals("null")) {
                                totalVolumeMenu.setTitle(Util.round("0", 2));
                            } else {
                                totalVolumeMenu.setTitle(Util.round(response.getString("total_volume"), 2));
                            }
                        } catch (JSONException error) {
                            Log.e("NELLIONS", "json exception on total volume", error);
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /* handle request error */
                Log.e("NELLIONS", "response error on get total volume", error);
            }
        });

        /* EXTENDS THE REQUEST TIMEOUT */
        int socketTimeout = 20000;// 20 secs
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getTotalVolRequest.setRetryPolicy(policy);

         /* add the request to the request queue */
        VolleyApplication.getInstance().addToRequestQueue(getTotalVolRequest, tag_json_obj);

    }

    //get category
    public void toolbarTitle(String category) {
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(category);
    }

    //save Items
    public void saveItems(String itemId, String itemName, String itemVolume, String itemQuantity) {

        String tag_json_obj = "save_item_request"; /* tag used to cancel the request */
        String saveItemUrl = restSaveItems.getCompleteUrl(); /* login rest url */

        /* this are the params to post */
        Map<String, String> params = new HashMap<String, String>();
        params.put("secret_code", "nellions");
        params.put("move_id", moveId);
        params.put("item_id", itemId);
        params.put("item_name", itemName);
        params.put("item_volume", itemVolume);
        params.put("item_quantity", itemQuantity);
        params.put("category_id", category_id);
        params.put("user_id", userId);

        /* use volley customRequest class to handle requests */
        CustomRequest saveItemRequest = new CustomRequest(Request.Method.POST, saveItemUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //handle the response in a new thread
                        try {
                            if (response.getString("STATUS").equals("SUCCESS")) {
                                Log.i("NELLIONS", "saved");
                                //Snackbar.make(progressBar, "items Recorded successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        } catch (JSONException error) {
                            Log.e("NELLIONS", "json exception on save item", error);
                        }
                        getTotalVolume();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                /* handle request error */
                Log.e("NELLIONS", "response error on save item", error);
            }
        });

        /* EXTENDS THE REQUEST TIMEOUT */
        int socketTimeout = 20000;// 20 secs
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        saveItemRequest.setRetryPolicy(policy);

         /* add the request to the request queue */
        VolleyApplication.getInstance().addToRequestQueue(saveItemRequest, tag_json_obj);

    }


    //on activity
    //on activity results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(ItemActivity.this, "Media Saved", Toast.LENGTH_LONG).show();
        }
    }

}
