package com.example.quotepad.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteRecipesAdapter extends RecyclerView.Adapter<FavouriteRecipesAdapter.viewHolder>{

    Context context;
    ArrayList<RecipeModel> list;

    public FavouriteRecipesAdapter(Context context, ArrayList<RecipeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteRecipesAdapter.viewHolder holder, int position) {
        RecipeModel model=list.get(position);

        holder.quote.setText("\"" + model.getRecipe_name() + "\"");
        Log.i(TAG, "onDataChange: as" + model.getRecipe_name());
        holder.author.setText(model.getAuthor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView quote, author;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            quote = itemView.findViewById(R.id.item1);
            author = itemView.findViewById(R.id.item2);
        }
    }

    public void removeItem(RecipeModel item, int position) {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("fav")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            if((item.getRecipe_name().toString()).equals((dataSnapshot.child("recipe_name").getValue().toString()))){
                                Log.i(TAG, "onDataChange: found ");
                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("fav").child(dataSnapshot.getKey().toString()).removeValue();
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