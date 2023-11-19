package com.example.dapm_food.Chef.ChefActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dapm_food.Chef.ChefAdapter.ChefOrderDishesAdapter;
import com.example.dapm_food.Chef.ChefModel.ChefPendingOrders;
import com.example.dapm_food.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chef_order_dishes extends AppCompatActivity {
    //May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
    RecyclerView recyclerViewdish;
    private List<ChefPendingOrders> chefPendingOrdersList;
    private ChefOrderDishesAdapter adapter;
    DatabaseReference reference;
    String RandomUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_order_dishes);
        recyclerViewdish = findViewById(R.id.Recycle_orders_dish);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(this));
        chefPendingOrdersList = new ArrayList<>();
        Cheforderdishes();

    }

    private void Cheforderdishes() {
//lấy một chuỗi được truyền từ Intent với key "RandomUID"
        RandomUID = getIntent().getStringExtra("RandomUID");
//Tham chiếu tới nút
        reference = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
       //tham chiếu đến reference để lấy dữ liệu duy nhất 1 lần , sau đó tự gỡ bỏ
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //dữ liệu được lấy trên fbase trả về dataSnap
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chefPendingOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChefPendingOrders chefPendingOrders = snapshot.getValue(ChefPendingOrders.class);
                    chefPendingOrdersList.add(chefPendingOrders);
                }
                adapter = new ChefOrderDishesAdapter(Chef_order_dishes.this, chefPendingOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
