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

import org.json.JSONArray;
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
import tdtu.finalproject.util.Server;

public class GetPassword extends AppCompatActivity {
    private TextInputEditText pass2;
    private Button getPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);
        getWidget();
        getPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pass2.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(GetPassword.this);
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Bạn chưa điền mật khẩu cấp 2");
                    builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog al = builder.create();
                    al.show();
                }else {
                    getPassword();
                }
            }
        });
    }

    /**Kiểm tra pass 2 đúng ko. Nếu đúng -> gọi phương thức giải mã*/
    private void getPassword() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.getPasswordDirection;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String password = "";
                            try{
                                password = decrypt(jsonObject.getString("password"), pass2.getText().toString());
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "Mật khẩu của bạn là: " + password, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(GetPassword.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(GetPassword.this);
                            builder.setTitle("Lỗi!");
                            builder.setMessage("Mật khẩu cấp 2 sai");
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
                Intent intent = getIntent();
                String email = intent.getStringExtra("EMAIL");
                int lenPass2 = pass2.getText().toString().length();
                if(pass2.getText().toString().length() < 16){
                    for(int i = 0; i < (16 - lenPass2); i++){
                        pass2.setText(pass2.getText().toString() + "a");
                    }
                }
                param.put("Customer_email", email);
                param.put("Customer_pass2", pass2.getText().toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**giải mã*/
    public String decrypt(String password, String pass2) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(pass2.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] byteEncrypted = Base64.getDecoder().decode(password);
        byte[] byteDecrypted = cipher.doFinal(byteEncrypted);
        String decrypted = new String(byteDecrypted);
        return decrypted;
    }

    private void getWidget() {
        pass2 = findViewById(R.id.fg_pass2);
        getPass = findViewById(R.id.fg_getPass);
    }
}