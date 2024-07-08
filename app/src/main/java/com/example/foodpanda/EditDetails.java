package com.example.foodpanda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditDetails extends AppCompatActivity {
    EditText etNameEdit, etEditEmail, etEditPhone, etEditLocation;
    Button btnCancel, btnSave;
    FirebaseAuth auth;
    FirebaseUser user;
    String UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNameEdit = findViewById(R.id.etNameEdit);
        etEditEmail = findViewById(R.id.etEditEmail);
        etEditPhone = findViewById(R.id.etEditPhone);
        etEditLocation = findViewById(R.id.etEditLocation);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        Intent i = getIntent();
        etNameEdit.setText(i.getStringExtra("User Name"));
        etEditLocation.setText(i.getStringExtra("Location"));
        etEditPhone.setText(i.getStringExtra("Phone Number"));
        etEditEmail.setText(i.getStringExtra("Email"));
        UserEmail = i.getStringExtra("Key");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String EditName = etNameEdit.getText().toString().trim();
                String EditLocation = etEditLocation.getText().toString().trim();
                String EditPhone = etEditPhone.getText().toString().trim();
                String EditEmail = etEditEmail.getText().toString().trim();


                if (EditName.isEmpty()) {
                    etNameEdit.setError("Name is required");
                    return;
                }
                if (EditLocation.isEmpty()) {
                    etEditLocation.setError("Location is required");
                    return;
                }
                if (EditPhone.isEmpty()) {
                    etEditPhone.setError("Phone Number is required");
                    return;
                }
                if (EditEmail.isEmpty()) {
                    etEditEmail.setError("Email is required");
                    return;
                }
                if(EditPhone.length()<11 | EditPhone.length()>11){
                    etEditPhone.setError("Phone Number length must be 11");
                    return;
                }


                HashMap<String, Object> editData = new HashMap<>();
                editData.put("User Name", EditName);
                editData.put("Location", EditLocation);
                editData.put("Phone Number", EditPhone);
                editData.put("Email", EditEmail);


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");
                Query query = databaseReference.orderByChild("Email").equalTo(UserEmail);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                                userSnapshot.getRef().updateChildren(editData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(EditDetails.this, "Data Updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(EditDetails.this, profile_view.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EditDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                        else {
                            Toast.makeText(EditDetails.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditDetails.this, "No Updation", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditDetails.this, profile_view.class));
                finish();
            }
        });
    }
}
