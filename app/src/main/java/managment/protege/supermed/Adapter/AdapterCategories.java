package managment.protege.supermed.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Fragment.Fragment_Cat_Product;
import managment.protege.supermed.Model.ModelCategories;
import managment.protege.supermed.R;

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.ViewHolder> {

    Context context;
    List<ModelCategories> list;

    public AdapterCategories(Context context, List<ModelCategories> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categories, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ModelCategories item = list.get(position);
        holder.tv_catName.setText(item.getName());

        String ImageLink = item.getImage();
        if (!ImageLink.equals("")) {
            ImageLink = ImageLink.replaceAll(" ", "%20");
            Picasso.get()
                    .load(ImageLink)
                    .resize(80, 80)
                    .centerCrop()
                    .placeholder(R.drawable.ic_category)
                    .into(holder.img_cat);
        }

        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putString("slug", item.getSlug());
                Main_Apps.getMainActivity().backfunction(new Fragment_Cat_Product(), args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_catName;
        LinearLayout layout1;
        ImageView img_cat;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_catName = itemView.findViewById(R.id.tv_catName);
            img_cat = itemView.findViewById(R.id.img_cat);
            layout1 = itemView.findViewById(R.id.layout1);
        }
    }
}
