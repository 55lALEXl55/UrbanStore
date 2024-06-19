package com.example.groceryapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private ArrayList<Product> cartList;
    private TextView tvTotal;

    public CartAdapter(Context context, ArrayList<Product> cartList, TextView tvTotal) {
        this.context = context;
        this.cartList = CartManager.getInstance().getCartItems(); // Sincronizando con CartManager
        this.tvTotal = tvTotal;
        updateTotal();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartList.get(position);

        holder.productName.setText(product.getTitle());
        holder.productPrice.setText(String.format("$%.2f", product.getPrice()));
        holder.etQuantity.setText(String.valueOf(product.getQuantity()));
        Glide.with(context).load(product.getImage()).into(holder.productImage);

        holder.btnIncrease.setOnClickListener(v -> {
            int currentQuantity = product.getQuantity();
            product.setQuantity(currentQuantity + 1);
            holder.etQuantity.setText(String.valueOf(product.getQuantity()));
            updateTotal();
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int currentQuantity = product.getQuantity();
            if (currentQuantity > 1) {
                product.setQuantity(currentQuantity - 1);
                holder.etQuantity.setText(String.valueOf(product.getQuantity()));
                updateTotal();
            }
        });

        holder.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int quantity = Integer.parseInt(s.toString());
                    if (quantity < 1) {
                        quantity = 1;
                        holder.etQuantity.setText(String.valueOf(quantity));
                    }
                    product.setQuantity(quantity);
                    updateTotal();
                } catch (NumberFormatException e) {
                    // En caso de que el campo esté vacío o no sea un número válido
                }
            }
        });

        holder.removeFromCartButton.setOnClickListener(v -> {
            int position1 = holder.getAdapterPosition();
            if (position1 != RecyclerView.NO_POSITION) {
                // Eliminar producto de CartManager
                Product productToRemove = cartList.get(position1);
                CartManager.getInstance().removeProductFromCart(productToRemove);

                // Eliminar producto de cartList
                cartList.remove(position1);
                notifyItemRemoved(position1);
                notifyItemRangeChanged(position1, cartList.size());

                // Actualizar la lista sincronizada de CartManager
                cartList = CartManager.getInstance().getCartItems();
                updateTotal();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    public void updateCartList(ArrayList<Product> newCartList) {
        this.cartList = newCartList;
        notifyDataSetChanged();
        updateTotal();
    }

    private void updateTotal() {
        int totalItems = 0;
        double totalPrice = 0.0;

        for (Product product : cartList) {
            totalItems += product.getQuantity();
            totalPrice += product.getQuantity() * product.getPrice();
        }

        String totalText = String.format("Total: %d productos, $%.2f", totalItems, totalPrice);
        tvTotal.setText(totalText);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        EditText etQuantity;
        ImageView productImage;
        Button removeFromCartButton, btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            etQuantity = itemView.findViewById(R.id.etQuantity);
            removeFromCartButton = itemView.findViewById(R.id.removeFromCartButton);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }
}
