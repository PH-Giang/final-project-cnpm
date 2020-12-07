package tdtu.finalproject.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tdtu.finalproject.R;
import tdtu.finalproject.activity.ManageProduct;
import tdtu.finalproject.activity.SignIn;
import tdtu.finalproject.model.Product;
import tdtu.finalproject.util.CheckConnection;
import tdtu.finalproject.util.Server;

public class ManageProductAdapter extends BaseAdapter {
    public ArrayList<Product> products;
    public Context context;

    public void setData(ArrayList<Product> data){
        this.products = data;
        notifyDataSetChanged();
    }

    public ManageProductAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_manage_product, null);
            viewHolder.img = view.findViewById(R.id.manage_image);
            viewHolder.name = view.findViewById(R.id.manage_name);
            viewHolder.price = view.findViewById(R.id.manage_price);
            viewHolder.category = view.findViewById(R.id.manage_category);
            viewHolder.delete = view.findViewById(R.id.manage_delete_product);
            viewHolder.edit = view.findViewById(R.id.manage_edit_product);
            viewHolder.save = view.findViewById(R.id.manage_save_product);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        final Product product = products.get(i);

        viewHolder.name.setText(product.getName());
        viewHolder.price.setText(Integer.toString(product.getPrice()));
        Picasso.with(context).load(product.getImage()).into(viewHolder.img);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final ViewHolder finalViewHolder = viewHolder;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.directionCategory,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    if(product.getId_cate() == jsonObject.getInt("id")){
                                       finalViewHolder.category.setText(jsonObject.getString("name"));
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonArrayRequest);

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalViewHolder.save.setVisibility(view.VISIBLE);
                finalViewHolder.edit.setVisibility(view.INVISIBLE);
                finalViewHolder.price.setFocusableInTouchMode(true);
                finalViewHolder.category.setFocusableInTouchMode(true);
                finalViewHolder.name.setFocusableInTouchMode(true);
            }
        });
        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.directionCategory,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if(response != null){
                                    boolean cateIsExist = false;
                                    for (int i = 0; i < response.length(); i++){
                                        try {
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            if(finalViewHolder.category.getText().toString().equals(jsonObject.getString("name"))){
                                                cateIsExist = true;
                                                finalViewHolder.save.setVisibility(view.INVISIBLE);
                                                finalViewHolder.edit.setVisibility(view.VISIBLE);
                                                finalViewHolder.category.setFocusable(false);
                                                finalViewHolder.price.setFocusable(false);
                                                finalViewHolder.name.setFocusable(false);
                                                finalViewHolder.price.setFocusableInTouchMode(false);
                                                finalViewHolder.category.setFocusableInTouchMode(false);
                                                finalViewHolder.name.setFocusableInTouchMode(false);
                                                updateProduct(product.getId(),
                                                        jsonObject.getInt("id"),
                                                        Integer.parseInt(finalViewHolder.price.getText().toString()),
                                                        finalViewHolder.name.getText().toString());
                                                break;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (!cateIsExist){
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                        builder.setTitle("Lỗi!");
                                        builder.setMessage("Không tìm thấy danh mục");
                                        builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                        AlertDialog al = builder.create();
                                        al.show();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                requestQueue.add(jsonArrayRequest);
            }
        });

        finalViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Message!");
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm này");
                builder.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_LONG).show();
                        delete(product.getId());
                    }
                });
                builder.setNegativeButton("hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog al = builder.create();
                al.show();
            }
        });
        return view;
    }

    private void delete(final int id) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.deleteProduct, new Response.Listener<String>() {
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
                hashMap.put("id", Integer.toString(id));
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
        Intent intent = new Intent(context, ManageProduct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void updateProduct(final int id, final int cate, final int price, final String name) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Message!");
        builder.setMessage("Cập nhật sản phẩm thành công");
        builder.setNegativeButton("đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = builder.create();
        al.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.updateProduct, new Response.Listener<String>() {
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
                hashMap.put("id", Integer.toString(id));
                hashMap.put("idCate", Integer.toString(cate));
                hashMap.put("price", Integer.toString(price));
                hashMap.put("name", name);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public class ViewHolder{
        ImageView img;
        EditText name, price;
        EditText category;
        ImageView delete, edit, save;
    }
}
