package com.nellions.nellionscanvas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.nellions.nellionscanvas.database.DatabaseHelper;
import com.nellions.nellionscanvas.model.AppModel;
import com.nellions.nellionscanvas.utils.CustomJsonRequest;
import com.nellions.nellionscanvas.utils.VolleyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncronizeActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    private List<AppModel> appModelList = new ArrayList<AppModel>();
    private List<AppModel> appModelList2 = new ArrayList<AppModel>();
    ImageView syncRoomsImg, syncItemsImg;
    TextView roomsTitle, itemsTitle, photosTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncronize);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHelper = new DatabaseHelper(SyncronizeActivity.this);

        syncRoomsImg = (ImageView) findViewById(R.id.sync_rooms_btn);
        roomsTitle = (TextView) findViewById(R.id.sync_rooms);
        itemsTitle = (TextView) findViewById(R.id.sync_items);
        syncItemsImg = (ImageView) findViewById(R.id.sync_items_btn);

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

        getRooms();
        getItems();

        syncRoomsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appModelList.size() >= 1) {
                    syncRooms();
                }
            }
        });

        syncItemsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncItems();
            }
        });

        roomsTitle.setText("Sync Rooms (" + appModelList.size() + ")");
        itemsTitle.setText("Sync Items (" + appModelList2.size() + ")");

        Log.i("NELLIONS_SYNC_ITEMS", getItemsInJson().toString());
    }

    //get summary item
    public void getRooms() {
        for (int i = 0; i < databaseHelper.getRoomsNotSynced(1).size(); i++) {
            AppModel appModel = new AppModel();
            appModel.setC_id(databaseHelper.getRoomsNotSynced(1).get(i).getC_id());
            appModel.setC_name(databaseHelper.getRoomsNotSynced(1).get(i).getC_name());
            appModel.setC_type(databaseHelper.getRoomsNotSynced(1).get(i).getC_type());
            appModelList.add(appModel);
        }
    }

    //get items
    public void getItems() {
        for (int i = 0; i < databaseHelper.getRoomItemsNotSynced(1).size(); i++) {
            AppModel appModel = new AppModel();
            appModel.setI_id(databaseHelper.getRoomItemsNotSynced(1).get(i).getI_id());
            appModel.setI_name(databaseHelper.getRoomItemsNotSynced(1).get(i).getI_name());
            appModel.setI_vol(databaseHelper.getRoomItemsNotSynced(1).get(i).getI_vol());
            appModel.setI_quantity(databaseHelper.getRoomItemsNotSynced(1).get(i).getI_quantity());
            appModel.setI_categoryId(databaseHelper.getRoomItemsNotSynced(1).get(i).getI_categoryId());
            appModel.setI_categoryName(databaseHelper.getRoomItemsNotSynced(1).get(i).getI_categoryName());
            appModelList2.add(appModel);
        }
    }

    public void syncRooms() {
        String tag_json_obj = "sync"; /* tag used to cancel the request */
        String getMoves_url = "https://nellions.co.ke/canvas/nellions_canvas_backend/sync_rooms.php";

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Syncing...", "Please wait...", false, false);

        /* param to post to the rest */
        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("secret_code", "nellions");
        mParam.put("sync_room", "yes");
        mParam.put("json_rooms", getRoomsInJson().toString());

        /* error response listener, will be called if any response error occurs */
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.e("NELLIONS", "response error on sync rooms", error);
            }
        };

        /* used to retrieve the server response */
        Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Parsing json
                appModelList.clear();
                loading.dismiss();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        databaseHelper.updateRoomSyncStatus(Integer.parseInt(obj.getString("category_id")), Integer.parseInt(obj.getString("sync")));
                        Toast.makeText(SyncronizeActivity.this, "Rooms Synced successfully", Toast.LENGTH_LONG).show();
                    } catch (JSONException error) {
                        Log.e("NELLIONS", "json exception on sync rooms", error);
                        Toast.makeText(SyncronizeActivity.this, "Check your Internet connections!", Toast.LENGTH_LONG).show();
                    }
                }
                getRooms();
                roomsTitle.setText("Sync Rooms (" + appModelList.size() + ")");
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

    //sync items
    public void syncItems() {
        String tag_json_obj = "sync"; /* tag used to cancel the request */
        String getMoves_url = "https://nellions.co.ke/canvas/nellions_canvas_backend/sync_items.php";

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Syncing...", "Please wait...", false, false);

        /* param to post to the rest */
        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("secret_code", "nellions");
        mParam.put("sync_item", "yes");
        mParam.put("json_items", getItemsInJson().toString());

        /* error response listener, will be called if any response error occurs */
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.e("NELLIONS", "response error on sync items", error);
            }
        };

        /* used to retrieve the server response */
        Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Parsing json
                appModelList.clear();
                loading.dismiss();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        databaseHelper.updateRoomItemSyncStatus(Integer.parseInt(obj.getString("item_id")), Integer.parseInt(obj.getString("sync")));
                        Toast.makeText(SyncronizeActivity.this, "Items Synced successfully", Toast.LENGTH_LONG).show();
                    } catch (JSONException error) {
                        Log.e("NELLIONS", "json exception on sync items", error);
                        Toast.makeText(SyncronizeActivity.this, "Check your Internet connections!", Toast.LENGTH_LONG).show();
                    }
                }
                getItems();
                itemsTitle.setText("Sync Items (" + appModelList2.size() + ")");
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

    //get room into json
    public JSONArray getRoomsInJson() {

        JSONObject jObject = new JSONObject();// main object
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

        for (int i = 0; i < appModelList.size(); i++) {
            JSONObject jGroup = new JSONObject();// /sub Object
            try {
                jGroup.put("category_id", appModelList.get(i).getC_id());
                jGroup.put("category_name", appModelList.get(i).getC_name());
                jGroup.put("category_type", appModelList.get(i).getC_type());
                jArray.put(jGroup);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jArray;
    }

    //get items into json
    public JSONArray getItemsInJson() {

        JSONObject jObject = new JSONObject();// main object
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

        for (int i = 0; i < appModelList2.size(); i++) {
            JSONObject jGroup = new JSONObject();// /sub Object
            try {
                jGroup.put("item_id", appModelList2.get(i).getI_id());
                jGroup.put("item_name", appModelList2.get(i).getI_name());
                jGroup.put("item_volume", appModelList2.get(i).getI_vol());
                jGroup.put("item_quantity", appModelList2.get(i).getI_quantity());
                jGroup.put("item_total", appModelList2.get(i).getI_total());
                jGroup.put("category_id", appModelList2.get(i).getI_categoryId());
                jGroup.put("category_name", appModelList2.get(i).getI_categoryName());
                jArray.put(jGroup);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jArray;
    }

}
