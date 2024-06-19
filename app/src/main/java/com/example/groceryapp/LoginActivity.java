package com.example.groceryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Vincular vistas
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Iniciar sesión con Firebase Auth
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Bienvenido, " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, CartActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Si el inicio de sesión falla, mostrar un mensaje al usuario.
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                        Toast.makeText(LoginActivity.this, "Error de autenticación: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
