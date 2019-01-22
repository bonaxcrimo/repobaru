package com.android.anime2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class favorit extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    List<Anime> lstAnime;

    AnimeAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        lstAnime = new ArrayList<>();

        loadAnime();
        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myAdapter = new AnimeAdapter(this,lstAnime);
        myrv.setLayoutManager(new GridLayoutManager(this,1));
        myrv.setAdapter(myAdapter);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            User user = SharedPrefManager.getInstance(this).getUser();
            View header = navigationView.getHeaderView(0);
            TextView navName = (TextView) header.findViewById(R.id.nav_user);
            TextView navSub = (TextView) header.findViewById(R.id.nav_sub);
            ImageView img= (ImageView) header.findViewById(R.id.imageViewProfile);
            navName.setText(user.getUsername());
            navSub.setText("@"+user.getUsername());
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            nav_Menu.findItem(R.id.nav_register).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
        }else{
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
        }
    }
    private void loadAnime(){
        User user = SharedPrefManager.getInstance(this).getUser();
        StringRequest reqData = new StringRequest(Request.Method.GET, ServerApi.URL_FAVORIT+user.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("datanya",res.toString());
                            JSONArray array = res.getJSONArray("data");
                            for(int i=0;i<array.length();i++){
                                JSONObject data= array.getJSONObject(i);
                                Log.d("datastring",data.getString("rating"));
                                String rating = data.getString("rating")=="null"?"0":data.getString("rating");
                                lstAnime.add(new Anime(Integer.valueOf(data.getString("id")),
                                        rating,data.getString("title").replace("null",""),
                                        data.getString("episode").replace("null",""),
                                        data.getString("genre").replace("null",""),
                                        data.getString("type").replace("null",""),
                                        data.getString("img_url").replace("null",""),data.getInt("members")));
                                //                                lstKuliner.add(new Kuliner(Integer.valueOf(data.getString("id")),data.getString("kuliner_name"),data.getString("kuliner_gambar"),data.getString("kuliner_lokasi"),data.getString("artikel")));
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
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(favorit.this,home.class);
                startActivity(h);
                break;
            case R.id.nav_favorit:
                Intent i= new Intent(favorit.this,favorit.class);
                startActivity(i);
                break;
            case R.id.nav_login:
                Intent g= new Intent(favorit.this,login.class);
                startActivity(g);
                break;
            case R.id.nav_register:
                Intent s= new Intent(favorit.this,register.class);
                startActivity(s);
                break;
            case R.id.nav_anime:
                Intent a= new Intent(favorit.this,tambahanime.class);
                startActivity(a);
                break;
            case R.id.nav_tentang:
                Intent t= new Intent(favorit.this,tentang.class);
                startActivity(t);
                break;
                case R.id.nav_logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
