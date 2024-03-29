package com.example.dapm_food.Customerr.CustomerFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dapm_food.Categories;
import com.example.dapm_food.CategoryAdapter;
import com.example.dapm_food.Chef.ChefModel.UpdateDishModel;
import com.example.dapm_food.Customerr.CustomerAdapter.CustomerHomeAdapter;
import com.example.dapm_food.Customerr.CustomerModel.Customer;
import com.example.dapm_food.Item;
import com.example.dapm_food.R;
import com.example.dapm_food.SlideViewPayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
//May not be copied in any form
//Copyright belongs to Nguyen TrongTin. contact: email:tinnguyen2421@gmail.com
public class CustomerHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SlideViewPayer QuangCao;

    RecyclerView recyclerView,rcvhome;
    private List<UpdateDishModel> updateDishModelList;
    private ArrayList<Categories> categoryList;
    private CustomerHomeAdapter adapter;
    private CategoryAdapter adapterCate;
    String State, City, Sub;
    DatabaseReference dataaa, databaseReference,databaseReference1;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmenthome, null);
        getActivity().setTitle("Giờ ăn đến rồi");
        setHasOptionsMenu(true);
        SliderView sliderView = v.findViewById(R.id.imageSlider);
        rcvhome = v.findViewById(R.id.rcvhome);
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        recyclerView.startAnimation(animation);
        rcvhome.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);
        updateDishModelList = new ArrayList<>();
        categoryList=new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
//extra


        //rcvhome.setLayoutManager(linearLayoutManager);
        rcvhome.setHasFixedSize(true);

        //extra

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
                dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Customer cust = dataSnapshot.getValue(Customer.class);
                        State = cust.getState();
                        City = cust.getCity();
                        Sub = cust.getSuburban();
                        customerCatemenu();
                        customermenu();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        //extra
        QuangCao = new SlideViewPayer(getContext());
        sliderView.setSliderAdapter(QuangCao);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        renewItems(v);
        removeLastItem(v);
        addNewItem(v);

        return v;
    }
    public void renewItems(View view) {
        List<Item> sliderItemList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Item sliderItem = new Item();
            if (i % 2 == 0) {
                sliderItem.setImageurl("https://images.squarespace-cdn.com/content/v1/53883795e4b016c956b8d243/1597821998048-538UNQI253SYL3KE9NGD/chup-anh-mon-an-breakfast-10.jpg");
            } else {
                sliderItem.setImageurl("https://toplist.vn/images/800px/-790915.jpg");
            }
            sliderItemList.add(sliderItem);
        }
        QuangCao.ViewPagerAdapter(sliderItemList);
    }
    public void removeLastItem(View view) {
        if (QuangCao.getCount() - 1 >= 0)
            QuangCao.deleteitem(QuangCao.getCount() - 1);
    }
    public void addNewItem(View view) {
        Item sliderItem = new Item();
//        sliderItem.setDescription("Re");
        sliderItem.setImageurl("https://eventusproduction.com/wp-content/uploads/2017/02/fresh-box.jpg");
        QuangCao.addItem(sliderItem);
    }

    @Override
    public void onRefresh() {

        customermenu();
    }
    private void customerCatemenu() {


        databaseReference1 = FirebaseDatabase.getInstance().getReference("Categories").child(State).child(City).child(Sub);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Categories categories = snapshot1.getValue(Categories.class);
                        categoryList.add(categories);
                    }
                }
                adapterCate = new CategoryAdapter(categoryList,getContext());
                rcvhome.setAdapter(adapterCate);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
    private void customermenu() {

        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(State).child(City).child(Sub);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateDishModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UpdateDishModel updateDishModel = snapshot1.getValue(UpdateDishModel.class);
                        updateDishModelList.add(updateDishModel);
                    }
                }
                adapter = new CustomerHomeAdapter(getContext(), updateDishModelList);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });

    }

    private void search(final String searchtext) {

        ArrayList<UpdateDishModel> mylist = new ArrayList<>();
        for (UpdateDishModel object : updateDishModelList) {
            if (object.getDishes().toLowerCase().contains(searchtext.toLowerCase())) {
                mylist.add(object);
            }
        }
        adapter = new CustomerHomeAdapter(getContext(), mylist);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.Searchdish);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Tìm món ăn ");


    }




}

