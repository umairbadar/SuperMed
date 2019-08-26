package managment.protege.supermed.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import managment.protege.supermed.Activity.Main_Apps;
import managment.protege.supermed.Fragment.ProductDetail;
import managment.protege.supermed.Model.TopCategories;
import managment.protege.supermed.R;

/**
 * Created by wahaj on 6/12/2018.
 */

public class TopCatagorieAdapter extends RecyclerView.Adapter<TopCatagorieAdapter.MyViewHolder> {

    public TopCatagorieAdapter(List<TopCategories> catalist) {
        this.catalist = catalist;
    }

    private List<TopCategories> catalist;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cata_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TopCategories cata = catalist.get(position);
        holder.title.setText(cata.getTitle());
        holder.iv.setImageResource(cata.getImage());
    }

    @Override
    public int getItemCount() {
        return catalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        CircleImageView iv;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            iv = (CircleImageView) view.findViewById(R.id.circle);
        }
    }
}
