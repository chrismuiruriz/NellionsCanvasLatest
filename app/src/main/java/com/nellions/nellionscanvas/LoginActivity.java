package com.nellions.nellionscanvas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.nellions.nellionscanvas.model.AppUrl;
import com.nellions.nellionscanvas.model.Util;
import com.nellions.nellionscanvas.utils.CustomRequest;
import com.nellions.nellionscanvas.utils.Preferences;
import com.nellions.nellionscanvas.utils.VolleyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginBtn;
    AppUrl restLoginMethod;
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = new Preferences();
        restLoginMethod = new AppUrl("apiUserLogin");

        setViews();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }

    //set up views
    private void setViews() {
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
    }

    //make sure the details submitted are ok
    private void validateUser() {
    }

    //process to log user in
    private void userLogin() {

        if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Something is missing!", Toast.LENGTH_LONG).show();
            return;
        }

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Validating...", "Please wait...", false, false);

        String tag_json_obj = "login_request"; /* tag used to cancel the request */
        String loginUrl = restLoginMethod.getCompleteUrl(); /* login rest url */

        /* this are the params to post */
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        params.put("secret_code", "nellions");

        /* use volley customRequest class to handle requests */
        CustomRequest loginRequest = new CustomRequest(Request.Method.POST, loginUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //handle the response in a new thread
                        try {
                            loading.setMessage("Welcome back " + response.getString("name"));
                            preferences.storePreferences(LoginActivity.this, Util.USER_PREF, Util.USER_ID, response.getString("staffid"));
                            preferences.storePreferences(LoginActivity.this, Util.USER_PREF, Util.U_NAME, response.getString("name"));
                            preferences.storePreferences(LoginActivity.this, Util.USER_PREF, Util.USER_NAME, response.getString("email"));

                            loading.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException error) {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "Phone/Password is Incorrect", Toast.LENGTH_LONG).show();
                            Log.e("NELLIONS", "json exception on login", error);
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                /* handle request error */
                Toast.makeText(LoginActivity.this, "Check your Network connections!", Toast.LENGTH_LONG).show();
                Log.e("NELLIONS", "response error on login", error);
                loginBtn.setEnabled(true);
            }
        });

        /* EXTENDS THE REQUEST TIMEOUT */
        int socketTimeout = 20000;// 20 secs
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        loginRequest.setRetryPolicy(policy);

         /* add the request to the request queue */
        VolleyApplication.getInstance().addToRequestQueue(loginRequest, tag_json_obj);

    }
}
