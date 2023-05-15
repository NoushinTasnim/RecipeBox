package com.example.quotepad.nav_frags.recipe;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quotepad.R;
import com.example.quotepad.model.RecipeModel;
import com.example.quotepad.swipe_handler.SwipeToDeleteCallback;
import com.example.quotepad.swipe_handler.SwipeToFav;
import com.example.quotepad.adapter.SearchRecipeAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextInputLayout til;
    private AppCompatImageButton btn;
    private AutoCompleteTextView autoCompleteTextView;

    private AutoCompleteTextView autoCompleteTextView1;
    private AutoCompleteTextView autoCompleteTextView2;
    private AutoCompleteTextView autoCompleteTextView3;
    private AutoCompleteTextView autoCompleteTextView4;
    private AutoCompleteTextView autoCompleteTextView5;
    private LinearLayout linearLayout;
    private TextInputLayout textInputLayout1;
    private TextInputLayout textInputLayout2;
    private TextInputLayout textInputLayout3;
    private TextInputLayout textInputLayout4;
    private TextInputLayout textInputLayout5;


    ArrayList arrayList = new ArrayList<>();
    ArrayList arrayList2 = new ArrayList<>();
    ArrayList arrayList3 = new ArrayList<>();
    ArrayList arrayList4 = new ArrayList<>();
    ArrayList arrayList5 = new ArrayList<>();
    ArrayList arrayList6 = new ArrayList<>();
    ArrayList arrayList7 = new ArrayList<>();
    ArrayList arrayList8 = new ArrayList<>();

    private String type;

    SearchRecipeAdapter searchRecipeAdapter;
    CoordinatorLayout coordinatorLayout;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    public SearchRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RandomQuotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchRecipeFragment newInstance(String param1, String param2) {
        SearchRecipeFragment fragment = new SearchRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getActivity().findViewById(R.id.search_recipe_recycler_view);
        swipeRefreshLayout = getActivity().findViewById(R.id.swipeContainer);
        coordinatorLayout = getActivity().findViewById(R.id.coordinatorLayout);
        til = getActivity().findViewById(R.id.search_bar_recipe);
        btn = getActivity().findViewById(R.id.search_btn);
        linearLayout = getActivity().findViewById(R.id.linearLayout111);

        autoCompleteTextView = getActivity().findViewById(R.id.searchType);
        autoCompleteTextView1 = getActivity().findViewById(R.id.searchMealType);
        textInputLayout1 = getActivity().findViewById(R.id.meal_actv);
        autoCompleteTextView2 = getActivity().findViewById(R.id.searchDishType);
        textInputLayout2 = getActivity().findViewById(R.id.dish_actv);
        autoCompleteTextView3 = getActivity().findViewById(R.id.searchDietType);
        textInputLayout3 = getActivity().findViewById(R.id.diet_actv);
        autoCompleteTextView4 = getActivity().findViewById(R.id.searchHealthType);
        textInputLayout4 = getActivity().findViewById(R.id.health_actv);
        autoCompleteTextView5 = getActivity().findViewById(R.id.searchCuisineType);
        textInputLayout5 = getActivity().findViewById(R.id.cuisine_actv);

        //We will use this data to inflate the drop-down items
        String[] Subjects = new String[]{   "Search By Recipe", "Search By Meal",
                "Search By Cuisine", "Search By Dish", "Search By Health"};


        String[] Cuisine = new String[]{   "American", "Asian", "Caribbean",
                "Chinese", "Central Europe", "Eastern Europe", "French", "Indian",
                "Italian", "Kosher", "Mediterranean", "Middle Eastern", "Mexican", "Nordic",
                "South American", "South East Asian"};

        String[] Meal = new String[]{   "Breakfast", "Dinner", "Lunch", "Snack",
                "Teatime"};

        String[] Dish = new String[]{   "Biscuits & Cookies", "Bread", "Cereal", "Condiments & Sauces",
                "Desserts", "Main Course", "Drinks", "Pancake", "Preps",
                "Preserve", "Salad", "Sandwiches", "Side Dish", "Soup", "Starter"};

        String[] Health = new String[]{   "alcohol-cocktail", "alcohol-free", "celery-free", "pork-free",
                "dairy-free", "vegan", "fodmap-free", "egg-free", "fish-free",
                "gluten-free", "vegeterian", "peanut-free", "no-oil-added"};


        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, Subjects);
        autoCompleteTextView.setAdapter(adapter);


        //to get selected value add item click listener
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                linearLayout.setVisibility(View.INVISIBLE);
                textInputLayout1.setVisibility(View.INVISIBLE);
                textInputLayout2.setVisibility(View.INVISIBLE);
                textInputLayout3.setVisibility(View.INVISIBLE);
                textInputLayout4.setVisibility(View.INVISIBLE);
                textInputLayout5.setVisibility(View.INVISIBLE);

                type = autoCompleteTextView.getText().toString().trim();
                if(type.equals("Search By Recipe")){
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else if(type.equals("Search By Cuisine")){
                    textInputLayout5.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, Cuisine);
                    autoCompleteTextView5.setAdapter(adapter);

                    //to get selected value add item click listener
                    autoCompleteTextView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String search = autoCompleteTextView5.getText().toString().trim();
                            if(!search.isEmpty()){
                                PlayOn("&cuisineType="+search);
                            }
                            //Toast.makeText(getActivity(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(type.equals("Search By Meal")){
                    textInputLayout1.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, Meal);
                    autoCompleteTextView1.setAdapter(adapter);

                    //to get selected value add item click listener
                    autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String search = autoCompleteTextView1.getText().toString().trim();
                            if(!search.isEmpty()){
                                PlayOn("&mealType="+search);
                            }
                            //Toast.makeText(getActivity(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(type.equals("Search By Dish")){
                    textInputLayout2.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, Dish);
                    autoCompleteTextView2.setAdapter(adapter);

                    //to get selected value add item click listener
                    autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String search = autoCompleteTextView2.getText().toString().trim();
                            if(!search.isEmpty()){
                                PlayOn("&dishType="+search);
                            }
                            //Toast.makeText(getActivity(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(type.equals("Search By Health")){
                    textInputLayout4.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, Health);
                    autoCompleteTextView4.setAdapter(adapter);

                    //to get selected value add item click listener
                    autoCompleteTextView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String search = autoCompleteTextView4.getText().toString().trim();
                            if(!search.isEmpty()){
                                PlayOn("&health="+search);
                            }
                            //Toast.makeText(getActivity(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //Toast.makeText(getActivity(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipe = til.getEditText().getText().toString().trim();
                if(!recipe.isEmpty())
                {
                    PlayOn(recipe);
                }
                else
                {
                    Toast.makeText(getActivity(), "Please enter query to search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final String item = searchRecipeAdapter.getQuotes().get(position);
                final String item2 = searchRecipeAdapter.getAuthors().get(position);

                searchRecipeAdapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        searchRecipeAdapter.restoreItem(item, item2, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void enableSwipeFav() {
        SwipeToFav swipeToFav = new SwipeToFav(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final String item = searchRecipeAdapter.getQuotes().get(position);
                final String item2 = searchRecipeAdapter.getAuthors().get(position);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference();
                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateTime = sdf.format(new Date());

                RecipeModel helperClass = new RecipeModel(item,item2);
                reference.child("users").child(currentuser).child("fav").child(currentDateTime).setValue(helperClass);

                searchRecipeAdapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Added to favourites.", Snackbar.LENGTH_LONG);

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToFav);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void PlayOn(String recipe) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Please Wait");
        progressDialog.setTitle("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        //String url = "https://zenquotes.io/api/quotes";
        String url = "https://api.edamam.com/search?q=" + recipe +"&app_id=0d4818ae&app_key=ed42b94f786d8e7da794ac82fb10da22";
        Log.i(TAG, "PlayOn: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the API response here
                        try {
                            Log.i(TAG, "onResponse: "+ response);
                            JSONObject responseJson = new JSONObject(response);
                            JSONArray hits = responseJson.getJSONArray("hits");
                            int num = hits.length();
                            if(num>75)
                                num = 75;

                            arrayList.clear();
                            arrayList2.clear();
                            arrayList3.clear();
                            arrayList4.clear();

                            for (int i = 0; i < num; i++) {
                                JSONObject hit = hits.getJSONObject(i);
                                JSONObject recipe = hit.getJSONObject("recipe");

                                String label = recipe.getString("label");
                                String image = recipe.getString("image");
                                String source = recipe.getString("source");
                                String url = recipe.getString("url");
                                String ing = recipe.getString("ingredients");
                                String cal = recipe.getString("calories");
                                String tm = recipe.getString("totalTime");
                                //String inst = recipe.getString("ins");
                                Log.i(TAG, "onResponse: ing" + ing);
                                //Log.i(TAG, "onResponse: imst" + inst);

                                Log.i(TAG, "onResponse: " + label + "\n" + source);

                                // Do something with the recipe data

                            arrayList.add(label);
                            arrayList2.add("Recipe from -" +(source));
                            arrayList3.add(image);
                            arrayList4.add(url);
                            arrayList5.add(ing);
                            arrayList6.add(cal);
                            arrayList7.add(tm);
                            //arrayList6.add(inst);

                        }
                            Log.i(TAG, "onResponse: ohno");
                        searchRecipeAdapter = new SearchRecipeAdapter(getActivity(), arrayList, arrayList2, arrayList3,arrayList4,arrayList5, arrayList6, arrayList7);
                        recyclerView.setAdapter(searchRecipeAdapter); // set the Adapter to RecyclerView
                        progressDialog.dismiss();

                        searchRecipeAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(getActivity(), "Could not fetch data. Restart App " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
        Log.i(TAG, "PlayOn: jj");
    }
}