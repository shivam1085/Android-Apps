package com.example.blooddonationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.RequestAdupter;
import Model.Request;

public class MyDoner extends AppCompatActivity {
    Activity context;
    ListView listView;
    EditText edtSearch;
    ArrayList<Request> datasource;
    RequestAdupter adapter;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doner);
        edtSearch = findViewById(R.id.searchtxt);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {

        super.onStart();

        context = MyDoner.this;
        datasource = new ArrayList<>();
        listView = findViewById(R.id.donerlist11);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Userdata").child(FirebaseAuth.getInstance().getUid()).child("Doner");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    datasource.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Request request = snapshot.getValue(Request.class);

                        datasource.add(request);

                    }
                    adapter = new RequestAdupter(context, datasource);
                    //calling Adapter
                    listView.setAdapter(adapter);

                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                    Toast.makeText(context, "Exeption", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }




}







