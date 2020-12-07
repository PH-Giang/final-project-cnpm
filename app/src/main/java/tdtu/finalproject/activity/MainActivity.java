package tdtu.finalproject.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//key
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

import tdtu.finalproject.R;
import tdtu.finalproject.adapter.CategoryAdapter;
import tdtu.finalproject.adapter.ProductAdapter;
import tdtu.finalproject.model.Category;
import tdtu.finalproject.model.Product;
import tdtu.finalproject.util.CheckConnection;
import tdtu.finalproject.util.Server;

import static javax.crypto.Cipher.SECRET_KEY;

public class MainActivity extends AppCompatActivity {
    //view
    private TextView tv_login;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar_home;
    private RecyclerView recyclerView_newproduct;
    private NavigationView nav_menu;
    private ListView lv_category;
    private ImageView move_to_cart;

    //functional component
    private ArrayList<Category> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private ArrayList<Product> products = new ArrayList<>();
    private ProductAdapter productAdapter;
    private int itemSelected = MyCart.carts.size();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWidget();

        //checking connection wifi or data mobile
        if(CheckConnection.haveNetworkConnection(getApplicationContext())) {
            actionBar();
            setViewGroup();
            categoryOnClickEvent();
            setEvent();
            checkPaid();
            checkUser();

        }else{
            CheckConnection.ShowToast(this, "Checking internet, please!");
            finish();
        }
    }

    /**kiểm tra user đã đăng nhập*/
    private void checkUser() {
        if(LoginActivity.customer.getUsername() != ""){
            tv_login.setText(LoginActivity.customer.getName() + ", cài đặt");
        }
    }

    /**kiểm tra thanh toán chưa*/
    private void checkPaid() {
        Intent intent = getIntent();
        int isPaid = intent.getIntExtra("CLEAR_CART", -1);
        if(isPaid == 1){
            MyCart.carts.removeAll(MyCart.carts);
        }
    }

    private void setEvent() {
        move_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyCart.class);
                startActivity(intent);
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoginActivity.customer.getUsername() == ""){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, InfomationUser.class);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * sự kiện chuyển màn hình theo danh mục sản phẩm*/
    private void categoryOnClickEvent() {
        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại mạng");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, ItemType.class);
                            intent.putExtra("ID_CATE", categories.get(i).getId());
                            intent.putExtra("ID_TYPE", i);
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại mạng");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                            Intent intent = new Intent(MainActivity.this, AdminLogin.class);
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToast(getApplicationContext(), "Vui lòng kiểm tra lại mạng");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });
    }

    /**
     * set sidebar listview
     * and set products recyler view
     */
    private void setViewGroup() {
        //product
        productAdapter = new ProductAdapter(getApplicationContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView_newproduct.setLayoutManager(gridLayoutManager);
        recyclerView_newproduct.setFocusable(false);
        recyclerView_newproduct.setNestedScrollingEnabled(false);
        getDataProduct();
        productAdapter.setData(products);
        recyclerView_newproduct.setAdapter(productAdapter);
        //sidebar
        getDataSidebar();
        categoryAdapter = new CategoryAdapter(getApplicationContext());
        categoryAdapter.setData(categories);
        lv_category.setAdapter(categoryAdapter);
    }

    /**
     * set data for product from server*/
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
                            productAdapter.notifyDataSetChanged();
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

    /**
     * get data for sidebar from server
     * */
    private void getDataSidebar() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.directionCategory,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            //category home page
                            String imageHome = Server.imgaeDirection + "home.png";
                            categories.add(new Category(0, "Trang chủ", imageHome));
                            //category clothes
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    int idCategory = jsonObject.getInt("id");
                                    String nameCategory = jsonObject.getString("name");
                                    String imageCategory = Server.imgaeDirection + jsonObject.getString("image");
                                    categories.add(new Category(idCategory, nameCategory, imageCategory));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            // category infomation
                            String imageInfo = Server.imgaeDirection + "info.png";
                            categories.add(new Category(categories.size(), "Admin", imageInfo));
                            categoryAdapter.notifyDataSetChanged();
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

    /**
     * * Set sidebar navigation
     * actionBar(): crate bar menu
     * onBackPressed: set event onlick to open and close sidebar
     * */
    private void actionBar() {
        setSupportActionBar(toolbar_home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar_home,
                R.string.navigation_drawerr_open, R.string.navigation_drawerr_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            super.onBackPressed();
        }
    }

    /**
     * get view components
     * */
    private void getWidget() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout) ;
        toolbar_home = (Toolbar) findViewById(R.id.toolbar_home);
        recyclerView_newproduct = (RecyclerView) findViewById(R.id.recyclerView_newproduct);
        nav_menu = (NavigationView) findViewById(R.id.nav_menu);
        lv_category = (ListView) findViewById(R.id.lv_category);
        move_to_cart = (ImageView) findViewById(R.id.move_to_cart);
        tv_login = (TextView) findViewById(R.id.tv_login);
    }
}