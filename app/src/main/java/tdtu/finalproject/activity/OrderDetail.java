package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tdtu.finalproject.R;
import tdtu.finalproject.adapter.OrderDetailAdapter;
import tdtu.finalproject.model.DetailOrder;
import tdtu.finalproject.model.MyOrderModel;
import tdtu.finalproject.util.Server;

public class OrderDetail extends AppCompatActivity {
    private ListView lv;
    private Toolbar toolbar;
    ArrayList<DetailOrder> detailOrders = new ArrayList<>();
    OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getWidget();
        actionBar();
        Toast.makeText(getApplicationContext(), getIntent().getStringExtra("ORDERID"), Toast.LENGTH_LONG).show();
        getDataOrder();
        setListView();
    }

    /**Lấy dữ liệu của hóa đơn*/
    private void getDataOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.getDetailOrderDirection;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i =0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            detailOrders.add(new DetailOrder(
                                    jsonObject.getInt("idDetail"),
                                    jsonObject.getInt("idOrder"),
                                    jsonObject.getInt("idProduct"),
                                    jsonObject.getInt("quantity"),
                                    jsonObject.getInt("cost")
                            ));
                            orderDetailAdapter.notifyDataSetChanged();
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
                Intent intent = getIntent();
                String str = intent.getStringExtra("ORDERID");
                param.put("Order_id", str);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void setListView() {
        orderDetailAdapter.setData(detailOrders);
        lv.setAdapter(orderDetailAdapter);
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
    private void getWidget() {
        lv = findViewById(R.id.lv_detail_order);
        toolbar = findViewById(R.id.toolbar_detail_order);
    }
}