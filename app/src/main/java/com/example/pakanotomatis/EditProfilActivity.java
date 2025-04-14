package com.example.pakanotomatis;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfilActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView textUbahFoto, editName, editEmail, editPhone;
    private Button btnSave;
    private LinearLayout backHeader; // Tambahkan ini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil); // Pastikan ini sesuai nama file XML

        profileImage = findViewById(R.id.profile_image);
        textUbahFoto = findViewById(R.id.text_ubah_foto);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        btnSave = findViewById(R.id.btn_save);
        backHeader = findViewById(R.id.back_header); // Inisialisasi back_header

        // Klik panah kembali
        backHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup activity
            }
        });

        // Klik ubah foto
        textUbahFoto.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur ubah foto belum tersedia", Toast.LENGTH_SHORT).show();
        });

        // Klik tombol simpan
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String telepon = editPhone.getText().toString().trim();

                if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty()) {
                    Toast.makeText(EditProfilActivity.this, "Data tidak lengkap", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("EditProfile", "Nama: " + nama + ", Email: " + email + ", Telepon: " + telepon);
                    Toast.makeText(EditProfilActivity.this, "Profil berhasil disimpan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
