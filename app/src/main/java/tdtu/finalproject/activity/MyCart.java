package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tdtu.finalproject.R;
import tdtu.finalproject.adapter.CartAdapter;
import tdtu.finalproject.model.Cart;
import tdtu.finalproject.util.CheckConnection;
import tdtu.finalproject.util.Server;

public class MyCart extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    public static TextView tv_first_price, tv_total;
    private EditText edt_coupon;
    private Button btn_confirm, btn_paying;

    public static ArrayList<Cart> carts = new ArrayList<>();
    private ListView lv_cart;
    private CartAdapter cartAdapter = new CartAdapter(this);
    private int idProduct = 0;
    private String nameProduct;
    private String imageProduct;
    private int priceProduct = 0;
    private int quantityProduct = 5;
    private String sizeProduct = "s";
    private int retail = 0;
    public int first_price = 0;
    public static int idOrderCurrent;
    public static ArrayList<Integer> coupon = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        getWidget();
        ActionBar();
        getData();
        setPrice();
        getIdOrder();
        getCoupon("none");
        setCartView();
        lv_cart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyCart.this, Details.class);
                intent.putExtra("ID_PRODUCT", carts.get(i).getId());
                startActivity(intent);
            }
        });

        btn_confirm.setOnClickListener(this);
        btn_paying.setOnClickListener(this);
    }

    /**set adapter cho view cart*/
    private void setCartView() {
        cartAdapter.setData(carts);
        lv_cart.setAdapter(cartAdapter);
    }

    /**check mã giảm giá*/
    private void getCoupon(final String code) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.couponDirection,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    if(jsonObject.getString("code").equals(code)){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MyCart.this);
                                        builder.setTitle("Áp dụng mã giảm giá thành công!");
                                        builder.setMessage("Giá trị: " + Integer.toString(jsonObject.getInt("value")));
                                        builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                        AlertDialog al = builder.create();
                                        al.show();
                                        applyCoupon(jsonObject.getInt("value"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    /**set tổng giá sau khi áp coupon*/
    private void applyCoupon(int value) {
        int apply = Integer.parseInt(tv_first_price.getText().toString()) - value;
        if(apply < 0){
            tv_total.setText(Integer.toString(0));
        }else {
            tv_total.setText(Integer.toString(apply));
        }
    }

    /**lấy idorder trước đó trên hệ thống*/
    public void getIdOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.getOrderDirection,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    idOrderCurrent = jsonObject.getInt("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(View view) {
        //nhập mã coupon
        if(view.getId() == btn_confirm.getId()){
            getCoupon(edt_coupon.getText().toString());
        }

        //xác nhận mua hàng
        if(view.getId() == btn_paying.getId()){
            if(LoginActivity.customer.getId() != -1) {
                AlertDialog.Builder b = new AlertDialog.Builder(MyCart.this);
                b.setTitle("Xác nhận");
                b.setMessage("Bạn có chắc chắn muốn đặt hàng ?");
                b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addOrder();
                        addDetailOrder();
                        Toast.makeText(MyCart.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyCart.this, MainActivity.class);
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
            }else{
                Intent intent = new Intent(MyCart.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    /**thêm đơn hàng vào server*/
    private void addOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(MyCart.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.addOrderDirection, new Response.Listener<String>() {
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
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("Order_total", tv_total.getText().toString());
                    jsonObject.put("Customer_id", Integer.toString(LoginActivity.customer.getId()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("json", jsonArray.toString());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**thêm chi tiết đơn hàng vào server*/
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
                        jsonObject.put("Order_id", idOrderCurrent + 1);
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

    /**cập nhật giá sản phẩm theo số lượng*/
    private void setPrice() {
        for(int i = 0; i < carts.size(); i++){
            first_price += carts.get(i).getPrice();
        }
        tv_first_price.setText(Integer.toString(first_price));
        tv_total.setText(Integer.toString(first_price));
    }

    /**lấy dữ liệu sản phẩm*/
    private void getData() {
        int index = -1;
        idProduct = getIntent().getIntExtra("ID_ITEM", -1);
        if(idProduct != -1){
            boolean isExist = false;
            for (int i = 0; i < carts.size(); i++){
                if(idProduct == carts.get(i).getId()){
                    isExist = true;
                    index = i;
                }
            }
            if(!isExist){
                carts.add(new Cart(idProduct, imageProduct, nameProduct, priceProduct, quantityProduct, sizeProduct, retail));
            }else if(carts.get(index).getSize() != sizeProduct ||
                    carts.get(index).getQuantity() != quantityProduct){
                carts.set(index, new Cart(idProduct, imageProduct, nameProduct, priceProduct, quantityProduct, sizeProduct, retail));
            }
        }
    }

    /**cài đặt thanh nav bar*/
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
        lv_cart = findViewById(R.id.lv_cart);
        toolbar = findViewById(R.id.toolbar_cart);
        tv_first_price = findViewById(R.id.tv_first_price);
        tv_total = findViewById(R.id.tv_total);
        edt_coupon = (EditText) findViewById(R.id.edt_coupon);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_paying = (Button) findViewById(R.id.btn_pay);
    }

}