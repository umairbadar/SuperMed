package managment.protege.supermed.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import managment.protege.supermed.Model.DoctorsModel;
import managment.protege.supermed.R;

public class DocInfoAdapter extends RecyclerView.Adapter<managment.protege.supermed.Adapter.DocInfoAdapter.MyViewHolder> {

    public DocInfoAdapter(List<DoctorsModel> list) {
        this.list = list;
    }

    private List<DoctorsModel> list;

    @NonNull
    @Override
    public managment.protege.supermed.Adapter.DocInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_doc_info, parent, false);

        return new managment.protege.supermed.Adapter.DocInfoAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull managment.protege.supermed.Adapter.DocInfoAdapter.MyViewHolder holder, int position) {

        final DoctorsModel Pro = list.get(position);
        holder.docInformation_hospital.setText((Pro.getFirstName().isEmpty() ? "" : Pro.getFirstName()) + " " + (Pro.getLastName().isEmpty() ? "" : Pro.getLastName()));
        holder.docInformation_qualifications.setText(Pro.getDesignation() + "");
        holder.docInformation_time.setText(Pro.getDaystime() + "");
        if (Pro.getVisitFee().equals("0") && Pro.getClinicFee().equals("0")) {
            holder.doc_fee.setVisibility(View.GONE);
        } else if (Integer.parseInt(Pro.getVisitFee()) > 0 || Integer.parseInt(Pro.getClinicFee()) > 0) {
            holder.doc_fee.setVisibility(View.VISIBLE);
            holder.doc_fee.setText("Vist Fee : " + Pro.getVisitFee() + "  |  " + " Clinic Fee : " + Pro.getClinicFee());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView docInformation_hospital, docInformation_qualifications, docInformation_time, doc_fee, docInformation_additional;

        public MyViewHolder(View view) {
            super(view);
            docInformation_hospital = (TextView) view.findViewById(R.id.docInformation_hospital);
            docInformation_qualifications = (TextView) view.findViewById(R.id.docInformation_qualifications);
            docInformation_time = (TextView) view.findViewById(R.id.docInformation_time);
            doc_fee = (TextView) view.findViewById(R.id.doc_fee);
            docInformation_additional = (TextView) view.findViewById(R.id.docInformation_additional);
        }
    }
}
