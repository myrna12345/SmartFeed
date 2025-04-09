package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityLogin extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false;

    // Email & Password yang valid
    private final String validEmail = "azmi@gmail.com";
    private final String validPassword = "azmi12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi komponen UI
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);

        // Menangani aksi tombol login
        btnLogin.setOnClickListener(view -> validateLogin());

        // Menangani aksi lupa password
        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityLogin.this, LupaKataSandiActivity.class);
            startActivity(intent);
        });

        // Toggle visibilitas password
        ivTogglePassword.setOnClickListener(view -> togglePasswordVisibility());

        // Validasi input email dan password agar tombol aktif
        etEmail.addTextChangedListener(loginTextWatcher);
        etPassword.addTextChangedListener(loginTextWatcher);
    }

    private void validateLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email tidak boleh kosong");
            etEmail.requestFocus();
            return;
        }

        if (!email.contains("@")) {
            etEmail.setError("Email harus mengandung simbol '@'");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Kata sandi tidak boleh kosong");
            etPassword.requestFocus();
            return;
        }

        // Validasi terhadap email & password yang ditentukan
        if (email.equals(validEmail) && password.equals(validPassword)) {
            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ActivityLogin.this, DashboardActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Email atau Kata Sandi salah", Toast.LENGTH_SHORT).show();
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            ivTogglePassword.setImageResource(R.drawable.ic_eye);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            ivTogglePassword.setImageResource(R.drawable.ic_eye_closed);
        }
        etPassword.setSelection(etPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private final TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String emailInput = etEmail.getText().toString().trim();
            String passwordInput = etPassword.getText().toString().trim();
            btnLogin.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };
}