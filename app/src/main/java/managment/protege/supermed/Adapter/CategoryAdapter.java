package managment.protege.supermed.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Fragment.SubCategory;
import managment.protege.supermed.Model.CategoryModel;
import managment.protege.supermed.R;

/**
 * Created by wahaj on 6/12/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    public CategoryAdapter(List<CategoryModel> list) {
        this.list = list;
    }

    private List<CategoryModel> list;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final CategoryModel Pro = list.get(position);
        holder.title.setText(Pro.getName());
        holder.detail.setText(Pro.getBrief_intro());
        holder.LL_subCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("id", Pro.getCatId());
                Main_Apps.getMainActivity().backfunction(new SubCategory(), args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, detail, genre;
        LinearLayout LL_subCat;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            detail = (TextView) view.findViewById(R.id.brief);
            iv = (ImageView) view.findViewById(R.id.iv);
            LL_subCat = (LinearLayout) view.findViewById(R.id.LL_subCat);
        }
    }
}
