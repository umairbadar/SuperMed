package managment.protege.supermed.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import managment.protege.supermed.Model.OrderHistoryList;
import managment.protege.supermed.Model.OrderhistoryModel;
import managment.protege.supermed.R;

/**
 * Created by Developer on 6/21/2018.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
    int ordernumber;
    int dummyorder = 0;

    public OrderHistoryAdapter(List<OrderhistoryModel> orderHistoryLists) {
        this.orderHistoryLists = orderHistoryLists;
    }

    private List<OrderhistoryModel> orderHistoryLists;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        OrderhistoryModel cata = orderHistoryLists.get(position);

        holder.title.setText(" Order No " + cata.getOrderNo());
        holder.Price.setText(" Total Price: Rs " + cata.getOrderTotalPrice());
        holder.Sale.setText(cata.getOrderNote());
        if (cata.getOrderNote().equals("")) {
            holder.orderHistoryNote.setText("Order Note: No Note");
        } else {
            holder.orderHistoryNote.setText("Order Note: " + cata.getOrderNote());
        }
        holder.orderHistoryStatus.setText("Order Status: " + cata.getOrderStatus());
    }

    @Override
    public int getItemCount() {
        return orderHistoryLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, Price, Sale, number, orderHistoryStatus, orderHistoryNote;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.orderHistoryProductName);
            Price = (TextView) view.findViewById(R.id.orderHistoryProductActualPrice);
            Sale = (TextView) view.findViewById(R.id.orderHistoryProductSalePrice);
            orderHistoryStatus = (TextView) view.findViewById(R.id.orderHistoryStatus);
            orderHistoryNote = (TextView) view.findViewById(R.id.orderHistoryNote);

        }
    }
}
