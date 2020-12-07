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
import android.widget.TextView;

import java.util.ArrayList;

import tdtu.finalproject.R;
import tdtu.finalproject.activity.AdminListOrder;
import tdtu.finalproject.model.AdminOrder;
import tdtu.finalproject.model.MyOrderModel;

public class AdminOrderAdapter extends BaseAdapter {
    public ArrayList<AdminOrder> adminOrders;
    Context context;
    public AdminOrderAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<AdminOrder> data){
        this.adminOrders = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return adminOrders.size();
    }

    @Override
    public Object getItem(int i) {
        return adminOrders.get(i);
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
            view = layoutInflater.inflate(R.layout.row_admin_order, null);
            viewHolder.id = view.findViewById(R.id.admin_order_id);
            viewHolder.customer = view.findViewById(R.id.admin_customer);
            viewHolder.phone = view.findViewById(R.id.admin_cusPhone);
            viewHolder.address = view.findViewById(R.id.admin_cusAddress);
            viewHolder.total = view.findViewById(R.id.admin_order_total);
            viewHolder.date = view.findViewById(R.id.admin_order_date);
            viewHolder.status = view.findViewById(R.id.admin_order_status);
            view.setTag(viewHolder);
        }else{
            viewHolder = (AdminOrderAdapter.ViewHolder) view.getTag();
        }
        final AdminOrder item = adminOrders.get(i);
        viewHolder.customer.setText(item.getCustomer());
        viewHolder.phone.setText(item.getPhone());
        viewHolder.address.setText(item.getAddress());
        viewHolder.id.setText(Integer.toString(item.getId()));
        viewHolder.total.setText(Integer.toString(item.getTotal()));
        viewHolder.date.setText(item.getDate());
        if(item.getStatus() == 1){
            viewHolder.status.setImageResource(R.drawable.ic_paid);
        }else{
            viewHolder.status.setImageResource(R.drawable.ic_notpaid);
        }
        viewHolder.id.setText("Mã đơn hàng: " + item.getId());
        return view;
    }

    public class ViewHolder{
        TextView id, total, date, customer, phone, address;
        ImageView status;
    }
}
