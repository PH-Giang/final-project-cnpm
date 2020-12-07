package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import tdtu.finalproject.R;
import tdtu.finalproject.adapter.AdminFeatureAdapter;
import tdtu.finalproject.model.Feature;
import tdtu.finalproject.util.Server;

public class AdminPage extends AppCompatActivity {
    private TextView name, confirm, uncomfirmed, revenue;
    private TextView revenue_title;
    private RecyclerView admin_feature;
    private Spinner date, month, year;
    private Button pick, logout;
    private RelativeLayout btn_confirm, btn_unconfirmed;

    private ArrayList<Feature> features = new ArrayList<>();
    private AdminFeatureAdapter adminFeatureAdapter;
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> months = new ArrayList<>();
    private ArrayList<String> years = new ArrayList<>();
    public static int datePicked = 0, monthPicked = 0, yearPicked = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        getWidget();
        setRecyclerView();
        setDate();
        setSpinner();
        //set ngày hiện tại nếu chưa chọn ngày
        if(datePicked == 0 && monthPicked == 0 && yearPicked == 0) {
            setCurrentDay();
        }
        getRevenue();
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date.getSelectedItem().toString().equals("ngày")||
                        month.getSelectedItem().toString().equals("tháng")||
                        year.getSelectedItem().toString().equals("năm")
                ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminPage.this);
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Một vài trường còn thiếu");
                    builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog al = builder.create();
                    al.show();
                }else{
                    datePicked = Integer.parseInt(date.getSelectedItem().toString());
                    monthPicked = Integer.parseInt(month.getSelectedItem().toString());
                    yearPicked = Integer.parseInt(year.getSelectedItem().toString());
                    getRevenue();
                }

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPage.this, AdminListOrder.class);
                intent.putExtra("STATUS", 1);
                startActivity(intent);
            }
        });
        btn_unconfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPage.this, AdminListOrder.class);
                intent.putExtra("STATUS", 0);
                startActivity(intent);
            }
        });
        admin_feature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPage.this, ManageProduct.class);
                startActivity(intent);
            }
        });
    }

    /**set ngày tháng năm hiện tại*/
    private void setCurrentDay() {
        Calendar cal = Calendar.getInstance();
        datePicked = cal.get(Calendar.DAY_OF_MONTH);
        monthPicked = cal.get(Calendar.MONTH) + 1;
        yearPicked = cal.get(Calendar.YEAR);
    }

    /**get doanh thu theo ngày pick*/
    private void getRevenue() {
        RequestQueue requestQueue = Volley.newRequestQueue(AdminPage.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getRevenue, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        int total =0;
                        int orderConfirm = 0;
                        int orderUnconfirmed = 0;
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getInt("status") == 0){
                                orderUnconfirmed +=1;
                            }else{
                                orderConfirm+=1;
                                total += jsonObject.getInt("total");
                            }
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                        revenue.setText(decimalFormat.format(total) + " Đ");
                        Calendar cal = Calendar.getInstance();
                        if((datePicked == cal.get(Calendar.DAY_OF_MONTH)) &&
                                (monthPicked == cal.get(Calendar.MONTH) + 1) &&
                                (yearPicked == cal.get(Calendar.YEAR))){
                            revenue_title.setText("Hôm nay");
                        }else {
                            revenue_title.setText(Integer.toString(datePicked) + "-" +
                                    Integer.toString(monthPicked) + "-" +
                                    Integer.toString(yearPicked));
                        }
                        confirm.setText(Integer.toString(orderConfirm));
                        uncomfirmed.setText(Integer.toString(orderUnconfirmed));

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
                hashMap.put("revenue_date", Integer.toString(datePicked));
                hashMap.put("revenue_month", Integer.toString(monthPicked));
                hashMap.put("revenue_year", Integer.toString(yearPicked));
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**cài đặt bộ ngày tháng năm*/
    private void setSpinner() {
        ArrayAdapter adapterDate = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dates);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date.setAdapter(adapterDate);
        ArrayAdapter adapterMonth = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(adapterMonth);
        ArrayAdapter adapterYear = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(adapterYear);
    }

    private void setDate() {
        dates.add("ngày");
        months.add("tháng");
        years.add("năm");
        for (int i = 1; i <= 31; i++){
            dates.add(Integer.toString(i));
        }
        for (int i = 1; i <=12; i++){
            months.add(Integer.toString(i));
        }
        for (int i = 2020; i <= 2021 ; i++){
            years.add(Integer.toString(i));
        }
    }

    /**set recycler view cho các tính năg*/
    private void setRecyclerView() {
        adminFeatureAdapter = new AdminFeatureAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        admin_feature.setLayoutManager(gridLayoutManager);
        admin_feature.setFocusable(false);
        admin_feature.setNestedScrollingEnabled(false);
        features.add(new Feature("Sản phẩm"));
        features.add(new Feature("Coupon"));
        adminFeatureAdapter.setData(features);
        admin_feature.setAdapter(adminFeatureAdapter);
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AdminPage.this, AdminPage.class);
        startActivity(intent);
        super.onBackPressed();

    }

    private void getWidget() {
        name = findViewById(R.id.admin_name);
        confirm = findViewById(R.id.admin_confirm_quantity);
        uncomfirmed = findViewById(R.id.admin_uncomfirmed_quantity);
        admin_feature = findViewById(R.id.admin_feature);
        date = findViewById(R.id.revenue_date);
        month = findViewById(R.id.revenue_month);
        year = findViewById(R.id.revenue_year);
        pick = findViewById(R.id.datepicker);
        revenue = findViewById(R.id.admin_revenue);
        revenue_title = findViewById(R.id.admin_revenue_title);
        logout = findViewById(R.id.admin_logout);
        btn_confirm =  findViewById(R.id.btn_confirm);
        btn_unconfirmed = findViewById(R.id.btn_unconfirmed);
    }
}