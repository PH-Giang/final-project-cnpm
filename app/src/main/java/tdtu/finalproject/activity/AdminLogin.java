package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class AdminLogin extends AppCompatActivity {
    private TextInputEditText admin, password;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        getWidget();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAdmin();
            }
        });
    }

    /**kiểm tra các field còn trống hay không*/
    private void checkAdmin() {
        if(admin.getText().toString().isEmpty() ||
        password.getText().toString().isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminLogin.this);
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
            adminLogin();
        }
    }

    /**check tài khoản*/
    private void adminLogin() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.getAdmin;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length()  == 1){
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            Intent intent = new Intent(AdminLogin.this, AdminPage.class);
                            startActivity(intent);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminLogin.this);
                            builder.setTitle("Lỗi!");
                            builder.setMessage("Tên đăng nhập hoặc mật khẩu sai");
                            builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog al = builder.create();
                            al.show();
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
                param.put("Admin_user", admin.getText().toString());
                param.put("Admin_password", password.getText().toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getWidget() {
        admin = findViewById(R.id.admin_user);
        password = findViewById(R.id.admin_password);
        login = findViewById(R.id.admin_login);
    }
}