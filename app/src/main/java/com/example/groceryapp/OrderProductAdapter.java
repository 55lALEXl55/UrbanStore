package com.example.groceryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public OrderProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList != null ? productList : new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_order, parent, false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product != null) {
            holder.titleTextView.setText(product.getTitle());
            holder.priceTextView.setText("Precio: $" + product.getPrice());
            holder.quantityTextView.setText("Cantidad: " + product.getQuantity());
            Glide.with(context).load(product.getImage()).into(holder.productImageView);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class OrderProductViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, priceTextView, quantityTextView;
        ImageView productImageView;

        public OrderProductViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
        }
    }
}
