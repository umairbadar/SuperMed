package managment.protege.supermed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import managment.protege.supermed.Model.EmergencyModel;
import managment.protege.supermed.R;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.MyViewHolder> {
    Context context;
    private List<EmergencyModel> emergencyModelList;
    private OnItemClickListener onItemClickListeners;

    public interface OnItemClickListener {
        void onItemClick(View view, EmergencyModel obj);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListeners = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button emergency_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            emergency_title = (Button) itemView.findViewById(R.id.emergency_title);
        }
    }

    public EmergencyAdapter(List<EmergencyModel> emergencyModelList, Context context) {
        this.emergencyModelList = emergencyModelList;
        this.context = context;
    }

    @Override
    public EmergencyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emergency_contact_list, parent, false);
        return new EmergencyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyAdapter.MyViewHolder holder, int position) {
        final EmergencyModel Pro = emergencyModelList.get(position);
        holder.emergency_title.setText(Pro.getName() + "-" + Pro.getContact());
        holder.emergency_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListeners != null) {
                    onItemClickListeners.onItemClick(view, Pro);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return emergencyModelList.size();
    }


}
