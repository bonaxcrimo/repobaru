package com.android.anime2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.anime2.Util.AppController;
import com.android.anime2.Util.ServerApi;
import com.android.anime2.Util.SharedPrefManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UlasanActivity extends AppCompatActivity {
    String id;
    EditText komen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ulasan);
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Tambah Comment"); // set the top title
        actionBar.setDisplayHomeAsUpEnabled(true);
        id=getIntent().getExtras().getString("id");
        komen = (EditText)findViewById(R.id.editcomment);
        Button btnComment = (Button) findViewById(R.id.btnKirim);
        btnComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Komen = komen.getText().toString();
                if (TextUtils.isEmpty(Komen)) {
                    komen.setError("Please enter your name");
                    komen.requestFocus();
                    return;
                }
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_TAMBAH_COMMENT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);
                                    Log.d("respNya",response.toString());
//                                    //if no error in response
                                    if (obj.getInt("error")==0) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(UlasanActivity.this,Ulas.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Gagal terhubung ke internet", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("anime_id",String.valueOf(id));
                        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                        params.put("user_id", String.valueOf(user.getId()));
                        params.put("comment", Komen);
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
                Log.d("urlnya", ServerApi.URL_TAMBAH_FAVORIT+":"+String.valueOf(id));
            }
        });
    }

}
