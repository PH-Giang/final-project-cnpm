package tdtu.finalproject.activity;



import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;


import tdtu.finalproject.R;

import tdtu.finalproject.util.Server;

public class Paying extends AppCompatActivity{
    private EditText cus_name, cus_email, cus_phone, cus_address;
    private Button btn_submit;
    private Toolbar toolbar;
    public int idOrder = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying);
        getWidget();
        ActionBar();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(Paying.this);
                b.setTitle("Xác nhận");
                b.setMessage("Bạn có chắc chắn muốn đặt hàng ?");
                b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addOrder();

                        addDetailOrder();
                        Toast.makeText(Paying.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Paying.this, MainActivity.class);
                        intent.putExtra("CLEAR_CART", 1);
                        startActivity(intent);
                    }
                });
                b.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog al = b.create();
                al.show();
            }
        });

    }

    private void addOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.addOrderDirection, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try {
                    Intent intent = getIntent();
                    String total = intent.getStringExtra("TOTAL");
                    jsonObject.put("Order_total", Integer.parseInt(total));
                    jsonObject.put("Customer_name", cus_name.getText().toString());
                    jsonObject.put("Customer_email", cus_email.getText().toString());
                    jsonObject.put("Customer_phone", cus_phone.getText().toString());
                    jsonObject.put("Customer_address", cus_address.getText().toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("json", jsonArray.toString());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void addDetailOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.addOrderDetailDirection, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray jsonArray = new JSONArray();
                for(int i = 0; i < MyCart.carts.size(); i++){
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("Order_id", idOrder);
                        jsonObject.put("Product_id", MyCart.carts.get(i).getId());
                        jsonObject.put("Product_quantity", MyCart.carts.get(i).getQuantity());
                        jsonObject.put("Product_cost", MyCart.carts.get(i).getPrice());
                        jsonObject.put("Product_size", MyCart.carts.get(i).getSize());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("json", jsonArray.toString());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionBar() {
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
        cus_name = (EditText) findViewById(R.id.cus_name);
        cus_email = (EditText) findViewById(R.id.cus_email);
        cus_phone = (EditText) findViewById(R.id.cus_phone);
        cus_address = (EditText) findViewById(R.id.cus_address);
        toolbar = (Toolbar) findViewById(R.id.toolbar_paying);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }
}