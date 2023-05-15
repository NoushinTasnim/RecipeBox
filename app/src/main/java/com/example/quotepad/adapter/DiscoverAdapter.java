package com.example.quotepad.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.R;
import com.example.quotepad.model.RecipeModel;
import com.example.quotepad.nav_frags.recipe.RecipeViewerActivity;

import java.util.ArrayList;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> {

    Context context;
    ArrayList<RecipeModel> list;

    public DiscoverAdapter(Context context, ArrayList<RecipeModel> list) {
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
    public void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position) {
        RecipeModel model = list.get(position);

        holder.recipe_name.setText(model.getRecipe_name());
        holder.author.setText(" Uploaded by: " + model.getAuthor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RecipeViewerActivity.class);
                intent.putExtra("from","us");
                intent.putExtra("name",model.getRecipe_name());
                intent.putExtra("ing",model.getRecipe_ing());
                intent.putExtra("ins",model.getRecipe_inst());
                intent.putExtra("prep", model.getRecipe_prep());
                intent.putExtra("cui",model.getType());
                intent.putExtra("time", model.getTime());
                intent.putExtra("author", model.getAuthor());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder {

        TextView recipe_name, time, cuisine, author, prep, ing, inst;

        public DiscoverViewHolder(@NonNull View itemView) {
            super(itemView);

            recipe_name = itemView.findViewById(R.id.item1);
            author = itemView.findViewById(R.id.item2);
        }
    }
}
