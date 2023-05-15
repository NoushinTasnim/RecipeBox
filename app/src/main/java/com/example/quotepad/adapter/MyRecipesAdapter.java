package com.example.quotepad.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.R;
import com.example.quotepad.model.RecipeModel;
import com.example.quotepad.nav_frags.profile.OtherUserActivity;
import com.example.quotepad.nav_frags.recipe.RecipeViewerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.DiscoverViewHolder> {

    Context context;
    ArrayList<RecipeModel> list;
    String user = null;

    public MyRecipesAdapter(Context context, ArrayList<RecipeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout2, parent, false);
        return new DiscoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipesAdapter.DiscoverViewHolder holder, int position) {
        RecipeModel model = list.get(position);

        holder.recipe_name.setText(model.getRecipe_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Intent intent = new Intent(view.getContext(), RecipeViewerActivity.class);
                intent.putExtra("from","me");
                intent.putExtra("name",model.getRecipe_name());
                intent.putExtra("ing",model.getRecipe_ing());
                intent.putExtra("ins",model.getRecipe_inst());
                intent.putExtra("prep", model.getRecipe_prep());
                intent.putExtra("cui",model.getType());
                intent.putExtra("time", model.getTime());
                view.getContext().startActivity(intent);
                //Toast.makeText(context, p, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder {

        TextView recipe_name, rec_ing, rec_ins, rec_prep, time, genre;

        public DiscoverViewHolder(@NonNull View itemView) {
            super(itemView);

            recipe_name = itemView.findViewById(R.id.item1);
        }
    }

    public void removeItem(RecipeModel item, int position) {

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.child("username").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context.getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("recipes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Log.i(TAG, "onDataChange: xcvbn "+dataSnapshot.getValue());
                            Log.i(TAG, "onDataChange: jhb "+dataSnapshot.child("recipe_name").getValue());
                            if((item.getRecipe_name().toString()).equals((dataSnapshot.child("recipe_name").getValue().toString()))){
                                Log.i(TAG, "onDataChange: found ");
                                FirebaseDatabase.getInstance().getReference("recipes").child(dataSnapshot.getKey()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context.getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("recipe")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            Log.i(TAG, "onDataChange: xcvbn "+dataSnapshot.getValue());
                            Log.i(TAG, "onDataChange: jhb "+dataSnapshot.child("recipe_name").getValue());
                            if((item.getRecipe_name().toString()).equals((dataSnapshot.child("recipe_name").getValue().toString()))){
                                Log.i(TAG, "onDataChange: found ");
                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("recipe").child(dataSnapshot.getKey().toString()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context.getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        list.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<RecipeModel> getQuotes() {
        return list;
    }
}