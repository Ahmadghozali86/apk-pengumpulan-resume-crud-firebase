package com.example.project_resume_collection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    FragmentAkun fragmentAkun;
    FragmentView fragmentView;
    FragmentTugas fragmentTugas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fragmentView = new FragmentView();
        fragmentTugas = new FragmentTugas();
        fragmentAkun = new FragmentAkun();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_tugas){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragmentTugas).commit();

                } else if(item.getItemId()==R.id.menu_view){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragmentView).commit();

                } else if(item.getItemId()==R.id.menu_akun){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragmentAkun).commit();
                }else {

                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_tugas);
    }
}