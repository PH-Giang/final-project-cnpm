package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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

import tdtu.finalproject.R;
import tdtu.finalproject.adapter.ManageProductAdapter;
import tdtu.finalproject.model.Product;
import tdtu.finalproject.util.CheckConnection;
import tdtu.finalproject.util.Server;

public class ManageProduct extends AppCompatActivity {
    ListView lv;
    Toolbar toolbar;

    ArrayList<Product> products = new ArrayList<>();
    ManageProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);
        getWidget();
        actionBar();
        getDataProduct();
        setListView();
    }
    /**lấy data danh sách sản phẩm*/
    private void getDataProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.directionProduct,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
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
                                            imageProduct
                                    ));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
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
    /**set view các item*/
    private void setListView() {
        adapter.setData(products);
        lv.setAdapter(adapter);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageProduct.this, AdminPage.class);
                startActivity(intent);
            }
        });
    }

    private void getWidget() {
        lv = findViewById(R.id.lv_manage_product);
        toolbar = findViewById(R.id.toolbar_manage_product);
        adapter = new ManageProductAdapter(ManageProduct.this);
    }
}