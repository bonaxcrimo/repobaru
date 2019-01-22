package com.android.anime2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anime2.Util.AppController;
import com.android.anime2.Util.ServerApi;
import com.android.anime2.Util.SharedPrefManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class animedetail extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    private TextView judul,genre,tipe;
    private ImageView img;
    ProgressBar progressBar;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animedetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(navigationView.GONE);

       drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        judul =(TextView) findViewById(R.id.judul);
        genre = (TextView) findViewById(R.id.genre);
        tipe = (TextView) findViewById(R.id.tipe);
        TextView Rating = (TextView) findViewById(R.id.rating);
        Intent intent = getIntent();
        String Judul = intent.getExtras().getString("title");
        String Gambar = intent.getExtras().getString("gambar");
        String Tipe = intent.getExtras().getString("tipe");
        String Genre = intent.getExtras().getString("genre");
        String Sinopsis = intent.getExtras().getString("sinopsis");
        String rating = intent.getExtras().getString("rating");
        ImageView  img_anime = (ImageView) findViewById(R.id.gambar_anime);
        Glide.with(getApplicationContext()).load(Gambar).placeholder(R.drawable.ic_menu_gallery).crossFade().into(img_anime);
        id = intent.getExtras().getInt("id");
//        textView.setHtml(Sinopsis,new HtmlAssetsImageGetter(textView));
        judul.setText(Judul);
        genre.setText("Genre :"+Genre);
        tipe.setText("Tipe :"+Tipe);
        Rating.setText("Rating : "+ rating);
//        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//        ratingBar.setRating(rating);
        Button btnLihat = (Button)findViewById(R.id.btnLihat);
        Button btnFavorit = (Button) findViewById(R.id.btnFavorit);
        Button btnRating = (Button) findViewById(R.id.btnRating);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            btnFavorit.setVisibility(View.GONE);
        }
        btnRating.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_TAMBAH_RATING,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);
                                    Log.d("respNya",response.toString());
//                                    //if no error in response
                                    if (obj.getInt("error")==0) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(animedetail.this,home.class));

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
                        params.put("id",String.valueOf(id));
                        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                        params.put("user_id", String.valueOf(user.getId()));
                        params.put("rating",String.valueOf(ratingBar.getRating()));
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
                Log.d("urlnya",ServerApi.URL_TAMBAH_RATING+":"+String.valueOf(id));
            }
        });
        btnFavorit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_TAMBAH_FAVORIT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);
                                    Log.d("respNya",response.toString());
//                                    //if no error in response
                                    if (obj.getInt("error")==0) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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
                        params.put("id",String.valueOf(id));
                        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                        params.put("user_id", String.valueOf(user.getId()));
                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(stringRequest);
                Log.d("urlnya",ServerApi.URL_TAMBAH_FAVORIT+":"+String.valueOf(id));
            }
        });
        btnLihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(animedetail.this,Ulas.class);
                intent.putExtra("id",String.valueOf(id));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(animedetail.this,home.class);
                startActivity(h);
                break;
            case R.id.nav_favorit:
                Intent i= new Intent(animedetail.this,favorit.class);
                startActivity(i);
                break;
            case R.id.nav_login:
                Intent g= new Intent(animedetail.this,login.class);
                startActivity(g);
                break;
            case R.id.nav_anime:
                Intent a= new Intent(animedetail.this,tambahanime.class);
                startActivity(a);
                break;
            case R.id.nav_register:
                Intent s= new Intent(animedetail.this,register.class);
                startActivity(s);
                break;
            case R.id.nav_tentang:
                Intent t= new Intent(animedetail.this,tentang.class);
                startActivity(t);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
