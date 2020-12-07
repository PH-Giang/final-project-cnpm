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
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

public class SignIn extends AppCompatActivity {
    TextInputEditText fullname, email, password, phone, pass2, address;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getWidget();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                builder.setTitle("Message");
                builder.setMessage("Bạn có chắc muốn tạo tài khoản ?");
                builder.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(fullname.getText().toString().isEmpty()){
                            fullname.requestFocus();
                            Toast.makeText(getApplicationContext(), "một hoặc vài trường còn trống", Toast.LENGTH_SHORT).show();
                        }else if(email.getText().toString().isEmpty()){
                            email.requestFocus();
                            Toast.makeText(getApplicationContext(), "một hoặc vài trường còn trống", Toast.LENGTH_SHORT).show();
                        }else if(password.getText().toString().isEmpty()){
                            password.requestFocus();
                            Toast.makeText(getApplicationContext(), "một hoặc vài trường còn trống", Toast.LENGTH_SHORT).show();
                        }else if(phone.getText().toString().isEmpty()){
                            phone.requestFocus();
                            Toast.makeText(getApplicationContext(), "một hoặc vài trường còn trống", Toast.LENGTH_SHORT).show();
                        }else if(pass2.getText().toString().isEmpty() ||
                                pass2.getText().toString().length() > 16){
                            pass2.requestFocus();
                            Toast.makeText(getApplicationContext(), "một hoặc vài trường còn trống", Toast.LENGTH_SHORT).show();
                        }else if(address.getText().toString().isEmpty()){
                            address.requestFocus();
                            Toast.makeText(getApplicationContext(), "một hoặc vài trường còn trống", Toast.LENGTH_SHORT).show();
                        }else {
                            hasEmail();
                        }
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog al = builder.create();
                al.show();
            }
        });
    }

    /**check email tồn tại chứ, nếu chưa -> tạo tài khoản*/
    public void hasEmail() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.checkUserDirection;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                            builder.setTitle("Lỗi!");
                            builder.setMessage("Email đã tồn tại");
                            builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog al = builder.create();
                            al.show();
                        }else{
                            createAccount();
                            Toast.makeText(SignIn.this, "Đăng kí tài khoản thành công", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignIn.this, LoginActivity.class);
                            startActivity(intent);
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
                param.put("Customer_email", email.getText().toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**tạo tài khoản*/
    public void createAccount() {
        RequestQueue requestQueue = Volley.newRequestQueue(SignIn.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.insertUserDirection, new Response.Listener<String>() {
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
                try {
                    jsonObject.put("Customer_name", fullname.getText().toString());
                    jsonObject.put("Customer_phone", phone.getText().toString());
                    jsonObject.put("Customer_email", email.getText().toString());
                    int lenPass2 = pass2.getText().toString().length();
                    if(pass2.getText().toString().length() < 16){
                        for(int i = 0; i < (16 - lenPass2); i++){
                            pass2.setText(pass2.getText().toString() + "a");
                        }
                    }
                    String pass = "";
                    try{
                        pass = encrypt(password.getText().toString(), pass2.getText().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    jsonObject.put("Customer_password", pass);
                    jsonObject.put("Customer_pass2", pass2.getText().toString());
                    jsonObject.put("Customer_address", address.getText().toString());
                }catch (Exception e){
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

    /** mã hóa mật khẩu*/
    public String encrypt(String original, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] byteEncrypted = cipher.doFinal(original.getBytes());
        String encrypted =  Base64.getEncoder().encodeToString(byteEncrypted);
        return encrypted;
    }

    private void getWidget() {
        fullname = findViewById(R.id.si_fullname);
        email = findViewById(R.id.si_username);
        password = findViewById(R.id.si_password);
        phone = findViewById(R.id.si_phone);
        pass2 = findViewById(R.id.si_pass2);
        create = findViewById(R.id.si_create);
        address = findViewById(R.id.si_addresss);
    }
}