package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tdtu.finalproject.R;

import tdtu.finalproject.adapter.ProductAdapter;
import tdtu.finalproject.model.Product;
import tdtu.finalproject.util.CheckConnection;
import tdtu.finalproject.util.Server;

public class ItemType extends AppCompatActivity {
    private RecyclerView recyclerView_tshirt;
    private Toolbar toolbar;

    private ArrayList<Product> products = new ArrayList<Product>();
    private ProductAdapter productAdapter;
    private int idCate = 0;
    private int idType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_type);
        getWidget();
        getIdCate();
        ActionBar();
        Toast.makeText(getApplicationContext(), Integer.toString(idCate), Toast.LENGTH_LONG).show();
        if (CheckConnection.haveNetworkConnection(this)) {
            // cài đặt và đổ dữ liệu cho màn hình
            productAdapter = new ProductAdapter(getApplicationContext());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView_tshirt.setLayoutManager(gridLayoutManager);
            recyclerView_tshirt.setFocusable(false);
            recyclerView_tshirt.setNestedScrollingEnabled(false);
            getDataProduct();
            productAdapter.setData(products);
            recyclerView_tshirt.setAdapter(productAdapter);
        }else{
            CheckConnection.ShowToast(this,"Vui lòng kiểm tra lại mạng");
        }
    }

    /**
     * Lấy data từ server
     * */
    private void getIdCate() {
        idCate = getIntent().getIntExtra("ID_CATE", -1);
        idType = getIntent().getIntExtra("ID_TYPE", -1);
    }

    /**
     * Lấy id danh mục
     * */
    private void getDataProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.typeDirection;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i =0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int idProduct = jsonObject.getInt("id");
                            int idCateOfProduct = jsonObject.getInt("id_category");
                            String nameProduct = jsonObject.getString("name");
                            String desProduct = jsonObject.getString("des");
                            int priceProduct = jsonObject.getInt("price");
                            String imageProduct = Server.imgaeDirection + jsonObject.getString("image");
                            products.add(new Product(idProduct,
                                    idCateOfProduct,
                                    nameProduct,
                                    desProduct,
                                    priceProduct,
                                    imageProduct));

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                productAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("Category_id", Integer.toString(idCate));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * Cài đặt thanh tab
     * */
    private void ActionBar() {
        switch (idType){
            case 1: toolbar.setTitle("Diệt virus"); break;
            case 2: toolbar.setTitle("Thiết kế"); break;
            case 3: toolbar.setTitle("Hệ thống"); break;
            case 4: toolbar.setTitle("Window"); break;
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Ánh xạ các view
     * */
    private void getWidget() {
        recyclerView_tshirt = (RecyclerView) findViewById(R.id.recyclerView_tshirt);
        toolbar = (Toolbar) findViewById(R.id.toolbar_title);

    }
}