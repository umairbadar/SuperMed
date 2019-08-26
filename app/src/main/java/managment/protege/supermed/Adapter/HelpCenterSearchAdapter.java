package managment.protege.supermed.Adapter;

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
import managment.protege.supermed.Model.HelpCenterSearchModel;
import managment.protege.supermed.R;

public class HelpCenterSearchAdapter extends RecyclerView.Adapter<HelpCenterSearchAdapter.MyViewHolder> {

    public HelpCenterSearchAdapter(List<HelpCenterSearchModel> list) {
        this.list = list;
    }

    private List<HelpCenterSearchModel> list;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_help_way, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HelpCenterSearchModel Pro = list.get(position);
        holder.title.setText(Pro.getTopic());
        holder.LL_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_Apps.getMainActivity().backfunction(new faqs());
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