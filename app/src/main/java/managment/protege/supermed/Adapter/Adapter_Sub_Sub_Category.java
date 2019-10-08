package managment.protege.supermed.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Fragment.Fragment_Sub_Sub_Cat_Product;
import managment.protege.supermed.Model.Model_Sub_Sub_Category;
import managment.protege.supermed.R;

public class Adapter_Sub_Sub_Category extends RecyclerView.Adapter<Adapter_Sub_Sub_Category.ViewHolder> {

    List<Model_Sub_Sub_Category> list;
    Context context;

    public Adapter_Sub_Sub_Category(List<Model_Sub_Sub_Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_sub_category, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Model_Sub_Sub_Category item = list.get(position);

        holder.tv_sub_cat_name.setText(item.getName());

        holder.tv_sub_cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("slug", item.getSlug());
                args.putString("sub_slug", item.getSub_slug());
                args.putString("main_slug", item.getMain_slug());
                Main_Apps.getMainActivity().backfunction(new Fragment_Sub_Sub_Cat_Product(), args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_sub_cat_name;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_sub_cat_name = itemView.findViewById(R.id.tv_sub_cat_name);
        }
    }
}
