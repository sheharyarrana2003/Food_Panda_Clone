package com.example.foodpanda;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import java.util.stream.Collectors;

public class HOME extends AppCompatActivity implements RecyclerClickInterface {
    TextView tvUser;
    EditText etSearch;
    ImageView account, cartid,ivDeliveryIcon;
    RecyclerView rcItems;
    List<Item> items = new ArrayList<>();
    List<Item> filteredItems = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;
    Query query;
    boolean search=false;
    boolean other,deliveryBoy;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartmain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvUser = findViewById(R.id.tvUser);
        account = findViewById(R.id.account);
        cartid = findViewById(R.id.cartid);
        etSearch = findViewById(R.id.etSearch);
        rcItems = findViewById(R.id.rcItems);
        ivDeliveryIcon=findViewById(R.id.ivDeliveryIcon);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        if (user != null) {
            String currentEmail = user.getEmail();
            query = FirebaseDatabase.getInstance().getReference("UserDetails")
                    .orderByChild("Email")
                    .equalTo(currentEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userName = userSnapshot.child("User Name").getValue(String.class);
                         deliveryBoy= userSnapshot.child("Is Delivery").getValue(boolean.class);
                         other= userSnapshot.child("Is Other").getValue(boolean.class);
                        tvUser.setText(userName);
                        if(deliveryBoy){
                            ivDeliveryIcon.setVisibility(View.VISIBLE);
                            return;
                        }
                        else{
                            ivDeliveryIcon.setVisibility(View.GONE);
                        }

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(HOME.this, "Failed to load user details", Toast.LENGTH_SHORT).show();
                }
            });
        }
        addItemData();

        rcItems.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ItemAdapter(getApplicationContext(), items, this);
        rcItems.setAdapter(adapter);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HOME.this, profile_view.class);
                startActivity(i);
            }
        });


        cartid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HOME.this, cartItems.class));
            }
        });

        ivDeliveryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HOME.this, DeliveryBoy.class));
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void addItemData() {
        items.add(new Item("Burger Deal", "1000Rs", R.drawable.burgerdeals, "Enjoy our delicious burger deals with a variety of flavors", "KFC Iqbal Town"));
        items.add(new Item("Pasta", "500Rs", R.drawable.pasta, "Indulge in our creamy and savory pasta dishes.", "Eats and Bites Samanabad"));
        items.add(new Item("Wings", "200Rs", R.drawable.wings, "Crispy, juicy wings perfect for any occasion.", "Macdonald's Gulberg"));
        items.add(new Item("Fries", "150Rs", R.drawable.fries, "Golden, crunchy fries that are a perfect snack.", "Masala Fries Johar Town"));
        items.add(new Item("Steaks", "700Rs", R.drawable.steaks, "Savor our tender, juicy steaks cooked to perfection", "Steaks Room Androon"));
        items.add(new Item("Salad", "300Rs", R.drawable.salad, "Fresh and healthy salads with a variety of dressings.", "Green Salad Cafe Gulberg"));
        items.add(new Item("Tacos", "400Rs", R.drawable.tacos, "Spicy and flavorful tacos with a variety of fillings.", "Taco Bell Model Town"));
        items.add(new Item("Ice Cream", "200Rs", R.drawable.icecream, "Creamy and refreshing ice cream in multiple flavors.", "Ice Cream Factory Johar Town"));
        items.add(new Item("Sushi", "1200Rs", R.drawable.sushi, "Authentic Japanese sushi with fresh ingredients.", "Sushi Master DHA"));
        items.add(new Item("Sandwich", "250Rs", R.drawable.sandwich, "Tasty sandwiches with fresh fillings.", "Sandwich Club Iqbal Town"));
        items.add(new Item("Smoothie", "180Rs", R.drawable.smoothi, "Healthy and refreshing smoothies in different flavors.", "Smoothie King Samanabad"));
        items.add(new Item("Donuts", "150Rs", R.drawable.donuts, "Soft and sweet donuts with a variety of glazes.", "Donut Delight Johar Town"));
        items.add(new Item("BBQ Platter", "1500Rs", R.drawable.bbq, "A delicious assortment of BBQ meats.", "BBQ Tonight Liberty"));
        items.add(new Item("Pizza", "800Rs", R.drawable.food_image, "Delicious cheesy pizza with various toppings.", "Pizza Hut Liberty"));

    }

    private void filterItems(String query) {
        search=true;
        filteredItems.clear();
        if (query.isEmpty()) {
            filteredItems.addAll(items);
        }
        else {
            filteredItems.addAll(items.stream()
                    .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        adapter.updateList(filteredItems);
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(HOME.this, ItemView.class);
        if(!search){
            i.putExtra("item",items.get(position));
        }
        else{
            i.putExtra("item", filteredItems.get(position));
        }
        startActivity(i);
    }
}
