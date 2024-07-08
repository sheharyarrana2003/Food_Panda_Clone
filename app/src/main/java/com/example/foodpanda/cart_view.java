package com.example.foodpanda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cart_view extends AppCompatActivity {
    TextView tvTotal, tvPayment;
    Button confirmOrder;
    RadioButton jazzCash, cashDelivery, debitCard;
    FirebaseAuth auth;
    FirebaseUser user;
    String userNumber;
    List<deliveryItems> deliverItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartmain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvTotal = findViewById(R.id.tvTotal);
        confirmOrder = findViewById(R.id.confirmOrder);
        jazzCash = findViewById(R.id.jazzCash);
        cashDelivery = findViewById(R.id.cashDelivery);
        debitCard = findViewById(R.id.debitCard);
        tvPayment = findViewById(R.id.tvPayment);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String currentEmail = user.getEmail();
        tvTotal.setText(String.valueOf("Total Number of Items " + Global.TotalItems));
        tvPayment.setText(Global.Payment + ".0 Rs");

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.Payment != 0) {
                    Global.TotalOrders += 1;
                    Toast.makeText(cart_view.this, "Thank You for your Order. \n Enjoy your meal.", Toast.LENGTH_SHORT).show();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");
                    Query query = databaseReference.orderByChild("Email").equalTo(currentEmail);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    userNumber = userSnapshot.child("Phone Number").getValue(String.class);
                                    int previousOrders = userSnapshot.child("Total Orders").getValue(int.class);
                                    previousOrders += Global.TotalOrders;
                                    userSnapshot.getRef().child("Total Orders").setValue(previousOrders)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(cart_view.this, "Orders updated", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(cart_view.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(cart_view.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(cart_view.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    DatabaseReference userOrders = FirebaseDatabase.getInstance().getReference("UserItems");
                    Query userOrderQuery = userOrders.orderByChild("Email").equalTo(currentEmail);

                    userOrderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            deliverItems.clear();
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String name = userSnapshot.child("Item Name").getValue(String.class);
                                String price = userSnapshot.child("Price").getValue(String.class);
                                String availability = userSnapshot.child("isAvailability").getValue(String.class);
                                deliverItems.add(new deliveryItems(name, price, availability));
                            }

                            userOrder userOrder = new userOrder(userNumber, deliverItems,Global.Payment);
                            FirebaseDatabase.getInstance().getReference().child("UserOrders")
                                    .push()
                                    .setValue(userOrder)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(cart_view.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(cart_view.this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                userSnapshot.getRef().removeValue();
                            }

                            Global.TotalItems = 0;
                            Global.Payment = 0;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(cart_view.this, "Cannot get Data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(cart_view.this, "No item Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

