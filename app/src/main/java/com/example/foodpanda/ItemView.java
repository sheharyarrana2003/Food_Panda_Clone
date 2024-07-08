package com.example.foodpanda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ItemView extends AppCompatActivity {
    ImageView ivPicFood;
    TextView tvItemName, item_description, tvSite, tvPrice;
    Button order_button;
    RadioButton removeorder, cancelorder, Callme;
    FirebaseAuth auth;
    FirebaseUser userNow;
    int TotalItems = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.itemlayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ivPicFood = findViewById(R.id.ivPicFood);
        tvItemName = findViewById(R.id.tvItemName);
        item_description = findViewById(R.id.item_description);
        tvSite = findViewById(R.id.tvSite);
        tvPrice = findViewById(R.id.tvPrice);
        removeorder = findViewById(R.id.removeorder);
        cancelorder = findViewById(R.id.cancelorder);
        Callme = findViewById(R.id.CallMe);
        order_button = findViewById(R.id.order_button);
        auth = FirebaseAuth.getInstance();
        userNow = auth.getCurrentUser();
        String UserEmail = userNow.getEmail();


        Item item = (Item) getIntent().getSerializableExtra("item");
        if (item != null) {
            tvItemName.setText(item.getName());
            tvPrice.setText(item.getCost());
            item_description.setText(item.getDescription());
            tvSite.setText(item.getLocation());
            ivPicFood.setImageResource(item.getImage());
        }

        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String priceString=tvPrice.getText().toString();
                try {
                    String numericString = priceString.replaceAll("[^0-9]", "");

                    int price = Integer.parseInt(numericString);

                   Global.Payment+=price;
                } 
                catch (NumberFormatException e) {
                    Toast.makeText(ItemView.this, "Invalid Format", Toast.LENGTH_SHORT).show();
                }

                Global.TotalItems+=1;

                boolean isRemove = removeorder.isChecked();
                boolean isCancel = cancelorder.isChecked();
                boolean isCall = Callme.isChecked();
                String itemAvailability;
                if (isRemove) {
                    itemAvailability = "Remove from my order";
                }
                else if (isCancel) {
                    itemAvailability = "Cancel entire order";
                }
                else if (isCall) {
                    itemAvailability = "Call and Confirm";
                }
                else {
                    itemAvailability = "None";
                }
                HashMap<String, Object> UserItems = new HashMap<>();
                UserItems.put("Email", UserEmail);
                UserItems.put("Item Name", item.getName());
                UserItems.put("Price", item.getCost());
                UserItems.put("Image", item.getImage());
                UserItems.put("isAvailability", itemAvailability);

                FirebaseDatabase.getInstance().getReference().child("UserItems")
                        .push()
                        .setValue(UserItems)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ItemView.this, "Item Added", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ItemView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}
