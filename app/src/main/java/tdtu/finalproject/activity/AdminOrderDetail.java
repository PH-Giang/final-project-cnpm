package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import tdtu.finalproject.util.Server;

public class AdminOrderDetail extends AppCompatActivity {
    private Button action;
    private Toolbar toolbar;
    private ListView lv;

    private ArrayList<DetailOrder> detailOrders = new ArrayList<>();
    private OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);
        getWidget();
        actionBar();
        getDataOrder();
        setListView();

        //cài đặt title cho button theo status của đơn hàng
        if(getIntent().getIntExtra("STATUS" ,-1) == 0){
            action.setBackgroundResource(R.drawable.ripple_action_unconfirmed);
            action.setText("Chưa xác nhận");
        }else{
            action.setBackgroundResource(R.drawable.ripple_action_confirm);
            action.setText("Đã xác nhận");
        }

        //xác nhận - hủy trạng thái đơn hàng
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminOrderDetail.this);
                builder.setTitle("Message!");
                if(getIntent().getIntExtra("STATUS" ,-1) == 1){
                    builder.setMessage("Hủy trạng thái đơn hàng");
                }else{
                    builder.setMessage("Xác nhận đơn hàng");
                }
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        int orderID = Integer.parseInt(getIntent().getStringExtra("ORDERID"));
                        if(getIntent().getIntExtra("STATUS" ,-1) == 0){
                            updateStatus(orderID, 1);
                        }else{
                            updateStatus(orderID, 0);
                        }
                        refresh();
                    }
                });
                AlertDialog al = builder.create();
                al.show();
            }
        });
    }

    /**làm mới lại trang để cập nhật lại danh sách*/
    public void refresh(){
        Intent intent = getIntent();
        Intent intent2 = new Intent(AdminOrderDetail.this, AdminListOrder.class);
        intent2.putExtra("STATUS", intent.getIntExtra("STATUS", -1));
        startActivity(intent2);
    }

    /**chuyển trạng thái cho các hóa đơn*/
    public void updateStatus(final int id, final int status) {
        RequestQueue requestQueue = Volley.newRequestQueue(AdminOrderDetail.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.updateStatus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("status", Integer.toString(status));
                hashMap.put("id", Integer.toString(id));
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminOrderDetail.this, AdminListOrder.class);
        intent.putExtra("STATUS", getIntent().getIntExtra("STATUS", -1));
        startActivity(intent);
        super.onBackPressed();
    }
    private void actionBar() {
        Intent intent = getIntent();
        if(intent.getIntExtra("STATUS",-1) == 1){
            toolbar.setTitle("Đơn hàng đã xác nhận");
        }else{
            toolbar.setTitle("Đơn hàng chưa xác nhận");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminOrderDetail.this, AdminListOrder.class);
                intent.putExtra("STATUS", getIntent().getIntExtra("STATUS", -1));
                startActivity(intent);
            }
        });
    }

    private void getWidget() {
        action = findViewById(R.id.action_order);
        toolbar = findViewById(R.id.toolbar_admin_order_detail);
        lv = findViewById(R.id.lv_admin_detail_order);
    }
}