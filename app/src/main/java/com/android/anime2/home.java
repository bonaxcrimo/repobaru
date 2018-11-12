package com.android.anime2;

import android.app.ProgressDialog;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
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

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    Spinner spin;
    int index;
    private ArrayList<String> filter;
    private JSONArray result;
    List<Anime> lstAnime;
    AnimeAdapter myAdapter;
    ArrayAdapter<String> spinAdapter;
    String[]  options = {"rating","members","rekomendasi"};
    int limit;
    ProgressDialog progress;
    String option;
    String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progress = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        limit=5;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        spin = (Spinner) findViewById(R.id.spin);
        spinAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spintext,options);
        spin.setAdapter(spinAdapter);
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        index=spin.getSelectedItemPosition();
        option = options[0];
        query="";
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (index != i){
                    // Your code here
                    limit=5;
                    option = options[i];
                    lstAnime.clear();
                    loadAnime();
                }
                index=i;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Button btnMore= (Button) findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                limit +=5;
                lstAnime.clear();
                loadAnime();

            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        lstAnime = new ArrayList<>();
        loadAnime();
        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myAdapter = new AnimeAdapter(this,lstAnime);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d("loginis", String.valueOf(SharedPrefManager.getInstance(this).isLoggedIn()));
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
            nav_Menu.findItem(R.id.nav_favorit).setVisible(true);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
        }else{
            nav_Menu.findItem(R.id.nav_favorit).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
        }


    }
    private void loadAnime(){
        String url="";
        url = query!="rekomendasi"?ServerApi.URL_ANIME+option+"/"+limit+"/"+query:ServerApi.URL_ANIME+option+"/"+limit+"/"+query;
        StringRequest reqData = new StringRequest(Request.Method.GET, url,
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

                            progress.dismiss();
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
    private void getData(){

//        getFilter(data);
//        StringRequest reqData = new StringRequest(Request.Method.GET, ServerApi.URL_FILTER,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject res = new JSONObject(response);
//                            Log.d("datanya",res.toString());
//                            result = res.getJSONArray("data");
//                            getFilter(result);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("VolErr","error : " + error.getMessage());
//
//                    }
//                }
//        );
//        AppController.getInstance().addToRequestQueue(reqData);
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
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query1) {
                query=query1;
                lstAnime.clear();
                loadAnime();
                Log.d("querynya",query1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(s);
                if (newText.length() == 0) {
                    // Search
                    query="";
                    lstAnime.clear();
                    loadAnime();
                    Log.d("querynya","kosong");
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
                Intent h= new Intent(home.this,home.class);
                startActivity(h);
                break;
            case R.id.nav_favorit:
                Intent i= new Intent(home.this,favorit.class);
                startActivity(i);
                break;
            case R.id.nav_login:
                Intent g= new Intent(home.this,login.class);
                startActivity(g);
                break;
            case R.id.nav_register:
                Intent s= new Intent(home.this,register.class);
                startActivity(s);
                break;
            case R.id.nav_tentang:
                Intent t= new Intent(home.this,tentang.class);
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
