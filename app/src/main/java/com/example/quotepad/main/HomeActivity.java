package com.example.quotepad.main;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotepad.R;
import com.example.quotepad.nav_frags.profile.MyProfileFragment;
import com.example.quotepad.nav_frags.recipe.DiscoverFragment;
import com.example.quotepad.nav_frags.recipe.MyRecipesFragment;
import com.example.quotepad.nav_frags.recipe.UploadRecipeFragment;
import com.example.quotepad.nav_frags.profile.SearchFragment;
import com.example.quotepad.main.user.UserActivity;
import com.example.quotepad.nav_frags.recipe.SearchRecipeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView nav_user_name, nav_name;
    ImageView menuIcon;
    TextView tv;
    Animation aniFade, aniFade2;

    DatabaseReference reference;
    Query checkUser;

    String name, em, username, ph, pass, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        tv = findViewById(R.id.tab_name);

        View headerView = navigationView.getHeaderView(0);

        nav_user_name = (TextView) headerView.findViewById(R.id.nav_header_user_name);
        nav_name = (TextView) headerView.findViewById(R.id.nav_header_name);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users");
        checkUser = reference.orderByChild("id").equalTo(uid);

        aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        aniFade2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);

        loadData();

        loadFragment(new SearchRecipeFragment());
        Menu menu = navigationView.getMenu();
        MenuItem item1 = menu.getItem(0);
        tv.setText("Search Recipes");
        item1.setChecked(true);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username = snapshot.child(uid).child("username").getValue(String.class);
                    name = snapshot.child(uid).child("name").getValue(String.class);
                    em = snapshot.child(uid).child("email").getValue(String.class);
                    ph = snapshot.child(uid).child("phone").getValue(String.class);
                    pass = snapshot.child(uid).child("password").getValue(String.class);

                    btnSaveData(name,username,em);

                    nav_name.setText(name);
                    nav_user_name.setText(username);
                    Log.i(TAG, "onDataChange: " + username);
                }
                else{
                    Toast.makeText(HomeActivity.this, "Could Not Fetch User Data", Toast.LENGTH_SHORT).show();
                    nav_user_name.setText("Anonymous");
                    nav_name.setText("name" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Could Not Fetch User Data", Toast.LENGTH_SHORT).show();
                nav_user_name.setText("Anonymous");
                nav_name.setText("name" );
            }
        });

        naviagtionDrawer();
    }

    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.frag_container);
    }

    private void naviagtionDrawer() {
        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setCheckedItem(R.id.nav_home);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.OpenDrawer, R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int it = -1;
                Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem item = menu.getItem(i);
                    if(item.hasSubMenu())
                    {
                        Log.i(TAG, "onNavigationItemSelected: haass " + item);
                        Menu menu2 = item.getSubMenu();
                        for (int ij = 0; ij < menu2.size(); ij++) {
                            MenuItem item2 = menu2.getItem(ij);
                            Log.i(TAG, "onNavigationItemSelected: jgifs " + item2);
                            if (item2.isChecked()) {
                                it = item2.getItemId();
                                item2.setChecked(false);
                                Log.i(TAG, "onNavigationItemSelected: selft " + it);
                            }
                        }
                    }
                    else
                    {
                        if (item.isChecked()) {
                            Log.i(TAG, "onNavigationItemSelected: nopes " + item);
                            it = item.getItemId();
                            item.setChecked(false);
                        }
                    }
                }
                menuItem.setChecked(false);
                int id = menuItem.getItemId();

                Log.i(TAG, "onNavigationItemSelected: " + it + " " + menuItem);

                if(it==id)
                {
                    menuItem.setChecked(true);
                    return false;
                }
                else
                {
                    switch (id) {
                        case R.id.nav_search_recipes:
                            navigationView.setCheckedItem(R.id.nav_search_recipes);
                            tv.setText("Search Recipes");
                            loadFragment(new SearchRecipeFragment());
                            break;

                        case R.id.nav_up:
                            tv.setText("Upload Recipe");
                            navigationView.setCheckedItem(R.id.nav_up);
                            loadFragment(new UploadRecipeFragment());
                            break;

                        case R.id.nav_my_recipes:
                            tv.setText("My Recipes");
                            navigationView.setCheckedItem(R.id.nav_my_recipes);
                            loadFragment(new MyRecipesFragment());
                            break;

                        case R.id.nav_discover:
                            tv.setText("Discover Recipes");
                            navigationView.setCheckedItem(R.id.nav_discover);
                            loadFragment(new DiscoverFragment());
                            break;

                        case R.id.nav_user_profile:
                            tv.setText("My Profile");
                             navigationView.setCheckedItem(R.id.nav_user_profile);
                             loadFragment(new MyProfileFragment());
                             break;

                        case R.id.search_user:
                            tv.setText("Search Users");
                            navigationView.setCheckedItem(R.id.search_user);
                            loadFragment(new SearchFragment());
                            break;

                        case R.id.sign_out:
                            navigationView.setCheckedItem(R.id.sign_out);

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
                            builder1.setMessage("Are you sure you want to logout from your account?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(HomeActivity.this, UserActivity.class));
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            break;

                        case R.id.del_user:
                            navigationView.setCheckedItem(R.id.del_user);

                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setMessage("You won't be able to retrieve your account. Are you sure you want to delete your account?");
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            FirebaseDatabase.getInstance().getReference("emails").child(username).removeValue();
                                            FirebaseDatabase.getInstance().getReference("users").child(uid).removeValue();

                                            FirebaseDatabase.getInstance().getReference("quotes")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for(DataSnapshot dataSnapshot: snapshot.getChildren())
                                                            {
                                                                Log.i(TAG, "onDataChange: xcvbn "+dataSnapshot.getValue());
                                                                Log.i(TAG, "onDataChange: jhb "+dataSnapshot.child("quote").getValue());
                                                                if(username.equals((dataSnapshot.child("author").getValue().toString()))){
                                                                    Log.i(TAG, "onDataChange: found ");
                                                                    FirebaseDatabase.getInstance().getReference("quotes").child(dataSnapshot.getKey()).removeValue();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Toast.makeText(HomeActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "User account deleted.");
                                                            }
                                                        }
                                                    });
                                            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                            startActivity(new Intent(HomeActivity.this, UserActivity.class));
                                            dialog.cancel();
                                        }
                                    });

                            builder.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert1 = builder.create();
                            alert1.show();

                            break;

                        default:
                            break;
                    }
                    menuItem.setChecked(true);
                    setTitle(menuItem.getTitle());
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();

        moveTaskToBack(true);
    }
    private void loadFragment(Fragment fragment) {
        if(getCurrentFragment() != fragment){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            if(getCurrentFragment()!=null)
            {
                fm.beginTransaction().remove(getCurrentFragment()).commit();
            }

            ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.frag_container, fragment);
            ft.commitNow();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void loadData() {
        File file = HomeActivity.this
                .getFileStreamPath("CurrentUser.txt");

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader( HomeActivity.this.openFileInput("CurrentUser.txt")));

                String st, qu = "";

                while ((st = reader.readLine()) != null){
                    qu = qu + st;
                }
                String[] separated = qu.split(" ; ");
                Log.i(TAG, "loadData: " + separated[1]);
                String separated2[] = qu.split(" ; ");
                // Print the string
                Log.i(TAG, "loadData: " + separated2[1]);
                nav_name.setText(separated[0]);
                nav_user_name.setText(separated2[1]);
                reader.close();

            } catch (IOException e) {
                Toast.makeText( HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    public void btnSaveData(String name, String username, String email) {
        try {
            FileOutputStream file = openFileOutput("CurrentUser.txt", MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(file);

            outputStreamWriter.write(name + " ; " + username + " ; " + email);

            outputStreamWriter.flush();
            outputStreamWriter.close();
            //Toast.makeText(MainActivity.this, "Successfully saved", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}