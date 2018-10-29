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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    EditText email,password,name,cpassword;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        email = (EditText) findViewById(R.id.editEmail);
        name = (EditText) findViewById(R.id.editName);
        password = (EditText) findViewById(R.id.editPassword);
        cpassword = (EditText)findViewById(R.id.editCPassword);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this,login.class));
            }
        });
        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email = email.getText().toString();
                final String Password = password.getText().toString();
                final String CPassword = cpassword.getText().toString();
                final String Name = name.getText().toString();
                if (TextUtils.isEmpty(Email)) {
                    email.setError("Please enter your email");
                    email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(Name)) {
                    name.setError("Please enter your name");
                    name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(CPassword)) {
                    cpassword.setError("Please enter your password again");
                    cpassword.requestFocus();
                    return;
                }
                if(!Password.equals(CPassword)){
                    password.setError("Password tidak sama");
                    password.requestFocus();
                    return;
                }
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.URL_REGISTER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);

                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);
                                    //if no error in response
                                    if (obj.getInt("error")==0) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                        //getting the user from the response
                                        startActivity(new Intent(register.this, login.class));
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
                        params.put("username",Name);
                        params.put("email", Email);
                        params.put("password", Password);
                        params.put("c_password",CPassword);
                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });
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
            nav_Menu.findItem(R.id.nav_favorit).setVisible(false);
        }
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



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(register.this,home.class);
                startActivity(h);
                break;
            case R.id.nav_favorit:
                Intent i= new Intent(register.this,favorit.class);
                startActivity(i);
                break;
            case R.id.nav_login:
                Intent g= new Intent(register.this,login.class);
                startActivity(g);
                break;
            case R.id.nav_register:
                Intent s= new Intent(register.this,register.class);
                startActivity(s);
                break;
            case R.id.nav_tentang:
                Intent t= new Intent(register.this,tentang.class);
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
