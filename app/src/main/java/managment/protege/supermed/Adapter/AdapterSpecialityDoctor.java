package managment.protege.supermed.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import managment.protege.supermed.Model.ModelSpecialityDoctor;
import managment.protege.supermed.R;

public class AdapterSpecialityDoctor extends RecyclerView.Adapter<AdapterSpecialityDoctor.ViewHolder> {

    private List<ModelSpecialityDoctor> list;
    Context context;

    public AdapterSpecialityDoctor(List<ModelSpecialityDoctor> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_speciality_doctor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ModelSpecialityDoctor item = list.get(position);

        holder.tv_doc_name.setText(item.getDoc_name());
        holder.tv_doc_designation.setText(item.getDoc_designation());
        holder.tv_doc_timing.setText(item.getDoc_time());

        if (item.getGender().equals("Male") || item.getGender().equals("male")){
            holder.img_doc.setImageResource(R.drawable.male_doc);
        } else {
            holder.img_doc.setImageResource(R.drawable.female_doc);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_doc;
        TextView tv_doc_name, tv_doc_designation, tv_doc_timing;

        public ViewHolder(View itemView) {
            super(itemView);

            img_doc = itemView.findViewById(R.id.img_doc);
            tv_doc_name = itemView.findViewById(R.id.tv_doc_name);
            tv_doc_designation = itemView.findViewById(R.id.tv_doc_designation);
            tv_doc_timing = itemView.findViewById(R.id.tv_doc_timing);
        }
    }
}
