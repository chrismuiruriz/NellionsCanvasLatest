package com.nellions.nellionscanvas;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.nellions.nellionscanvas.adapters.NewSurveySummaryAdapter;
import com.nellions.nellionscanvas.database.DatabaseHelper;
import com.nellions.nellionscanvas.model.AppModel;
import com.nellions.nellionscanvas.model.AppUrl;
import com.nellions.nellionscanvas.model.Util;
import com.nellions.nellionscanvas.utils.CustomJsonRequest;
import com.nellions.nellionscanvas.utils.CustomRequest;
import com.nellions.nellionscanvas.utils.VolleyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewSummaryActivity extends AppCompatActivity {

    private RecyclerView.Adapter summaryItemAdapter;
    private List<AppModel> appModelList = new ArrayList<AppModel>();
    private List<AppModel> appModelListNotSynced = new ArrayList<AppModel>();
    private List<AppModel> timestampsItems = new ArrayList<AppModel>();
    DatabaseHelper databaseHelper;
    String moveId, totalVolume, theSoftIssues, theHardIssues;
    TextView summaryTotalVolumeTv, summaryClientName;
    ProgressBar progressBar;
    AppUrl restSubmitSurvey;
    EditText softIssues, hardIssues;
    ListView listView;
    NewSurveySummaryAdapter newSurveySummaryAdapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        restSubmitSurvey = new AppUrl("apiUpdateMoves2");

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(ViewSummaryActivity.this);

        moveId = getIntent().getStringExtra("MOVE_ID");

        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.summary_recyclerview);
        listView = (ListView) findViewById(R.id.survey_summary_listview);
        summaryTotalVolumeTv = (TextView) findViewById(R.id.summary_total_volume);
        summaryClientName = (TextView) findViewById(R.id.summary_client_name);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        softIssues = (EditText) findViewById(R.id.soft_issues);
        hardIssues = (EditText) findViewById(R.id.hard_issues);


        newSurveySummaryAdapter = new NewSurveySummaryAdapter(ViewSummaryActivity.this, appModelList);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(newSurveySummaryAdapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppModel appModel = newSurveySummaryAdapter.getItem(i);
                showEditItemDialog(appModel.getS_surveyItemId(), appModel.getS_itemName(), appModel.getS_itemVolume(), appModel.getS_itemQuantity());
                return true;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        summaryClientName.setText(getIntent().getStringExtra("CLIENT_NAME"));
        getNotes();
        //getSummaryItems();
        //getSummaryItemsNotSynced();
        new MyAsyncTask().execute();
        Log.i("NELLIONS_N_SYNC", Integer.toString(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).size()));
        //databaseHelper.deleteItems();
    }

    //get summary item
    public void getSummaryItems() {

        for (int i = 0; i < databaseHelper.getSurveyItems(Integer.parseInt(moveId)).size(); i++) {
            AppModel appModel = new AppModel();
            if (!databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemQuantity().equals("0")) {
                Log.i("NELLIONZ", databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemQuantity());
                appModel.setS_surveyItemId(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_surveyItemId());
                appModel.setS_moveId(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_moveId());
                appModel.setS_itemId(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemId());
                appModel.setS_itemName(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemName());
                appModel.setS_itemVolume(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemVolume());
                appModel.setS_itemQuantity(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemQuantity());
                appModel.setS_itemTotal(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemTotal());
                appModel.setS_categoryId(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_categoryId());
                appModel.setS_idUser(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_idUser());
                appModel.setS_sync(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_sync());
                appModel.setS_categoryName(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_categoryName());
                appModelList.add(appModel);
            }
        }

        newSurveySummaryAdapter.notifyDataSetChanged();
        totalVolume = Util.round(String.valueOf(databaseHelper.getTotalVolume(moveId)), 2);
        summaryTotalVolumeTv.setText(totalVolume);
    }

    //get items not synced
    public void getSummaryItemsNotSynced() {
        for (int i = 0; i < databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).size(); i++) {
            AppModel appModel = new AppModel();
            appModel.setS_surveyItemId(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_surveyItemId());
            appModel.setS_moveId(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_moveId());
            appModel.setS_itemId(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_itemId());
            appModel.setS_itemName(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_itemName());
            appModel.setS_itemVolume(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_itemVolume());
            appModel.setS_itemQuantity(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_itemQuantity());
            appModel.setS_itemTotal(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_itemTotal());
            appModel.setS_categoryId(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_categoryId());
            appModel.setS_idUser(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_idUser());
            appModel.setS_sync(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_sync());
            appModel.setS_categoryName(databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).get(i).getS_categoryName());
            appModelListNotSynced.add(appModel);
        }

    }

    //get notes
    public void getNotes() {
        if (databaseHelper.checkIfNotesExists(Integer.parseInt(moveId)) >= 1) {
            softIssues.setText(Html.fromHtml("<bold>Soft Issues</bold>: ") + databaseHelper.getNotes(Integer.parseInt(moveId)).get(0).getN_softIssues());
            hardIssues.setText(Html.fromHtml("<bold>Hard Issues</bold>: ") + databaseHelper.getNotes(Integer.parseInt(moveId)).get(0).getN_hardIssues());
            theSoftIssues = databaseHelper.getNotes(Integer.parseInt(moveId)).get(0).getN_softIssues();
            theHardIssues = databaseHelper.getNotes(Integer.parseInt(moveId)).get(0).getN_hardIssues();

            return;
        }

        theSoftIssues = "";
        theHardIssues = "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_summary, menu);
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
        if (id == R.id.action_sync) {
            syncSurveyItems();
            return true;
        }
        if (id == R.id.action_submmit) {
            if (databaseHelper.checkIfAllitemsAreSynced(Integer.parseInt(moveId)) >= 1) {
                Toast.makeText(ViewSummaryActivity.this, "Some items are not synced!", Toast.LENGTH_LONG).show();
            } else {
                //submit survey
                showPromptDialog();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //sync items
    public void syncSurveyItems() {

        if (appModelListNotSynced == null) {
            return;
        }

        if (databaseHelper.getSurveyItemsNotSynced(Integer.parseInt(moveId)).size() <= 0) {
            Toast.makeText(ViewSummaryActivity.this, "Everything is synced!", Toast.LENGTH_SHORT).show();
            return;
        }
        String arrivalTime = databaseHelper.getStatusTime(moveId,"ARRIVED");
        String startTime = databaseHelper.getStatusTime(moveId,"STARTED");
        if(arrivalTime == null) {
           arrivalTime = "Not Set";
        }

        if(startTime == null) {
            startTime = "Not Set";
        }

        String tag_json_obj = "sync"; /* tag used to cancel the request */
        String getMoves_url = "https://nellions.co.ke/canvas/nellions_canvas_backend/sync.php";

        final ProgressDialog loading = ProgressDialog.show(this, "Syncing...", "Please wait...", false, false);

        /* param to post to the rest */
        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("secret_code", "nellions");
        mParam.put("sync_item", "yes");
        mParam.put("json_items", getItemsInJson().toString());
        mParam.put("move_id", moveId);
        mParam.put("timestamp_arrival", arrivalTime);
        mParam.put("timestamp_started", startTime);

        /* error response listener, will be called if any response error occurs */
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handle error
                loading.dismiss();

                Snackbar.make(progressBar, "Check your Internet connections!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.e("NELLIONS", "response error on sync survey items", error);
            }
        };

        /* used to retrieve the server response */
        Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Parsing json
                appModelList.clear();
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject obj = response.getJSONObject(i);
                        databaseHelper.updateItemStatus(Integer.parseInt(obj.getString("survey_item_id")), Integer.parseInt(obj.getString("sync")));
                        Toast.makeText(ViewSummaryActivity.this, "Sync successful", Toast.LENGTH_LONG).show();
                    } catch (JSONException error) {
                        Log.e("NELLIONS", "json exception on sync survey items", error);
                        Toast.makeText(ViewSummaryActivity.this, "Check your Internet connections!", Toast.LENGTH_LONG).show();
                    }
                }
                loading.dismiss();
                getSummaryItems();
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

    public JSONArray getItemsInJson() {

        JSONObject jObject = new JSONObject();// main object
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray

        for (int i = 0; i < appModelListNotSynced.size(); i++) {
            JSONObject jGroup = new JSONObject();// /sub Object
            try {
                jGroup.put("survey_item_id", appModelListNotSynced.get(i).getS_surveyItemId());
                jGroup.put("move_id", appModelListNotSynced.get(i).getS_moveId());
                jGroup.put("item_id", appModelListNotSynced.get(i).getS_itemId());
                jGroup.put("item_name", appModelListNotSynced.get(i).getS_itemName());
                jGroup.put("item_volume", appModelListNotSynced.get(i).getS_itemVolume());
                jGroup.put("item_quantity", appModelListNotSynced.get(i).getS_itemQuantity());
                jGroup.put("item_total", appModelListNotSynced.get(i).getS_itemTotal());
                jGroup.put("category_id", appModelListNotSynced.get(i).getS_categoryId());
                jGroup.put("user_id", appModelListNotSynced.get(i).getS_idUser());
                jGroup.put("category_name", appModelListNotSynced.get(i).getS_categoryName());
                jArray.put(jGroup);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jArray;
    }

    //submit survey
    public void submitSurvey() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Submitting...", "Please wait...", false, false);

        String tag_json_obj = "submit_request"; /* tag used to cancel the request */
        String loginUrl = restSubmitSurvey.getCompleteUrl(); /* login rest url */

        /* this are the params to post */
        Map<String, String> params = new HashMap<String, String>();
        params.put("move_id", moveId);
        params.put("status_id", "1a");
        params.put("volume", totalVolume);
        params.put("soft_issues", theSoftIssues);
        params.put("hard_issues", theHardIssues);
        params.put("secret_code", "nellions");

        /* use volley customRequest class to handle requests */
        CustomRequest loginRequest = new CustomRequest(Request.Method.POST, loginUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //handle the response in a new thread
                        try {
                            loading.dismiss();
                            if (response.getString("STATUS").equals("SUCCESS")) {
                                Toast.makeText(ViewSummaryActivity.this, "Survey Submitted successfully!", Toast.LENGTH_SHORT).show();
                                //delete items
                                deleteSubmittedSurveyItems(moveId);
                            } else {
                                Toast.makeText(ViewSummaryActivity.this, "An Error occurred! Try Again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException error) {
                            loading.dismiss();
                            Toast.makeText(ViewSummaryActivity.this, "An Error occurred! Try Again", Toast.LENGTH_LONG).show();
                            Log.e("NELLIONS", "json exception on login", error);
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                /* handle request error */
                Toast.makeText(ViewSummaryActivity.this, "Check your Network connections!", Toast.LENGTH_LONG).show();
                Log.e("NELLIONS", "response error on submit survey", error);
            }
        });

        /* EXTENDS THE REQUEST TIMEOUT */
        int socketTimeout = 20000;// 20 secs
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        loginRequest.setRetryPolicy(policy);

         /* add the request to the request queue */
        VolleyApplication.getInstance().addToRequestQueue(loginRequest, tag_json_obj);
    }

    //show prompt dialog
    public void showPromptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSummaryActivity.this);

        builder.setTitle("Submit Summary");

        builder.setMessage("This action cannot be undone!");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                submitSurvey();
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

    //edit item
    public void showEditItemDialog(final String itemId, String itemName, String itemVolume, final String itemQuantity) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ViewSummaryActivity.this);
        View promptView = layoutInflater.inflate(R.layout.edit_item_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewSummaryActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText name = (EditText) promptView.findViewById(R.id.edit_item_name);
        name.setText(itemName);
        final EditText volume = (EditText) promptView.findViewById(R.id.edit_item_volume);
        volume.setText(itemVolume);
        final EditText number = (EditText) promptView.findViewById(R.id.edit_item_qunatity);
        number.setText(itemQuantity);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        appModelListNotSynced.clear();
                        appModelList.clear();
                        if (number.getText().toString().equals("")) {
                            databaseHelper.updateItem(itemId, volume.getText().toString(), "0");
                        } else {
                            databaseHelper.updateItem(itemId, volume.getText().toString(), number.getText().toString());
                        }
                        new MyAsyncTask().execute();
                        //getSummaryItems();
                        //getSummaryItemsNotSynced();

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

    public class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ViewSummaryActivity.this);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading ...");
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... strings) { // run time intensive task in separate thread
            for (int i = 0; i < databaseHelper.getSurveyItems(Integer.parseInt(moveId)).size(); i++) {
                AppModel appModel = new AppModel();
                if (!databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemQuantity().equals("0")) {
                    Log.i("NELLIONZ", databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemQuantity());
                    appModel.setS_surveyItemId(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_surveyItemId());
                    appModel.setS_moveId(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_moveId());
                    appModel.setS_itemId(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemId());
                    appModel.setS_itemName(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemName());
                    appModel.setS_itemVolume(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemVolume());
                    appModel.setS_itemQuantity(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemQuantity());
                    appModel.setS_itemTotal(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_itemTotal());
                    appModel.setS_categoryId(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_categoryId());
                    appModel.setS_idUser(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_idUser());
                    appModel.setS_sync(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_sync());
                    appModel.setS_categoryName(databaseHelper.getSurveyItems(Integer.parseInt(moveId)).get(i).getS_categoryName());
                    appModelList.add(appModel);
                }
            }
            getSummaryItemsNotSynced();
            return null;
        }

        protected void onPostExecute(String result) {
            newSurveySummaryAdapter.notifyDataSetChanged();
            totalVolume = Util.round(String.valueOf(databaseHelper.getTotalVolume(moveId)), 2);
            summaryTotalVolumeTv.setText(totalVolume);
            pd.dismiss();
        }
    }

    //delete submitted survey items
    public void deleteSubmittedSurveyItems(String moveId) {
        databaseHelper.deleteSubmittedMoveItems(moveId);
    }
}
