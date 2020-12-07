package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import tdtu.finalproject.R;
import tdtu.finalproject.model.User;
import tdtu.finalproject.util.Server;

public class LoginActivity extends AppCompatActivity  {
    TextInputEditText user, password;
    Button login, forgetPasswrod, newUser;

    public static User customer = new User();
    public boolean isPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWidget();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Bạn chưa điền mật khẩu hoặc email");
                    builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog al = builder.create();
                    al.show();
                }else {
                    loginUser();
                }
            }
        });
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignIn.class);
                startActivity(intent);
            }
        });
        forgetPasswrod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    /**lấy pass 2 để mã hóa*/
    private void loginUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.checkUserDirection;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length() == 1){
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String pass2 = jsonObject.getString("pass2");
                            checkUser(pass2);
                        }else{

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
                param.put("Customer_email", user.getText().toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    /**check user*/
    private void checkUser(final String pass2) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.getUserDirection;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length() == 1){
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            customer = new User(
                                    jsonObject.getString("email"),
                                    jsonObject.getString("password"),
                                    jsonObject.getString("phone"),
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("pass2"),
                                    jsonObject.getString("address")
                            );
                            Toast.makeText(getApplicationContext(), "Chào mừng, " + customer.getName().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Lỗi!");
                            builder.setMessage("Email hoặc tài khoản không đúng");
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
                param.put("Customer_email", user.getText().toString());
                String encryptPassword = "";
                try{
                    encryptPassword = encrypt(password.getText().toString(), pass2);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                param.put("Customer_password", encryptPassword);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**mã hóa mật khẩu*/
    public String encrypt(String original, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] byteEncrypted = cipher.doFinal(original.getBytes());
        String encrypted =  Base64.getEncoder().encodeToString(byteEncrypted);
        return encrypted;
    }


    private void getWidget() {
        user = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        login = findViewById(R.id.go_login);
        forgetPasswrod = findViewById(R.id.forgetPassword);
        newUser = findViewById(R.id.new_user);
    }
}