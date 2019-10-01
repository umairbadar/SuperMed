package managment.protege.supermed.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import managment.protege.supermed.Fragment.DoctorDetailFragment;
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

        final ModelSpecialityDoctor item = list.get(position);

        holder.tv_doc_name.setText(item.getDoc_name());
        holder.tv_doc_designation.setText(item.getDoc_designation());
        holder.tv_doc_timing.setText(item.getDoc_time());

        if (item.getGender().equals("Male") || item.getGender().equals("male")){
            holder.img_doc.setImageResource(R.drawable.male_doc);
        } else {
            holder.img_doc.setImageResource(R.drawable.female_doc);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog();
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                DoctorDetailFragment dialog = new DoctorDetailFragment();
                Bundle args = new Bundle();
                args.putString("Id", item.getDoc_id());
                dialog.setArguments(args);
                dialog.setCancelable(false);
                dialog.show(manager, "Tag");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_doc;
        TextView tv_doc_name, tv_doc_designation, tv_doc_timing;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            img_doc = itemView.findViewById(R.id.img_doc);
            tv_doc_name = itemView.findViewById(R.id.tv_doc_name);
            tv_doc_designation = itemView.findViewById(R.id.tv_doc_designation);
            tv_doc_timing = itemView.findViewById(R.id.tv_doc_timing);
            linearLayout = itemView.findViewById(R.id.linearlayout);
        }
    }
}
