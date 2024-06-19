package com.example.groceryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private TextView tvEmptyCart, tvTotal;
    private CartAdapter cartAdapter;
    private ArrayList<Product> cartList;
    private Button btnViewOrderHistory, btnCompletePurchase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnViewOrderHistory = findViewById(R.id.btnViewOrderHistory);
        btnCompletePurchase = findViewById(R.id.btnCompletePurchase);

        cartList = CartManager.getInstance().getCartItems();

        if (cartList.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);
            recyclerViewCart.setVisibility(View.GONE);
        } else {
            tvEmptyCart.setVisibility(View.GONE);
            recyclerViewCart.setVisibility(View.VISIBLE);
            setUpRecyclerView();
            updateTotalAmount();
        }

        btnViewOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        btnCompletePurchase.setOnClickListener(v -> completePurchase());

        FloatingActionButton fabAddProduct = findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, ProductListActivity.class);
            startActivity(intent);
        });
    }

    private void setUpRecyclerView() {
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, cartList, tvTotal);
        recyclerViewCart.setAdapter(cartAdapter);
    }

    private void updateTotalAmount() {
        double totalAmount = calculateTotalAmount();
        int totalItems = calculateTotalItems();
        tvTotal.setText(String.format(Locale.getDefault(), "Total: %d productos, $%.2f", totalItems, totalAmount));
    }

    private double calculateTotalAmount() {
        double total = 0;
        for (Product product : cartList) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }

    private int calculateTotalItems() {
        int totalItems = 0;
        for (Product product : cartList) {
            totalItems += product.getQuantity();
        }
        return totalItems;
    }

    private void completePurchase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders").child(userId);

        String orderId = ordersRef.push().getKey();
        double totalAmount = calculateTotalAmount();
        String orderDate = getCurrentDate();

        Order order = new Order(orderId, new ArrayList<>(cartList), totalAmount, orderDate);

        if (orderId != null) {
            ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Compra completada con éxito", Toast.LENGTH_SHORT).show();
                    CartManager.getInstance().clearCart();
                    finish(); // Cierra la actividad actual y regresa a la anterior
                } else {
                    Toast.makeText(CartActivity.this, "Error al guardar el pedido", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartList = CartManager.getInstance().getCartItems();
        if (cartList.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);
            recyclerViewCart.setVisibility(View.GONE);
        } else {
            tvEmptyCart.setVisibility(View.GONE);
            recyclerViewCart.setVisibility(View.VISIBLE);
            cartAdapter.updateCartList(cartList);
            updateTotalAmount();
        }
    }
}
