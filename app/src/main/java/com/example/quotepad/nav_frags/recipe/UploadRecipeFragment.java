package com.example.quotepad.nav_frags.recipe;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quotepad.R;
import com.example.quotepad.model.RecipeModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputLayout upload_recipe_name, upload_ing, upload_ins, upload_prep;
    private Button upload_btn;
    private AutoCompleteTextView upload_cuisine;
    private CheckBox publicity;

    private String type;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    public UploadRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadQuoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadRecipeFragment newInstance(String param1, String param2) {
        UploadRecipeFragment fragment = new UploadRecipeFragment();
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
        return inflater.inflate(R.layout.fragment_upload_recipe, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        upload_recipe_name = getActivity().findViewById(R.id.upload_recipe_name);
        upload_ing = getActivity().findViewById(R.id.upload_recipe_ing);
        upload_ins = getActivity().findViewById(R.id.upload_recipe_ins);
        upload_prep = getActivity().findViewById(R.id.upload_recipe_prep);
        upload_cuisine = getActivity().findViewById(R.id.upload_cusineType);
        publicity = getActivity().findViewById(R.id.upload_publicity);
        upload_btn = getActivity().findViewById(R.id.upload_recipe_btn);

        //We will use this data to inflate the drop-down items
        String[] Subjects = new String[]{   "American", "Britsh", "Asian", "Caribbean",
                                            "Chinese", "Central Europe", "Eastern Europe", "French",
                                            "Indian", "Italian", "Kosher", "Mediterranean", "Middle Eastern",
                                            "Mexican", "Nordic", "South American", "South East Asian"};

        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, Subjects);
        upload_cuisine.setAdapter(adapter);

        //to get selected value add item click listener
        upload_cuisine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = upload_cuisine.getText().toString().trim();
                //Toast.makeText(getActivity(), "" + upload_cuisine.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get all the values
                String recipe_name = upload_recipe_name.getEditText().getText().toString().trim();
                String recipe_ing = upload_ing.getEditText().getText().toString().trim();
                String recipe_inst = upload_ins.getEditText().getText().toString().trim();
                String recipe_prep = upload_prep.getEditText().getText().toString().trim();

                upload_recipe_name.setErrorEnabled(false);
                upload_ing.setErrorEnabled(false);
                upload_ins.setErrorEnabled(false);
                upload_prep.setErrorEnabled(false);

                if(TextUtils.isEmpty(recipe_name))
                {
                    upload_recipe_name.setError("Type your recipe name");
                }
                else if(TextUtils.isEmpty(recipe_ing))
                {
                    upload_ing.setError("Type Ingredients");
                }
                else if(TextUtils.isEmpty(recipe_inst))
                {
                    upload_ins.setError("Type Instructions");
                }
                else if(TextUtils.isEmpty(recipe_prep))
                {
                    upload_prep.setError("Type Preparation Time");
                }
                else if(TextUtils.isEmpty(type)){
                    upload_cuisine.setError("Please select a cuisine Type for your recipe");
                }
                else
                {
                    ProgressDialog pd = new ProgressDialog(getActivity());

                    pd.setMessage("Please Wait");
                    pd.setTitle("Saving Data...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd LLLL,yyyy HH:mm:ss", Locale.getDefault());
                    String currentDateTime = sdf.format(new Date());

                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference();
                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    RecipeModel helperClass = new RecipeModel(recipe_name ,recipe_ing, recipe_inst, recipe_prep, type,currentDateTime,"0");
                    reference.child("users").child(currentuser).child("recipe").child(currentDateTime).setValue(helperClass);

                    Query checkUser = reference.child("users").orderByChild("id").equalTo(currentuser);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                pd.hide();
                                String username = snapshot.child(currentuser).child("username").getValue(String.class);
                                String new_id = currentDateTime + ";" + currentuser;
                                if(publicity.isChecked())
                                {
                                    RecipeModel discoverModel = new RecipeModel(recipe_name ,recipe_ing, recipe_inst, recipe_prep, currentDateTime,type, username, new_id);
                                    rootNode.getReference("recipes").child(new_id).setValue(discoverModel);
                                    reference.child("users").child(currentuser).child("recipe").child(currentDateTime).child("publicity").setValue("1");

                                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                                }

                                publicity.setChecked(false);
                                upload_recipe_name.getEditText().setText("");
                                upload_cuisine.setText("");

                                Log.i(TAG, "onDataChange: " + username);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            pd.hide();
                        }
                    });
                }
            }
        });
    }
}