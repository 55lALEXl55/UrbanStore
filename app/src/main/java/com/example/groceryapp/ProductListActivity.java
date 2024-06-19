package com.example.groceryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<Product> productList;
    private ArrayList<Product> filteredList;
    private EditText searchBar;
    private Spinner categorySpinner;
    private Button viewCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchBar = findViewById(R.id.searchBar);
        categorySpinner = findViewById(R.id.categorySpinner);
        viewCartButton = findViewById(R.id.viewCartButton);

        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ProductAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        setupCategorySpinner();
        fetchProducts();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        viewCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void setupCategorySpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        categories.add("electronics");
        categories.add("jewelery");
        categories.add("men's clothing");
        categories.add("women's clothing");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
    }

    private void fetchProducts() {
        ProductApiService apiService = RetrofitClient.getClient("https://fakestoreapi.com/").create(ProductApiService.class);
        Call<List<Product>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    filterProducts();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void filterProducts() {
        String query = searchBar.getText().toString().toLowerCase();
        String selectedCategory = categorySpinner.getSelectedItem().toString().toLowerCase();

        filteredList.clear();
        for (Product product : productList) {
            boolean matchesCategory = selectedCategory.equals("all") || product.getCategory().toLowerCase().equals(selectedCategory);
            boolean matchesQuery = product.getTitle().toLowerCase().contains(query);
            if (matchesCategory && matchesQuery) {
                filteredList.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
