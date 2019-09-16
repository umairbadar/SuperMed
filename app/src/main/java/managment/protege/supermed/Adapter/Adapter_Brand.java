package managment.protege.supermed.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ConcurrentModificationException;
import java.util.List;

import managment.protege.supermed.Model.Model_Brand;
import managment.protege.supermed.R;

public class Adapter_Brand extends RecyclerView.Adapter<Adapter_Brand.ViewHolder> {

    List<Model_Brand> list;
    Context context;

    public Adapter_Brand(List<Model_Brand> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_list, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Model_Brand item = list.get(position);

        String ImageLink = item.getImage();
        if (!ImageLink.equals("")) {
            ImageLink = ImageLink.replaceAll(" ", "%20");
            Picasso.get()
                    .load(ImageLink)
                    /*.resize(80, 80)
                    .centerCrop()*/
                    .placeholder(R.drawable.tab_miss)
                    .into(holder.img_brand);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, item.getId(),
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_brand;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            img_brand = itemView.findViewById(R.id.img_brand);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
