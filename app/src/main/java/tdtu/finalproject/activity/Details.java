package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import tdtu.finalproject.model.Cart;
import tdtu.finalproject.util.Server;

public class Details extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Spinner spinner;
    private ImageView image_product;
    private TextView tv_name, tv_price, tv_quantity;
    private Button btn_add_to_cart_2, move_cart, btn_minus, btn_add;


    private int idProduct = 0;
    private String nameProduct;
    private String imageProduct;
    private int priceProduct = 0;
    private int quantityProduct = 1;
    private String sizeProduct = "s";
    public int retail = 0;
    private ArrayList<String> sizes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getWidget();
        ActionBar();
        getIdProduct();
        getDataProduct();
        btn_add_to_cart_2.setOnClickListener(this);
        move_cart.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
    }

    /**sự kiện click các view*/
    @Override
    public void onClick(View view) {
        int index = -1;
        //nút đi tới giỏ hàng
        if(view.getId() == move_cart.getId()){
            Intent intent = new Intent(Details.this, MyCart.class);
            startActivity(intent);
        }

        //nút thêm vào giỏ hàng
        //nếu sản phẩm đó có rồi thì ko thêm
        //nếu số lượng hoặc size được làm mới thì cập nhật chúng
        if(view.getId() == btn_add_to_cart_2.getId()){
            boolean isExist = false;
            for(int i = 0; i < MyCart.carts.size(); i++){
                if(idProduct == MyCart.carts.get(i).getId()){
                    isExist = true;
                    index = i;
                }
            }
            if(!isExist){
                MyCart.carts.add(new Cart(idProduct,
                        imageProduct,
                        nameProduct,
                        priceProduct,
                        quantityProduct,
                        sizeProduct,
                        retail));
                Toast.makeText(getApplicationContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
            }else if(MyCart.carts.get(index).getQuantity() != quantityProduct){
                MyCart.carts.set(index, new Cart(idProduct,
                        imageProduct,
                        nameProduct,
                        priceProduct,
                        quantityProduct,
                        sizeProduct,
                        retail));
                Toast.makeText(getApplicationContext(), "Cập nhật số lượng thành công", Toast.LENGTH_SHORT).show();
            }else if(MyCart.carts.get(index).getSize() != sizeProduct){
                MyCart.carts.set(index, new Cart(idProduct,
                        imageProduct,
                        nameProduct,
                        priceProduct,
                        quantityProduct,
                        sizeProduct,
                        retail));
                Toast.makeText(getApplicationContext(), "Cập nhật size thành công", Toast.LENGTH_SHORT).show();
            }
        }

        //nút thêm số lượng
        if(view.getId() == btn_add.getId()){
            quantityProduct ++;
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            priceProduct = retail * quantityProduct;
            tv_price.setText(decimalFormat.format(priceProduct)  + " Đ");
            tv_quantity.setText(Integer.toString(quantityProduct));
        }
        //nút giảm số lưuongj
        if(view.getId() == btn_minus.getId()){
            quantityProduct --;
            if(quantityProduct < 0){
                quantityProduct = 0;
            }
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            priceProduct = retail * quantityProduct;
            tv_price.setText(decimalFormat.format(priceProduct)  + " Đ");
            tv_quantity.setText(Integer.toString(quantityProduct));
        }
    }

    /**lấy id sản phẩm*/
    private void getIdProduct() {
        idProduct = getIntent().getIntExtra("ID_PRODUCT", -1);
    }

    /**lấy data sản phẩm dựa theo id*/
    private void getDataProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.detailDirection;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i =0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Picasso.with(getApplicationContext()).
                                    load(Server.imgaeDirection +jsonObject.getString("image")).
                                    placeholder(R.drawable.cancel).
                                    error(R.drawable.warning).
                                    into(image_product);
                            imageProduct = Server.imgaeDirection +jsonObject.getString("image");
                            nameProduct = jsonObject.getString("name");
                            tv_name.setText(jsonObject.getString("name"));
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            priceProduct = jsonObject.getInt("price");
                            retail = jsonObject.getInt("price");
                            tv_price.setText(decimalFormat.format(jsonObject.getInt("price"))  + " Đ");
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
                param.put("Product_id", Integer.toString(idProduct));
                return param;
            }
        };
        requestQueue.add(stringRequest);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        image_product = (ImageView) findViewById(R.id.image_product);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_quantity = (TextView) findViewById(R.id.tv_quantity);
        btn_add_to_cart_2 = (Button) findViewById(R.id.btn_add_to_cart_2);
        move_cart = (Button) findViewById(R.id.move_cart);
        btn_minus = findViewById(R.id.btn_minus);
        btn_add = findViewById(R.id.btn_add);
    }
}