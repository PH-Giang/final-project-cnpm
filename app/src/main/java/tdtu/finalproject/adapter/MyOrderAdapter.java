package tdtu.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tdtu.finalproject.R;
import tdtu.finalproject.model.MyOrderModel;

public class MyOrderAdapter extends BaseAdapter {
    public ArrayList<MyOrderModel> myOrders;
    Context context;

    public MyOrderAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<MyOrderModel> data){
        this.myOrders = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myOrders.size();
    }

    @Override
    public Object getItem(int i) {
        return myOrders.get(i);
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
            view = layoutInflater.inflate(R.layout.row_item_myorder, null);
            viewHolder.id = view.findViewById(R.id.myorder_id);
            viewHolder.total = view.findViewById(R.id.myorder_total);
            viewHolder.date = view.findViewById(R.id.myorder_date);
            viewHolder.status = view.findViewById(R.id.myorder_status);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        MyOrderModel item = myOrders.get(i);
        viewHolder.id.setText(Integer.toString(item.getId()));
        viewHolder.total.setText(Integer.toString(item.getTotal()));
        viewHolder.date.setText(item.getDate());
        if(item.isStatus() == true){
            viewHolder.status.setImageResource(R.drawable.ic_paid);
        }else{
            viewHolder.status.setImageResource(R.drawable.ic_notpaid);
        }
        viewHolder.id.setText("Mã đơn hàng: " + item.getId());
        return view;
    }
    public class ViewHolder{
        TextView id, total, date;
        ImageView status;

    }
}
