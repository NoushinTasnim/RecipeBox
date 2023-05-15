package com.example.quotepad.nav_frags.recipe;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.quotepad.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeViewerImgActivity extends AppCompatActivity {

    private String from, recipe_name, recipe_ing, recipe_ins, recipe_prep, recipe_type, recipe_img, recipe_time, url, recipe_src, recipe_author, ing, cal;
    private TextView rec_name, rec_ing, rec_prep, rec_type, urls;
    private CardView cr1, cr2, cardie;
    private ImageView iv;
    ArrayList<String> ingrs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_viewer_img);

        rec_name = findViewById(R.id.recipe_nm);
        rec_ing = findViewById(R.id.recipe_img_ing);
        rec_prep = findViewById(R.id.recipe_prep_tm);
        rec_type = findViewById(R.id.recipe_cal);
        urls = findViewById(R.id.recipe_url);
        iv = findViewById(R.id.recipe_pic);


        recipe_name = getIntent().getStringExtra("name");
        recipe_src = getIntent().getStringExtra("src");
        recipe_img = getIntent().getStringExtra("img");
        ing = getIntent().getStringExtra("ing");
        url = getIntent().getStringExtra("url");
        cal = getIntent().getStringExtra("cal");
        recipe_prep = getIntent().getStringExtra("tm");
        Log.i(TAG, "onCreatehhh: "+recipe_name);

        rec_name.setText(recipe_name);

        double pre = Double.parseDouble(recipe_prep);
        Log.i(TAG, "onCreate: " + pre);
        if(pre<1.0){
            recipe_prep="-";
        }

        rec_prep.setText("Preparation Time: " + recipe_prep+ " minutes");
        double num = Double.parseDouble(cal);
        Log.i(TAG, "onCreate: " + num);
        int myInt = (int) num;
        Log.i(TAG, "onCreate: nn"+ myInt);
        rec_type.setText("Total Calories: " + myInt + "kCal");
        Picasso.get().load(recipe_img).into(iv);
        urls.setText("Learn More About This Recipe");
        urls.setTextColor(Color.BLUE);
        urls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the URL in a web browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        String items = "";
        try {
            ingrs.clear();
            JSONArray jsonArray = new JSONArray(ing);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String text = jsonObject.getString("text");
            ingrs.add(text);
            items += text;
            for (int i = 1; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                text = jsonObject.getString("text");
                ingrs.add(text);
                items += ", " + text;
                // Do something with the text value, such as add it to a list
            }
            rec_ing.setText(items);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}