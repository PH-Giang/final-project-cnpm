package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tdtu.finalproject.R;
import tdtu.finalproject.util.Server;

public class AddCoupon extends AppCompatActivity {
    private Button add;
    private TextInputEditText code, value;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);
        getWidget();
        actionBar();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCoupon.this);
                builder.setTitle("Message!");
                builder.setMessage("Thêm mã này ?");
                builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //check mã này tồn tại chưa
                        RequestQueue requestQueue = Volley.newRequestQueue(AddCoupon.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.couponDirection, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response != null){
                                    try{
                                        JSONArray jsonArray = new JSONArray(response);
                                        boolean isExist = false;
                                        for (int i =0; i < jsonArray.length(); i++){
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            if(jsonObject.getString("code").equals(code.getText().toString())){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AddCoupon.this);
                                                builder.setTitle("Lỗi!");
                                                builder.setMessage("Mã này đã tồn tại");
                                                builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                });
                                                AlertDialog al = builder.create();
                                                al.show();
                                                isExist = true;
                                                break;
                                            }
                                        }
                                        if(isExist == false){
                                            insertCoupon(code.getText().toString(), value.getText().toString());
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
                                hashMap.put("code", code.getText().toString());
                                hashMap.put("value", value.getText().toString());
                                return hashMap;
                            }
                        };
                        requestQueue.add(stringRequest);
                    }
                });
                AlertDialog al = builder.create();
                al.show();
            }
        });

    }

    /**Thêm coupon vào database*/
    private void insertCoupon(final String code, final String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCoupon.this);
        builder.setTitle("Messsage!");
        builder.setMessage("Thêm thành công");
        builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(AddCoupon.this, ManageCoupon.class);
                startActivity(intent);
                dialogInterface.cancel();
            }
        });
        AlertDialog al = builder.create();
        al.show();
        RequestQueue requestQueue = Volley.newRequestQueue(AddCoupon.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.addCoupon, new Response.Listener<String>() {
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
                hashMap.put("code", code);
                hashMap.put("value", value);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AddCoupon.this, ManageCoupon.class);
        startActivity(intent);
        super.onBackPressed();

    }
    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCoupon.this, ManageCoupon.class);
                startActivity(intent);
            }
        });
    }

    private void getWidget() {
        toolbar = findViewById(R.id.toolbar_add_coupon);
        code = findViewById(R.id.add_code);
        value = findViewById(R.id.add_value);
        add = findViewById(R.id.add_coupon);
    }
}