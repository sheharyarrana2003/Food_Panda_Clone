package com.example.foodpanda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class cartItems extends AppCompatActivity {
    RecyclerView rcCartItems;
    FirebaseAuth auth;
    FirebaseUser user;
    List<CartItem> cartItemsList = new ArrayList<>();
    CartAdapter cartAdapter;
    Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rcCartItems = findViewById(R.id.rcCartItems);
        btnProceed=findViewById(R.id.btnProceed);
        rcCartItems.setLayoutManager(new GridLayoutManager(this, 2));
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            String currentEmail = user.getEmail();
            Query query = FirebaseDatabase.getInstance().getReference("UserItems")
                    .orderByChild("Email").equalTo(currentEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String itemName = userSnapshot.child("Item Name").getValue(String.class);
                        String itemPrice = userSnapshot.child("Price").getValue(String.class);
                        int itemImage = userSnapshot.child("Image").getValue(int.class);
                        cartItemsList.add(new CartItem(itemName, itemPrice, itemImage));
                    }
                    cartAdapter = new CartAdapter(cartItemsList, cartItems.this);
                    rcCartItems.setAdapter(cartAdapter);
                    cartAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(cartItems.this, "Failed to load Data", Toast.LENGTH_SHORT).show();
                }
            });
        }
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cartItems.this, cart_view.class));
                finish();
            }
        });
    }
}
