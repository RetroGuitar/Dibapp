package com.example.dibapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;

    @Override
    public void onBackPressed() {

        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_mycourses);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View parent = navigationView.getHeaderView(0);
        Menu navMenu = navigationView.getMenu();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, new MyCourseFragment());
        ft.commit();
        toolbar.setTitle(R.string.MyCourses);








        //gestione della schermata relativa all'UTENTE CORRENTE
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //impostazione del nome dell'utente e del tipo di utente (studete/professore)
                TextView nameText=(TextView)findViewById(R.id.Nomeprofilo);
                TextView typeText=(TextView)findViewById(R.id.TipoProfilo);
                nameText.setText(dataSnapshot.child("name").getValue().toString());
                boolean teacher=(boolean)dataSnapshot.child("teacher").getValue();
                if (teacher)  typeText.setText(R.string.teacher);
                else typeText.setText(R.string.student);
                //definizione ed implementazione del fab in base al tipo di utente
                fab = findViewById(R.id.fab);

                if (teacher) {
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent addCourse=new Intent(ProfileActivity.this, CreateCourseActivity.class);
                            startActivity (addCourse);
                        }
                    });

                } else{

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent listCourse=new Intent(ProfileActivity.this, ListCourseActivity.class);
                            startActivity (listCourse);
                        }
                    });

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id =menuItem.getItemId();
        if (id==R.id.nav_mycourses){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new MyCourseFragment());
            ft.commit();
            toolbar.setTitle(R.string.MyCourses);
            fab.show();
        }
            if (id==R.id.nav_editProfile){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new EditFragment());
                ft.commit();
                toolbar.setTitle(R.string.EditProfile);
                fab.hide();
        }
        if (id==R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();

            Toast.makeText(ProfileActivity.this, "Successfully Logged Out!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            finish();
            startActivity(intent);

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
