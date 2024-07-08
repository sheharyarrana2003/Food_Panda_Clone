package com.example.foodpanda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class activity_signup extends AppCompatActivity  {

    EditText etusernamesign, etemailsignup, etpasswordsign, etLocation, eTCONTACTNO;
    Button btnCancelsignup, btnLoginsignup;
    TextView tvsignuptext;
    FirebaseAuth auth;
    RadioButton deliveryBoy,other;
    RadioGroup userGroup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartmain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etusernamesign = findViewById(R.id.etusernamesign);
        etemailsignup = findViewById(R.id.etemailsignup);
        etpasswordsign = findViewById(R.id.etpasswordsign);
        etLocation = findViewById(R.id.etLocation);
        eTCONTACTNO = findViewById(R.id.eTCONTACTNO);
        btnCancelsignup = findViewById(R.id.btnCancelsignup);
        btnLoginsignup = findViewById(R.id.btnLoginsignup);
        tvsignuptext = findViewById(R.id.tvsignuptext);
        auth=FirebaseAuth.getInstance();
        deliveryBoy=findViewById(R.id.deliveryBoy);
        other=findViewById(R.id.other);
        userGroup=findViewById(R.id.userGroup);


        btnLoginsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etusernamesign.getText().toString().trim();
                String email = etemailsignup.getText().toString().trim();
                String password = etpasswordsign.getText().toString();
                String location = etLocation.getText().toString().trim();
                String contactno = eTCONTACTNO.getText().toString().trim();
                boolean isDeliveryBoy=deliveryBoy.isChecked();
                boolean isOther=other.isChecked();


                if (username.isEmpty()) {
                    etusernamesign.setError("Enter Username!");
                }
                if (email.isEmpty()) {
                    etemailsignup.setError("Invalid email address");
                }
                if (password.isEmpty()) {
                    etpasswordsign.setError("Invalid password");
                }
                if (location.isEmpty()) {
                    etLocation.setError("Location Require");
                }
                if (contactno.isEmpty()) {
                    eTCONTACTNO.setError("Invalid Contact No");
                }
                if(password.length()<6){
                    etpasswordsign.setError("Password Length atleast 6 charcters or digits");
                }
                if(contactno.length()<11 | contactno.length()>11){
                    eTCONTACTNO.setError("Number length must be 11 digits");
                }
                if (userGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(activity_signup.this, "Select one option from the three", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(activity_signup.this, "User Created", Toast.LENGTH_SHORT).show();
                                    HashMap<String,Object> data=new HashMap<>();
                                    data.put("User Name",username);
                                    data.put("Email",email);
                                    data.put("Location",location);
                                    data.put("Phone Number",contactno);
                                    data.put("Total Orders",0);
                                    data.put("Is Delivery", isDeliveryBoy);
                                    data.put("Is Other", isOther);
                                    FirebaseDatabase.getInstance().getReference().child("UserDetails")
                                            .push()
                                            .setValue(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(activity_signup.this, "Added", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(activity_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent intent = new Intent(activity_signup.this, HOME.class);
                                    intent.putExtra("isDeliveryBoy", isDeliveryBoy);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(activity_signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        btnCancelsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity_signup.this, "Back to Login page!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(activity_signup.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
