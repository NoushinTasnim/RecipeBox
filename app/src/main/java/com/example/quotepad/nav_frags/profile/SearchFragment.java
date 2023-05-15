package com.example.quotepad.nav_frags.profile;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quotepad.R;
import com.example.quotepad.adapter.UserListAdapter;
import com.example.quotepad.model.UserModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextInputLayout til;
    private ImageButton btn;
    private RecyclerView recyclerView;
    UserListAdapter adapter;
    ArrayList<UserModel> list=new ArrayList<>();


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        til = getActivity().findViewById(R.id.search_bar);
        btn = getActivity().findViewById(R.id.img_btn);
        recyclerView = getView().findViewById(R.id.recycler_user);

        adapter = new UserListAdapter(getContext(),list);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());

                progressDialog.setMessage("Please Wait");
                progressDialog.setTitle("Fetching Data...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String username = til.getEditText().getText().toString().trim();
                if(!username.isEmpty())
                {
                    list.clear();
                    FirebaseDatabase.getInstance().getReference("emails").orderByChild("username").startAt(username).endAt(username + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.exists())
                                    {
                                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                                        {
                                            Log.i(TAG, "onDataChange: " + dataSnapshot.getValue());
                                            String id = dataSnapshot.child("id").getValue(String.class);
                                            Log.i(TAG, "onDataChange:id " + id);

                                            FirebaseDatabase.getInstance().getReference("users").orderByChild("id").equalTo(id)
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                            if(snapshot1.exists()){
                                                                Log.i(TAG, "onDataChangeval: " + snapshot1.child(id).getValue());
                                                                Log.i(TAG, "onDataChangevchill: " + snapshot1.getChildren());
                                                                //Log.i(TAG, "onDataChange: " + snapshot1);
                                                                UserModel notification = snapshot1.child(id).getValue(UserModel.class);

                                                                //Log.i(TAG, "onDataChange: " + notification);

                                                                list.add(notification);
                                                            }
                                                            adapter.notifyDataSetChanged();
                                                            progressDialog.dismiss();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                        }
                                    }
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                }
                else
                {
                    progressDialog.dismiss();
                }
            }
        });
    }
}