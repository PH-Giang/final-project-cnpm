package tdtu.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tdtu.finalproject.R;
import tdtu.finalproject.model.Category;

public class CategoryAdapter extends BaseAdapter {
    ArrayList<Category> categories;
    Context context;

    public CategoryAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<Category> list){
        this.categories = list;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        ImageView img_category;
        TextView tv_category_name;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_navigation_bar, null);
            viewHolder.tv_category_name = view.findViewById(R.id.tv_category_name);
            viewHolder.img_category = view.findViewById(R.id.img_category);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Category category = (Category) getItem(i);
        viewHolder.tv_category_name.setText(category.getName());
        Picasso.with(context).load(category.getImage()).
                placeholder(R.drawable.cancel).
                error(R.drawable.warning).
                into(viewHolder.img_category);
        return view;
    }
}
