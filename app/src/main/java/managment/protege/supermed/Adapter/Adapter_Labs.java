package managment.protege.supermed.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import managment.protege.supermed.Fragment.Fragment_Tests;
import managment.protege.supermed.Model.Model_Brand;
import managment.protege.supermed.Model.Model_Labs;
import managment.protege.supermed.R;

public class Adapter_Labs extends RecyclerView.Adapter<Adapter_Labs.ViewHolder> {

    List<Model_Labs> list;
    Context context;

    public Adapter_Labs(List<Model_Labs> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lab_list, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Model_Labs item = list.get(position);

        String ImageLink = item.getImage();
        if (!ImageLink.equals("")) {
            ImageLink = ImageLink.replaceAll(" ", "%20");
            Picasso.get()
                    .load(ImageLink)
                    .fit()
                    .into(holder.img_lab);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new Fragment_Tests();
                Bundle args = new Bundle();
                args.putString("LabID", item.getId());
                myFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_lab;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);

            img_lab = itemView.findViewById(R.id.img_lab);
            card = itemView.findViewById(R.id.card);
        }
    }
}
