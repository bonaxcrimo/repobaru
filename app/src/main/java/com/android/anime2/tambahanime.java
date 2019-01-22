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

public class tambahanime extends AppCompatActivity {
    String id;
    EditText title,url,episode,genre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahanime);
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Tambah Anime"); // set the top title
        actionBar.setDisplayHomeAsUpEnabled(true);
        title = (EditText)findViewById(R.id.edittitle);
        url =(EditText)findViewById(R.id.editurl);
        episode=(EditText)findViewById(R.id.editepisode);
        genre =(EditText)findViewById(R.id.editgenre);
        Button btnKirim = (Button) findViewById(R.id.btnKirim);
        btnKirim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Title = title.getText().toString();
                final String Url = url.getText().toString();
                final String Episode = episode.getText().toString();
                final String Genre= genre.getText().toString();
                if (TextUtils.isEmpty(Title)) {
                    title.setError("Please enter anime title");
                    title.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(Url)){
                    url.setError("Please enter anime url image");
                    url.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(Episode)){
                    episode.setError("Please enter total episode anime");
                    episode.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(Genre)){
                    genre.setError("Please enter genre anime");
                    genre.requestFocus();
                    return;
                }
                Log.d("urldia",ServerApi.URL_TAMBAH_ANIME);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_TAMBAH_ANIME,
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
                                        startActivity(new Intent(tambahanime.this,home.class));
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
                        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                        params.put("user_id", String.valueOf(user.getId()));
                        params.put("title",Title);
                        params.put("url",Url);
                        params.put("episode",Episode);
                        params.put("type","0");
                        params.put("members","0");
                        params.put("genre",Genre);
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
                Log.d("urlnya", ServerApi.URL_TAMBAH_FAVORIT+":"+String.valueOf(id));
            }
        });
    }
}
