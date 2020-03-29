package com.example.blooddonationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    static FirebaseAuth mAuth;
    //String[]city = {"City","Betul","Bhopal","Burhanpur","Chhindwara","Dewas","Gwalior","Harda","Hosangabad","Indoure","Itarsi","Jabalpur","Katni","Khandwa","Rewa","Sagar"};
    //String[]blood = {"A+","A-","B+","B-","AB+","AB-","O+","o-"};

    private ImageView imageview;
    private int GALLERY = 1, CAMERA = 2;
    EditText name ,Email,passwd, edtphone,bloodtxt,citytxt,passwd2;
    String sentotp,email,password,password2,Name,phone,PHOTO,citystr,bloodstr;
    DatabaseReference databaseReference ;
    Button btn1;




    Map<String,String> User = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.edtname);
        Email = findViewById(R.id.edtemail);
        passwd = findViewById(R.id.edtpasswd);

        passwd2 = findViewById(R.id.edtpasswd2);
        bloodtxt = findViewById(R.id.bloodedt);
        citytxt = findViewById(R.id.cityedt);

        edtphone = findViewById(R.id.edtphone);
        btn1 = findViewById(R.id.btn11);
        imageview = findViewById(R.id.imagev);



    }

    @Override
    protected void onStart()
    {
        super.onStart();

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();

            }
        });
                btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    public void register() {
        Name = name.getText().toString();
        phone = edtphone.getText().toString();
        email = Email.getText().toString();
        password = passwd.getText().toString();
        password2 = passwd2.getText().toString();
        citystr = citytxt.getText().toString();
        bloodstr = bloodtxt.getText().toString();
        PHOTO = imageview.getImageMatrix().toString();

        if (!email.isEmpty() && !password.isEmpty() && !PHOTO.isEmpty() && !bloodstr.isEmpty() && !citystr.isEmpty() && ! Name.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                String uid = FirebaseAuth.getInstance().getUid();

                                User.put("image",PHOTO);
                                User.put("id",uid);
                                User.put("name",Name);
                                User.put("Email",email);
                                User.put("Phone",phone);
                                User.put("Bloodtype",bloodstr);
                                User.put("City",citystr);
                                //data is adding in user table
                                databaseReference.child("Userdata").child(uid).setValue(User);

                              //  databaseReference.child(uid).setValue(User);

                                Toast.makeText(getApplicationContext(), "Signup successfull" , Toast.LENGTH_SHORT).show();
                                genrateotp();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        else{
            Toast.makeText(getApplicationContext(),"plese fill",Toast.LENGTH_SHORT).show();
            }
    }

    public void genrateotp() {

        String phone = edtphone.getText().toString();

        if (phone.length() < 10) {
            Toast.makeText(getApplicationContext(), "plese enter valid phonenumber", Toast.LENGTH_SHORT).show(); }
        else{
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                   "+91"+phone,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks  );
    }
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                }
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    sentotp = s;
                    Toast.makeText(getApplicationContext(),"otp sent",Toast.LENGTH_SHORT).show();
                    Intent intt = new Intent(Register.this, otpverification.class);
                    intt.putExtra("otp",sentotp);

                    startActivity(intt);

                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),"exeption",Toast.LENGTH_SHORT).show();

                }
    };







    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "GALLERY",
                "CAMERA"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY && data != null) {
            Uri contentURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                imageview.setImageBitmap(bitmap);

            } catch (IOException e) {
                Toast.makeText(Register.this, "Failed!", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA && data != null) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(thumbnail);
            PHOTO = ImageUtility.BitMapToString(thumbnail);

            //data is addind in user table

        }

    }

}





//----------------------------------------------------------------------

