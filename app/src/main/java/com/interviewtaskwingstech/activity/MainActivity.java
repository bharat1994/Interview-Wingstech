package com.interviewtaskwingstech.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.interviewtaskwingstech.MyApp;
import com.interviewtaskwingstech.R;
import com.interviewtaskwingstech.common.EndlessRecyclerViewScrollListener;
import com.interviewtaskwingstech.model.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rvCustomerList) RecyclerView rvCustomerList;
    @BindView(R.id.txtAppName) TextView txtAppName;
    @BindView(R.id.txtFollowUser) TextView txtFollowUser;
    @BindView(R.id.etSearch) EditText etSearch;

    private MyApp myApp = MyApp.getInstance();
    List<Customer> customerList = new ArrayList<>();
    CustomerAdapter customerAdapter;
    public OkHttpClient client = new OkHttpClient();
    private EndlessRecyclerViewScrollListener scrollListener;
    LinearLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        fetchData(10);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filter_text = etSearch.getText().toString().trim().toLowerCase(Locale.getDefault());
                customerAdapter.searchFilter(filter_text);
                customerAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        manager = new LinearLayoutManager(MainActivity.this);
        rvCustomerList.setHasFixedSize(true);
        rvCustomerList.setLayoutManager(manager);

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.e("PAGE",""+page);
                fetchData(10+page);
                //loadNextDataFromApi(page);
            }
        };
        rvCustomerList.addOnScrollListener(scrollListener);
    }

    public void fetchData(int i)
    {
        Log.e("PassUrl","https://api.github.com/users?per_page="+i+"&since=11");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.github.com/users?per_page="+i+"&since=11")
                .method("GET",null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Responce",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.e("Responce",responseStr);

                    final GsonBuilder gsonBuilder = new GsonBuilder();
                    final Gson gson = gsonBuilder.create();

                    Customer[] customer = gson.fromJson(responseStr,Customer[].class);

                    myApp.getAppDatabase().customerDao().insertAll(customer);

                    customerList = myApp.getAppDatabase().customerDao().getAll();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customerAdapter = new CustomerAdapter(MainActivity.this,customerList);
                            rvCustomerList.setAdapter(customerAdapter);
                        }
                    });

                }else {

                }
            }
        });

    }

    private class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder>{

        Context context;
        List<Customer> orignal = new ArrayList<>();
        List<Customer> duplicate = new ArrayList<>();
        String mSearchText;

        public CustomerAdapter(Context context, List<Customer> orignal) {
            this.context = context;
            this.orignal = orignal;
            this.duplicate.addAll(orignal);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cust_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            Customer customer = orignal.get(position);
            holder.txtCustName.setText(customer.login);

            Log.e("IMAGE","id"+customer.id);
            Log.e("IMAGE","nordid"+customer.node_id);
            Log.e("IMAGE","image"+customer.avatar_url);
            Glide.with(context)
                    .load(customer.avatar_url)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .error(R.drawable.ic_baseline_person_24)
                    .into(holder.civProfileImage);

            holder.civProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context,FullScreenActivity.class).putExtra("image_url",""+customer.avatar_url));
                }
            });

            holder.view.setBackgroundColor(customer.isSelected() ? Color.CYAN : Color.WHITE);
            holder.llCustView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customer.setSelected(!customer.isSelected());
                    holder.view.setBackgroundColor(customer.isSelected() ? Color.CYAN : Color.WHITE);
                    Count();
                }
            });

        }

        @Override
        public int getItemCount() {
            return orignal.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
        private View view;
        LinearLayout llCustView;
        CircleImageView civProfileImage;
        TextView txtCustName,txtFollow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            llCustView = itemView.findViewById(R.id.llCustView);
            civProfileImage = itemView.findViewById(R.id.civProfileImage);
            txtCustName = itemView.findViewById(R.id.txtCustName);
            txtFollow = itemView.findViewById(R.id.txtFollow);
        }

    }

        public void searchFilter(String filter_text) {

            mSearchText = filter_text;
            filter_text = filter_text.toLowerCase(Locale.getDefault());
            orignal.clear();

            if (filter_text.length() == 0) {
                orignal.addAll(duplicate);

            } else {
                for (Customer restaurantss : duplicate) {
                    if (restaurantss.login.toLowerCase(Locale.getDefault()).contains(filter_text)) {
                        orignal.add(restaurantss);
                    }
                }
                if (orignal.size() == 0) {
                    Toast.makeText(context, "No User Found !", Toast.LENGTH_SHORT).show();
                }
            }
            notifyDataSetChanged();
        }

        public void Count(){
            int i = 0;
            for (Customer customer : orignal){
                if (customer.isSelected()){
                    i++;
                }
            }

            if (i>0){
                txtAppName.setText(""+i);
                txtFollowUser.setVisibility(View.VISIBLE);
            }else {
                txtAppName.setText(R.string.app_name);
                txtFollowUser.setVisibility(View.GONE);
            }
        }
    }

}