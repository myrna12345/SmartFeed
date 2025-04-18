package com.example.pakanotomatis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class LupaKataSandiActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetKataSandi;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_kata_sandi);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi view
        etEmail = findViewById(R.id.et_email_reset);
        btnResetKataSandi = findViewById(R.id.btn_reset_kata_sandi);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Reset Kata Sandi");
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Aksi reset kata sandi
        btnResetKataSandi.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            } else {
                // Kirim email reset melalui Firebase
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Link reset telah dikirim ke email kamu", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, ActivityLogin.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Gagal mengirim email reset. Coba lagi.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
