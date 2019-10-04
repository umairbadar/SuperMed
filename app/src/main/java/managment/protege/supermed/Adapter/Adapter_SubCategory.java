package managment.protege.supermed.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import managment.protege.supermed.Model.Model_SubCategory;
import managment.protege.supermed.R;

public class Adapter_SubCategory extends RecyclerView.Adapter<Adapter_SubCategory.ViewHolder> {

    List<Model_SubCategory> list;
    Context context;

    public Adapter_SubCategory(List<Model_SubCategory> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_category, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model_SubCategory item = list.get(position);

        holder.tv_sub_cat_name.setText(item.getName());

        if (item.getThirdlevels().equals("[]")){
            holder.img_drop_down.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_sub_cat_name;
        ImageView img_drop_down;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_sub_cat_name = itemView.findViewById(R.id.tv_sub_cat_name);
            img_drop_down = itemView.findViewById(R.id.img_drop_down);
        }
    }
}
