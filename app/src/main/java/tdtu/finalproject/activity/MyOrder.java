package tdtu.finalproject.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tdtu.finalproject.R;
import tdtu.finalproject.adapter.MyOrderAdapter;
import tdtu.finalproject.model.MyOrderModel;
import tdtu.finalproject.util.Server;

public class MyOrder extends AppCompatActivity {
    ListView lv;
    Toolbar toolbar;
    ArrayList<MyOrderModel> myOrders = new ArrayList<MyOrderModel>();
    MyOrderAdapter myOrderAdapter = new MyOrderAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        getWidget();
        getDataOrder();
        setListView();
        actionBar();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyOrder.this, OrderDetail.class);
                //truyền id của hóa đơn được chọn
                intent.putExtra("ORDERID", Integer.toString(myOrders.get(i).getId()));
                startActivity(intent);
            }
        });
    }

    /**lấy dữ liệu của hóa đơn*/
    private void getDataOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.getMyOrderDirection;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i =0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            boolean status =false;
                            if(jsonObject.getInt("status") == 1){
                                status = true;
                            }
                            myOrders.add(new MyOrderModel(
                                    jsonObject.getInt("id"),
                                    jsonObject.getInt("total"),
                                    jsonObject.getString("dateOrder"),
                                    status
                            ));
                            myOrderAdapter.notifyDataSetChanged();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("Customer_id", Integer.toString(LoginActivity.customer.getId()));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void setListView() {
        myOrderAdapter.setData(myOrders);
        lv.setAdapter(myOrderAdapter);
    }

    private void getWidget() {
        lv = findViewById(R.id.lv_myorder);
        toolbar = findViewById(R.id.toolbar_myorder);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}