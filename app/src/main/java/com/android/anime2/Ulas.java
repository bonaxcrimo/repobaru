package com.android.anime2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.anime2.Util.AppController;
import com.android.anime2.Util.ServerApi;
import com.android.anime2.Util.SharedPrefManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ulas extends AppCompatActivity {
    List<Ula> lstUlas ;
    UlaAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ulas);
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Comment"); // set the top title
        actionBar.setDisplayHomeAsUpEnabled(true);
        lstUlas = new ArrayList<>();
        loadAnime();
        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myAdapter = new UlaAdapter(this,lstUlas);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);
        Button btntambah = (Button)findViewById(R.id.btnUlasan);
        btntambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Ulas.this,UlasanActivity.class);
                i.putExtra("id",getIntent().getExtras().getString("id"));
                startActivity(i);
            }
        });
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            findViewById(R.id.btnUlasan).setVisibility(View.GONE);
        }

    }
    private void loadAnime(){
        StringRequest reqData = new StringRequest(Request.Method.GET, ServerApi.URL_COMMENT+getIntent().getExtras().getString("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("datanya",res.toString());
                            JSONArray array = res.getJSONArray("data");
                            for(int i=0;i<array.length();i++){
                                JSONObject data= array.getJSONObject(i);
                                lstUlas.add(new Ula(Integer.valueOf(data.getString("id")), data.getString("comment"), "gk",data.getString("username")));

//                                Log.d("datacomment",data.toString());
                            }
                            myAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolErr","error : " + error.getMessage());

                    }
                }
        );
        AppController.getInstance().addToRequestQueue(reqData);
    }
}
