package com.example.quotepad.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotepad.R;
import com.example.quotepad.nav_frags.recipe.RecipeViewerActivity;
import com.example.quotepad.nav_frags.recipe.RecipeViewerImgActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchRecipeAdapter extends RecyclerView.Adapter<SearchRecipeAdapter.MyViewHolder> {

    ArrayList<String> recipe_label;
    ArrayList<String> recipe_source;
    ArrayList<String> food_img;
    ArrayList<String> ing;
    ArrayList<String> url;
    ArrayList<String> cal;
    ArrayList<String> tm;
    Context context;

    public SearchRecipeAdapter(Context context) {
        this.context = context;
    }

    public SearchRecipeAdapter(Context context, ArrayList<String> recipe_label, ArrayList<String> recipe_source, ArrayList<String> food_img, ArrayList<String> url, ArrayList<String> ing, ArrayList<String> cal, ArrayList<String> tm) {
        this.context = context;
        this.recipe_label = recipe_label;
        this.recipe_source = recipe_source;
        this.food_img = food_img;
        this.ing = ing;
        this.url = url;
        this.tm = tm;
        this.cal = cal;

        Log.i("here","nopes");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        Log.i("er","assdf");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        String name = recipe_label.get(position);
        String src = recipe_source.get(position);
        String ings = ing.get(position);
        String urls=url.get(position);
        holder.rec_lab.setText(name);
        holder.rec_src.setText(src);
        String imageUrl = food_img.get(position);
        String time = tm.get(position);
        String calory=cal.get(position);
        Picasso.get().load(imageUrl).into(holder.food);
        Log.i(TAG, "onBindViewHolder: here");

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RecipeViewerImgActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("src",src);
                intent.putExtra("img", imageUrl);
                intent.putExtra("url",urls);
                intent.putExtra("ing", ings);
                intent.putExtra("cal",calory);
                intent.putExtra("tm", time);
                Log.i(TAG, "onClick: even here");
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipe_label.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rec_lab, rec_src;// init the item view's
        ImageView food;

        public MyViewHolder(View itemView) {
            super(itemView);

            Log.i("gt","as");
            // get the reference of item view's
            rec_lab = (TextView) itemView.findViewById(R.id.item1);
            rec_src = (TextView) itemView.findViewById(R.id.item2);
            food = (ImageView) itemView.findViewById(R.id.food_img);
        }
    }

    public void removeItem(int position) {
        recipe_label.remove(position);
        recipe_source.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(String item, String item2, int position) {
        recipe_label.add(position, item);
        recipe_source.add(position,item2);
        notifyItemInserted(position);
    }

    public ArrayList<String> getQuotes() {
        return recipe_label;
    }

    public ArrayList<String> getAuthors() {
        return recipe_source;
    }
}
