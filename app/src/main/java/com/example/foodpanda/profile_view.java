package com.example.foodpanda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class profile_view extends AppCompatActivity {
    TextView tvName, tvLocation, tvEmailAddress, tvPhoneNumber, tvTotalOrders;
    FirebaseAuth auth1;
    FirebaseUser user;
    Query query;
    Button btnLogOut, btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profileView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvName = findViewById(R.id.tvName);
        tvLocation = findViewById(R.id.tvLocation);
        tvEmailAddress = findViewById(R.id.tvEmailAddress);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        auth1 = FirebaseAuth.getInstance();
        user = auth1.getCurrentUser();

        if (user != null) {
            String currentEmail = user.getEmail();
            query = FirebaseDatabase.getInstance().getReference("UserDetails")
                    .orderByChild("Email")
                    .equalTo(currentEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String name = userSnapshot.child("User Name").getValue(String.class);
                        String location = userSnapshot.child("Location").getValue(String.class);
                        String email = userSnapshot.child("Email").getValue(String.class);
                        String phoneNumber = userSnapshot.child("Phone Number").getValue(String.class);
                        Integer totalOrders = userSnapshot.child("Total Orders").getValue(Integer.class);

                        tvName.setText(name);
                        tvLocation.setText(location);
                        tvEmailAddress.setText(email);
                        tvPhoneNumber.setText(phoneNumber);
                        tvTotalOrders.setText(String.valueOf(totalOrders));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(profile_view.this, "Failed to load user details", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alterLogOut = new AlertDialog.Builder(profile_view.this);
                alterLogOut.setTitle("Confirmation");
                alterLogOut.setMessage("Do you want to Log Out?....");
                alterLogOut.setPositiveButton("LogOut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth1.signOut();
                        startActivity(new Intent(profile_view.this, MainActivity.class));
                        finish();
                        Global.TotalOrders = 0;
                    }
                });
                alterLogOut.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alterLogOut.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile_view.this, EditDetails.class);
                intent.putExtra("User Name", tvName.getText());
                intent.putExtra("Location", tvLocation.getText());
                intent.putExtra("Phone Number", tvPhoneNumber.getText());
                intent.putExtra("Email", tvEmailAddress.getText());
                intent.putExtra("Key", user.getEmail());
                startActivity(intent);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alterDelete = new AlertDialog.Builder(profile_view.this);
                alterDelete.setTitle("Confirmation");
                alterDelete.setMessage("Do you want to Delete your Account?....");
                alterDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (user != null) {
                            String currentEmail = user.getEmail();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserDetails");
                            Query userQuery = userRef.orderByChild("Email").equalTo(currentEmail);

                            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                                        userSnapshot.getRef().removeValue();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(profile_view.this, "Failed to find user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            DatabaseReference userRefItems = FirebaseDatabase.getInstance().getReference("UserItems");
                            Query userQueryItems = userRefItems.orderByChild("Email").equalTo(currentEmail);

                            userQueryItems.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                                        userSnapshot.getRef().removeValue();
                                    }
                                    auth1.signOut();
                                    user.delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(profile_view.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(profile_view.this, MainActivity.class));
                                                    finish();
                                                    Global.TotalOrders = 0;
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(profile_view.this, "Failed to delete account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(profile_view.this, "Failed to find user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                alterDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alterDelete.show();
            }
        });
    }
}
