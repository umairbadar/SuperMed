package managment.protege.supermed.Adapter;

import android.content.Context;
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
import managment.protege.supermed.Fragment.faqs;
import managment.protege.supermed.Model.CategoryModel;
import managment.protege.supermed.Model.HelpCenterModel;
import managment.protege.supermed.R;

/**
 * Created by wahaj on 6/12/2018.
 */

public class HelpCenterAdapter extends RecyclerView.Adapter<HelpCenterAdapter.MyViewHolder> {
    public static String data = "";

    public HelpCenterAdapter(List<HelpCenterModel> list) {
        this.list = list;
    }

    private List<HelpCenterModel> list;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_help_way, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final HelpCenterModel Pro = list.get(position);
        holder.title.setText(Pro.getTopic());
        holder.LL_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Apps.getMainActivity().backfunction(new faqs());
                data = Pro.getId();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, detail, genre;
        LinearLayout LL_help;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            detail = (TextView) view.findViewById(R.id.brief);
            iv = (ImageView) view.findViewById(R.id.iv);
            LL_help = (LinearLayout) view.findViewById(R.id.LL_help);
        }
    }
}
