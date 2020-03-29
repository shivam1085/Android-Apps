package com.example.blooddonationapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class BloodRequest extends AppCompatActivity {
    HashMap <String,String> Requestlist = new HashMap<>();
    EditText editbloodtype,edtcity;
    String blood,city,name,phone,image,address;
    ImageView imgview;
    DatabaseReference databaseReference,databaseReference2;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);
        editbloodtype = findViewById(R.id.edttxtblood);
        edtcity = findViewById(R.id.edttxtcity);

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference2 = firebaseDatabase.getInstance().getReference();
        databaseReference = firebaseDatabase.getInstance().getReference("Userdata").child(FirebaseAuth.getInstance().getUid());
    }

    public void Request(View view) {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name= dataSnapshot.child("name").getValue(String.class);
                phone = dataSnapshot.child("Phone").getValue(String.class);

                blood = editbloodtype.getText().toString();
                city = edtcity.getText().toString();

                if(!blood.isEmpty() && !city.isEmpty()) {

                    String uid = FirebaseAuth.getInstance().getUid();

                    Requestlist.put("name", name);
                    Requestlist.put("blood", blood);
                    Requestlist.put("city", city);
                    Requestlist.put("phone", phone);
                    Requestlist.put("Uid",uid);
                    databaseReference2.child("Requests").child(uid).setValue(Requestlist);
                    Toast.makeText(getApplicationContext(),"Request registerd",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"plese fill all",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("dsafsd", "onCancelled: ");
            }
        });




    }
}




