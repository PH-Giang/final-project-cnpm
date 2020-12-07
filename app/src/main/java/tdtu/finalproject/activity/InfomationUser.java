package tdtu.finalproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tdtu.finalproject.R;
import tdtu.finalproject.model.User;

public class InfomationUser extends AppCompatActivity {
    private TextView name, email, phone, address;
    private Button seeOrder, gohome, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_user);
        getWidget();
        setView();
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfomationUser.this, MainActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.customer = new User();
                Toast.makeText(InfomationUser.this, "Đăng xuất thành công", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(InfomationUser.this, MainActivity.class);
                startActivity(intent);
            }
        });
        seeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfomationUser.this, MyOrder.class);
                startActivity(intent);
            }
        });
    }



    private void setView() {
        name.setText(LoginActivity.customer.getName());
        email.setText(LoginActivity.customer.getUsername());
        phone.setText(LoginActivity.customer.getPhone());
        address.setText(LoginActivity.customer.getAddress());
    }

    private void getWidget() {
        name = findViewById(R.id.info_user);
        email = findViewById(R.id.info_email);
        phone = findViewById(R.id.info_phone);
        address = findViewById(R.id.info_address);
        seeOrder = findViewById(R.id.info_seeorder);
        gohome = findViewById(R.id.info_home);
        logout = findViewById(R.id.info_logout);
    }


}