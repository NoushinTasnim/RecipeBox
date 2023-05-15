package com.example.quotepad.nav_frags.recipe;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quotepad.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeViewerActivity extends AppCompatActivity {

    private String from, recipe_name, recipe_ing, recipe_ins, recipe_prep, recipe_type, recipe_img, recipe_time, recipe_src, recipe_author, ing, url, cal;
    private TextView rec_name, rec_ing, rec_ins, rec_prep, rec_type, rec_time, rec_aut, rec_author;
    ArrayList<String> ingrs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_viewer);

        rec_name = findViewById(R.id.recipe_label);
        rec_ing = findViewById(R.id.recipe_ing);
        rec_ins = findViewById(R.id.recipe_inst);
        rec_prep = findViewById(R.id.recipe_prep);
        rec_type = findViewById(R.id.recipe_cuisine);
        rec_time = findViewById(R.id.recipe_tm);
        rec_aut = findViewById(R.id.recipe_aut);
        rec_author = findViewById(R.id.rec_up_au);

        from = getIntent().getStringExtra("from");

            recipe_name = getIntent().getStringExtra("name");
            recipe_ing = getIntent().getStringExtra("ing");
            recipe_ins = getIntent().getStringExtra("ins");
            recipe_prep = getIntent().getStringExtra("prep");
            recipe_type = getIntent().getStringExtra("cui");
            recipe_time = getIntent().getStringExtra("time");

            rec_aut.setVisibility(View.INVISIBLE);
            rec_author.setVisibility(View.INVISIBLE);

            if (from.equals("us")) {
                recipe_author = getIntent().getStringExtra("author");
                rec_aut.setVisibility(View.VISIBLE);
                rec_aut.setText(recipe_author);
                Log.i(TAG, "onCreate: " + recipe_author);
                rec_author.setVisibility(View.VISIBLE);
            }

            rec_name.setText(recipe_name);
            rec_ing.setText(recipe_ing);
            rec_ins.setText(recipe_ins);
            rec_prep.setText("Preparation Time: " + recipe_prep + " minutes");
            rec_type.setText("Cuisine Type: " + recipe_type);
            rec_time.setText(recipe_time);

    }
}