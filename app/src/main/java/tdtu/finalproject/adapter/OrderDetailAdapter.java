package tdtu.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tdtu.finalproject.R;
import tdtu.finalproject.activity.LoginActivity;
import tdtu.finalproject.activity.OrderDetail;
import tdtu.finalproject.model.DetailOrder;
import tdtu.finalproject.model.MyOrderModel;
import tdtu.finalproject.util.Server;

public class OrderDetailAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DetailOrder> detailOrders;

    public OrderDetailAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<DetailOrder> data){
        this.detailOrders = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return detailOrders.size();
    }

    @Override
    public Object getItem(int i) {
        return detailOrders.get(i);
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
            view = layoutInflater.inflate(R.layout.row_detail_order, null);
            viewHolder.name = view.findViewById(R.id.detail_name);
            viewHolder.price = view.findViewById(R.id.detail_price);
            viewHolder.quantity = view.findViewById(R.id.detail_quantity);
            viewHolder.img = view.findViewById(R.id.detail_img);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final DetailOrder item = detailOrders.get(i);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String direction = Server.detailDirection;
        final ViewHolder finalViewHolder = viewHolder;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, direction, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i =0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            finalViewHolder.name.setText(jsonObject.getString("name"));
                            Picasso.with(context).load(Server.imgaeDirection + jsonObject.getString("image")).
                                    placeholder(R.drawable.cancel).
                                    error(R.drawable.warning).
                                    into(finalViewHolder.img);
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
                param.put("Product_id", Integer.toString(item.getProductID()));
                return param;
            }
        };
        requestQueue.add(stringRequest);
        viewHolder.price.setText(Integer.toString(item.getCost()));
        viewHolder.quantity.setText(Integer.toString(item.getQuantity()));
        return view;
    }

    public static class ViewHolder{
        TextView name, price, quantity;
        ImageView img;
    }
}
