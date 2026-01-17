package com.example.projeto;

import static com.example.projeto.utils.Constants.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class MenuHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private String email;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null || token.isEmpty()) {
            redirectToLogin();
            return;
        }

        setContentView(R.layout.activity_menu_home);

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

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.contentFragment, new HomeFragment())
                    .commit();
        }
    }

    private void carregarCabecalho() {
        email = getIntent().getStringExtra("EMAIL");

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
            fragment = new RatingsFragment();
            setTitle("Rating");
        }else if (menuItem.getItemId()==R.id.navProfile) {
            setTitle("Profile");
            fragment = new ProfileFragment();
        }else if (menuItem.getItemId()==R.id.navSettings) {
            System.out.println("Settings");
            setTitle("Settings");
            fragment = new SettingsFragment();
        }else if (menuItem.getItemId()==R.id.navLogout) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().clear().apply();

            redirectToLogin();
        }

        if (fragment!= null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment,
                    fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);

        // Flags para limpar a pilha de atividades e impedir o utilizador de voltar atrás.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}