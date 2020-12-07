package tdtu.finalproject.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import tdtu.finalproject.R;
import tdtu.finalproject.adapter.ManageCouponAdapter;
import tdtu.finalproject.model.Coupon;
import tdtu.finalproject.util.Server;

public class ManageCoupon extends AppCompatActivity {
    private ListView listView;
    private Toolbar toolbar;
    private ImageView add;
    private ArrayList<Coupon> coupons = new ArrayList<>();
    private ManageCouponAdapter couponAdapter =  new ManageCouponAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_coupon);
        getWidget();
        getData();
        actionBar();
        couponAdapter.setData(coupons);
        listView.setAdapter(couponAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageCoupon.this, AddCoupon.class);
                startActivity(intent);
            }
        });
    }
    /**lấy data các coupon*/
    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(ManageCoupon.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.couponDirection, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i =0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            coupons.add(new Coupon(jsonObject.getString("code"), jsonObject.getInt("value")));
                            couponAdapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ManageCoupon.this, AdminPage.class);
        startActivity(intent);
        super.onBackPressed();

    }
    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageCoupon.this, AdminPage.class);
                startActivity(intent);
            }
        });
    }

    private void getWidget() {
        toolbar = findViewById(R.id.toolbar_manage_coupon);
        listView = findViewById(R.id.lv_manage_coupon);
        add = findViewById(R.id.manage_add_coupon);
    }
}