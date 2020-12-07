package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import tdtu.finalproject.adapter.AdminOrderAdapter;
import tdtu.finalproject.adapter.MyOrderAdapter;
import tdtu.finalproject.model.AdminOrder;
import tdtu.finalproject.model.MyOrderModel;
import tdtu.finalproject.util.Server;

public class AdminListOrder extends AppCompatActivity {
    private ListView lv;
    private Toolbar toolbar;


    private ArrayList<AdminOrder> adminListOrders = new ArrayList<AdminOrder>();
    private AdminOrderAdapter adminOrderAdapter = new AdminOrderAdapter(this);

    public AdminListOrder() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_order);
        getWidget();
        actionBar();
        getDataOrder();
        setListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AdminListOrder.this, AdminOrderDetail.class);
                intent.putExtra("STATUS", getIntent().getIntExtra("STATUS", -1));
                intent.putExtra("ORDERID", Integer.toString(adminListOrders.get(i).getId()));
                startActivity(intent);
            }
        });
    }


    /**lấy data các hóa đơn*/
    private void getDataOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getAdminOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            adminListOrders.add(new AdminOrder(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("phone"),
                                    jsonObject.getString("address"),
                                    jsonObject.getInt("total"),
                                    jsonObject.getString("date1"),
                                    jsonObject.getInt("status")
                            ));
                            adminOrderAdapter.notifyDataSetChanged();
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
                HashMap<String, String> hashMap = new HashMap<>();
                Intent intent = getIntent();
                hashMap.put("date", Integer.toString(AdminPage.datePicked));
                hashMap.put("month", Integer.toString(AdminPage.monthPicked));
                hashMap.put("year", Integer.toString(AdminPage.yearPicked));
                hashMap.put("status", Integer.toString(intent.getIntExtra("STATUS", -1)));
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**set giao diện các tính năng trong admin*/
    private void setListView() {
        adminOrderAdapter.setData(adminListOrders);
        lv.setAdapter(adminOrderAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminListOrder.this, AdminPage.class);
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
                Intent intent = new Intent(AdminListOrder.this, AdminPage.class);
                startActivity(intent);
            }
        });
    }
    private void getWidget() {
        lv = findViewById(R.id.lv_order_admin);
        toolbar = findViewById(R.id.tollbar_order_admin);
    }
}