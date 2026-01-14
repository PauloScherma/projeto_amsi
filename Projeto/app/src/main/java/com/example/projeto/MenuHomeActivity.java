package com.example.projeto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;



public class MenuHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int EDIT = 100, ADD = 200;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private String email;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call this first
        setContentView(R.layout.activity_menu_home); // Then set the layout

        // Initialize views and managers
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        carregarCabecalho();

        setTitle("Home");

        // NOW you can safely use fragmentManager and navigationView
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFragment, new HomeFragment())
                    .commit();
            // navigationView.setCheckedItem(R.id.navHome);
        }
    }

    private void carregarCabecalho() {
        email = getIntent().getStringExtra("EMAIL");
        // se recebeu email => armazena SharedPref
        // caso contrario => vai carregar o que existe no shared
        SharedPreferences sharedPrefUser= getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        if (email!=null){
            SharedPreferences.Editor editor=sharedPrefUser.edit();
            editor.putString("EMAIL",email);
            editor.apply();
        }
        else
            email=sharedPrefUser.getString("EMAIL","Sem Email");

        View hView = navigationView.getHeaderView(0);
        TextView tvEmail=hView.findViewById(R.id.tvEmail);
        tvEmail.setText(email);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        if(menuItem.getItemId()==R.id.navHome){
            fragment = new HomeFragment();
            setTitle("Home");
        }
        else if(menuItem.getItemId()==R.id.navRequest){
            fragment = new RequestsFragment();
            setTitle("Requests");
        } else if (menuItem.getItemId()==R.id.navDocuments) {
            System.out.println("Documents");
            setTitle("Documents");
        }else if (menuItem.getItemId()==R.id.navProfile) {
            setTitle("Profile");
        }else if (menuItem.getItemId()==R.id.navSettings) {
            setTitle("Settings");
        }else if (menuItem.getItemId()==R.id.navLogout) {
            Intent intent = new Intent(this , LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (fragment!= null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment,
                    fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickCreateRequest(View view) {
        System.out.println("Request");
    }

    public void onClickCreateProfile(View view) {
        System.out.println("Profile");
    }

    public void onClickAddDocument(View view) {
        System.out.println("Documents");
    }
}