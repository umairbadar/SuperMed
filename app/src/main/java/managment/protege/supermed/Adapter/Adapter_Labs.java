package managment.protege.supermed.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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
        Model_Labs item = list.get(position);
        holder.tv_name.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        ImageView img_lab;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            img_lab = itemView.findViewById(R.id.img_lab);
        }
    }
}
