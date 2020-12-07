package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import java.util.HashMap;
import java.util.Map;

import tdtu.finalproject.R;
import tdtu.finalproject.model.User;
import tdtu.finalproject.util.Server;

public class ForgotPassword extends AppCompatActivity {
    private TextInputEditText email;
    private Button getPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWidget();
        getPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Bạn chưa điền email");
                    builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog al = builder.create();
                    al.show();
                }else {
                    checkUser();
                }
            }
        });
    }

    /**check sư tồn tại chủa email*/
    private void checkUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String direction = Server.checkUserDirection;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length() > 0) {
                            Intent intent = new Intent(ForgotPassword.this, GetPassword.class);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            intent.putExtra("EMAIL", jsonObject.getString("email"));
                            startActivity(intent);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                            builder.setTitle("Lỗi!");
                            builder.setMessage("Email không tồn tại");
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
                param.put("Customer_email", email.getText().toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getWidget() {
        email = findViewById(R.id.fg_email);
        getPass = findViewById(R.id.fg_next);
    }
}