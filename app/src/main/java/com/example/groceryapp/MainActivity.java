package com.example.groceryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister, btnShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnShop = findViewById(R.id.btnShop);

        // Listener para el botón de inicio de sesión
        btnLogin.setOnClickListener(view -> {
            // Intent para navegar a LoginActivity
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        // Listener para el botón de registro
        btnRegister.setOnClickListener(view -> {
            // Intent para navegar a RegisterActivity
            Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        });

        // Listener para el botón de comprar
        btnShop.setOnClickListener(view -> {
            // Intent para navegar a ProductListActivity
            Intent shopIntent = new Intent(MainActivity.this, ProductListActivity.class);
            startActivity(shopIntent);
        });
    }
}
