package com.example.blooddonationapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.RequestAdupter;
import Model.Request;

public class Homeactivity extends AppCompatActivity {
    Activity context;
    ListView listView;
    EditText edtSearch;
    String srcstring = "";
    Button btnsrc;
    ArrayList<Request> datasource;
    RequestAdupter adapter;
    DatabaseReference databaseReference,databaseReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);
        edtSearch = findViewById(R.id.searchtxt);
        btnsrc = findViewById(R.id.btnsrc);
    }

    public  void Doner(View view)
    {
        Intent intt2 = new Intent(Homeactivity.this, AcceptedRequests.class);
        startActivity(intt2);
    }
    public  void Doners(View view)
    {
        Intent intt2 = new Intent(Homeactivity.this, MyDoner.class);
        startActivity(intt2);
    }

    public  void profile(View v){
        Intent intt1 = new Intent(Homeactivity.this,UserProfile.class);
        startActivity(intt1);

    }
    public void requestblood(View v)
    {
        Intent intt = new Intent(Homeactivity.this,BloodRequest.class);
        startActivity(intt);
       // databaseReference = databaseReference.child("Requests");

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

        context = Homeactivity.this;
        datasource = new ArrayList<>();
        listView = findViewById(R.id.listofrq);

        databaseReference = FirebaseDatabase.getInstance().getReference("Requests");


btnsrc.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        srcstring = edtSearch.getText().toString();
        String search = "";
        if (srcstring.length()>0) {
            search = srcstring.substring(0, 1).toUpperCase();
        }

        //-----------------------------------
                //searching and showig the list of reuests
        //----------------------------------
        Query query = databaseReference.orderByChild("city").startAt(search);

        query.addValueEventListener(new ValueEventListener() {
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


                    // list item click listener
                    listView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                  Request request = datasource.get(i);
                                  CallUpdateAndDeleteDialog(request.getUid(), request.getName(),request.getBlood(),request.getPhone(),request.getCity());
                                }
                            });

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
    private void CallUpdateAndDeleteDialog(final String userid, final String username, final String blood, final String monumber, final String City ) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Homeactivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog, null);
        dialogBuilder.setView(dialogView);

        //Access Dialog views
        final TextView Textname = dialogView.findViewById(R.id.updateTextname);
        final TextView TextBlood =  dialogView.findViewById(R.id.updateTextemail);
        final TextView Textmobileno =  dialogView.findViewById(R.id.updateTextmobileno);
        final  TextView city = dialogView.findViewById(R.id.updateTextcity);
        final String uid =userid;
        Textname.setText(username);
        TextBlood.setText(blood);
        Textmobileno.setText(monumber);
        city.setText(City);


        final Button buttonDelete =  dialogView.findViewById(R.id.buttonDeleteUser);
        //username for set dialog title
        dialogBuilder.setTitle(username);
        final AlertDialog b = dialogBuilder.create();
        b.show();



        // Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                //Method for delete data
                deleteUser(userid);
                // Request adding as accepted request


                final Request doner = new Request();
                Request Areq = new Request();
               // for requested user
                databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Userdata").child(uid);

                //for current user


                databaseReference = FirebaseDatabase.getInstance().getReference().child("Userdata").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String name= dataSnapshot.child("name").getValue(String.class);
                        String phone = dataSnapshot.child("Phone").getValue(String.class);
                        String city = dataSnapshot.child("City").getValue(String.class);
                        String blood = dataSnapshot.child("Bloodtype").getValue(String.class);
                        doner.setName(name);
                        doner.setBlood(blood);
                        doner.setPhone(phone);
                        doner.setCity(city);

                            databaseReference2.child("Doner").child(FirebaseAuth.getInstance().getUid()).setValue(doner);
                            Toast.makeText(getApplicationContext(),"you are doner",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("dsafsd", "onCancelled: ");
                    }
                });


                //-------------------------------------------------
                Areq.setName(username);
                Areq.setBlood(blood);
                Areq.setPhone(monumber);
                Areq.setCity(City);
                //finding the path of user who loged in
               //finding the path of user who requested

                //saving request dat insside the user table
                databaseReference.child("AcceptedRequests").child(userid).setValue(Areq);



                b.dismiss();
            }
        });
    }
});
    }


    void deleteUser(String id) {
        //getting the specified User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference().child("Requests").child(id);
        //removing User
        DeleteReference.removeValue();
        Toast.makeText(getApplicationContext(), "Request Accepted ", Toast.LENGTH_LONG).show();
        //return true;


    }






}
