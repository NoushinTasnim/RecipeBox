package com.example.quotepad.nav_frags.profile;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotepad.R;
import com.example.quotepad.adapter.MyRecipesAdapter;
import com.example.quotepad.model.RecipeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OtherUserActivity extends AppCompatActivity {

    ArrayList<RecipeModel> list=new ArrayList<>();
    private String name, username, id;
    private TextView tv1, tv2;
    private RecyclerView rv;
    MyRecipesAdapter adapter;
    ImageView btn;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        id = getIntent().getStringExtra("id");

        tv1 = findViewById(R.id.other_name);
        tv2 = findViewById(R.id.other_username);
        rv = findViewById(R.id.other_rv);
        btn = findViewById(R.id.user_back_btn);

        tv1.setText(name);
        tv2.setText(username);

        adapter = new MyRecipesAdapter(this,list);

        // To display the Recycler view linearly
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


        FirebaseDatabase.getInstance().getReference("users").child(id).child("recipe")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();

                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            if(dataSnapshot.child("publicity").getValue(String.class).equals("1"))
                            {
                                RecipeModel notification = dataSnapshot.getValue(RecipeModel.class);
                                Log.i(TAG, "onDataChange: okkk");
                                list.add(notification);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OtherUserActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}