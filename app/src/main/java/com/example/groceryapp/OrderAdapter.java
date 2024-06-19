package com.example.groceryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        holder.orderDateTextView.setText("Date: " + order.getOrderDate());
        holder.totalAmountTextView.setText("Total: $" + order.getTotalAmount());

        // Set up the products RecyclerView
        OrderProductAdapter productAdapter = new OrderProductAdapter(context, order.getProductList());
        holder.recyclerViewOrderProducts.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewOrderProducts.setAdapter(productAdapter);

        // Set up the delete button
        holder.btnDeleteOrder.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(userId).child(order.getOrderId());
            ordersRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    orderList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, orderList.size());
                    Toast.makeText(context, "Pedido eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar el pedido", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, orderDateTextView, totalAmountTextView;
        RecyclerView recyclerViewOrderProducts;
        Button btnDeleteOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
            recyclerViewOrderProducts = itemView.findViewById(R.id.recyclerViewOrderProducts);
            btnDeleteOrder = itemView.findViewById(R.id.btnDeleteOrder);
        }
    }
}
