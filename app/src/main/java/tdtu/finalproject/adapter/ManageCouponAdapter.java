package tdtu.finalproject.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tdtu.finalproject.R;
import tdtu.finalproject.activity.AddCoupon;
import tdtu.finalproject.activity.ManageCoupon;
import tdtu.finalproject.model.Coupon;
import tdtu.finalproject.util.Server;

public class ManageCouponAdapter extends BaseAdapter {
    Context context;
    ArrayList<Coupon> coupons;

    public ManageCouponAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Coupon>data){
        this.coupons = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return coupons.size();
    }

    @Override
    public Object getItem(int i) {
        return coupons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_manage_coupon, null);
            viewHolder.code = view.findViewById(R.id.coupon_name);
            viewHolder.value = view.findViewById(R.id.coupon_value);
            viewHolder.edit = view.findViewById(R.id.manage_coupon_edit);
            viewHolder.delete = view.findViewById(R.id.manage_coupon_delete);
            viewHolder.save = view.findViewById(R.id.manage_coupon_save);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        final Coupon coupon = coupons.get(i);
        viewHolder.code.setText(coupon.getCode());
        viewHolder.value.setText(Integer.toString(coupon.getValue()));
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalViewHolder.edit.setVisibility(view.INVISIBLE);
                finalViewHolder.save.setVisibility(view.VISIBLE);
                finalViewHolder.code.setFocusableInTouchMode(true);
                finalViewHolder.value.setFocusableInTouchMode(true);
            }
        });
        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalViewHolder.save.setVisibility(view.INVISIBLE);
                finalViewHolder.edit.setVisibility(view.VISIBLE);
                finalViewHolder.code.setFocusableInTouchMode(false);
                finalViewHolder.value.setFocusableInTouchMode(false);
                finalViewHolder.value.setFocusable(false);
                finalViewHolder.value.setFocusable(false);
                updateCoupon(coupon.getCode(), finalViewHolder.code.getText().toString(), finalViewHolder.value.getText().toString());
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Message!");
                builder.setMessage("Bạn có chắc chắn muốn xóa mã này");
                builder.setNegativeButton("hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteCoupon(coupon.getCode());
                    }
                });
                AlertDialog al = builder.create();
                al.show();
            }
        });
        return view;
    }

    private void deleteCoupon(final String code) {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Message!");
        builder.setMessage("Đã xóa mã này");
        builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = builder.create();
        al.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.deleteCoupon, new Response.Listener<String>() {
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
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("code", code);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
        Intent intent = new Intent(context, ManageCoupon.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void updateCoupon(final String couponCode, final String code2, final String value) {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Message!");
        builder.setMessage("Update mã giảm giá thành công");
        builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = builder.create();
        al.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.updateCoupon, new Response.Listener<String>() {
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
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("code", couponCode);
                hashMap.put("code2", code2);
                hashMap.put("value", value);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public class ViewHolder{
        TextInputEditText code, value;
        ImageView edit, delete, save;
    }
}
