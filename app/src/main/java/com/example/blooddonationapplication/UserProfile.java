package com.example.blooddonationapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    private TextView textView,textView1,textView2,textView3,textView4;
    private FirebaseAuth firebaseAuth;
    ImageView img;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        textView = findViewById(R.id.nametxt);

        textView1 = findViewById(R.id.bloodtxt);

        textView2 = findViewById(R.id.citytxt);
        textView3 = findViewById(R.id.phonetxt);
        textView4 = findViewById(R.id.emailtxt);
        img = findViewById(R.id.Profilepic);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("Userdata").child(Objects.requireNonNull(firebaseAuth.getUid()));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String pic = dataSnapshot.child("image").getValue(String.class);

                String name = "Name:" + dataSnapshot.child("name").getValue(String.class);
                String city = "Address :" + dataSnapshot.child("City").getValue(String.class);
                String Email = "Email:" + dataSnapshot.child("Email").getValue(String.class);
                String mob = "Mobile Number:" + dataSnapshot.child("Phone").getValue(String.class);
                String blood = "Blood:" + dataSnapshot.child("Bloodtype").getValue(String.class);

                Bitmap bitmap = ImageUtility.StringToBitMap(pic);
                if (bitmap != null){
                    img.setImageBitmap(bitmap);
                }

                textView.setText(name);
                textView1.setText(blood);
                textView2.setText(city);
                textView3.setText(mob);
                textView4.setText(Email);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("dsafsd", "onCancelled: ");
            }
        });
    }
}
