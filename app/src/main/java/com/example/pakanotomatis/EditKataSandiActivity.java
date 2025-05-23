package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;

public class EditKataSandiActivity extends AppCompatActivity {

    EditText etKataSandiLama, etKataSandiBaru, etKonfirmasiKataSandi;
    Button btnSimpan;
    ImageView ivToggleKataSandiLama, ivToggleKataSandiBaru, ivToggleKonfirmasiKataSandi, btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kata_sandi);

        // Inisialisasi view
        etKataSandiLama = findViewById(R.id.etKataSandiLama);
        etKataSandiBaru = findViewById(R.id.etKataSandiBaru);
        etKonfirmasiKataSandi = findViewById(R.id.etKonfirmasiKataSandi);

        ivToggleKataSandiLama = findViewById(R.id.iv_toggle_kata_sandi_lama);
        ivToggleKataSandiBaru = findViewById(R.id.iv_toggle_kata_sandi_baru);
        ivToggleKonfirmasiKataSandi = findViewById(R.id.iv_toggle_konfirmasi_kata_sandi);

        btnKembali = findViewById(R.id.btnKembali);
        btnSimpan = findViewById(R.id.btnSimpan);

        // Tombol kembali
        btnKembali.setOnClickListener(v -> finish());

        // Tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String sandiLama = etKataSandiLama.getText().toString().trim();
            String sandiBaru = etKataSandiBaru.getText().toString().trim();
            String konfirmasiSandi = etKonfirmasiKataSandi.getText().toString().trim();

            if (sandiLama.isEmpty() || sandiBaru.isEmpty() || konfirmasiSandi.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            } else if (sandiBaru.length() < 6) {
                Toast.makeText(this, "Kata sandi harus minimal 6 karakter", Toast.LENGTH_SHORT).show();
            } else if (!sandiBaru.equals(konfirmasiSandi)) {
                Toast.makeText(this, "Kata sandi baru tidak cocok", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.getEmail() != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), sandiLama);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    user.updatePassword(sandiBaru)
                                            .addOnCompleteListener(updateTask -> {
                                                if (updateTask.isSuccessful()) {
                                                    Toast.makeText(this, "Kata sandi berhasil diubah. Silakan login kembali.", Toast.LENGTH_LONG).show();

                                                    // Logout & arahkan ke login
                                                    FirebaseAuth.getInstance().signOut();
                                                    Intent intent = new Intent(EditKataSandiActivity.this, ActivityLogin.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    Toast.makeText(this, "Gagal mengubah kata sandi. Coba lagi.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(this, "Kata sandi lama salah", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Toggle visibility untuk setiap field
        ivToggleKataSandiLama.setOnClickListener(v -> togglePasswordVisibility(etKataSandiLama, ivToggleKataSandiLama));
        ivToggleKataSandiBaru.setOnClickListener(v -> togglePasswordVisibility(etKataSandiBaru, ivToggleKataSandiBaru));
        ivToggleKonfirmasiKataSandi.setOnClickListener(v -> togglePasswordVisibility(etKonfirmasiKataSandi, ivToggleKonfirmasiKataSandi));
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleIcon) {
        int inputType = editText.getInputType();
        if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_eye_closed); // ikon mata tertutup
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_eye); // ikon mata terbuka
        }
        editText.setSelection(editText.getText().length());
    }
}
