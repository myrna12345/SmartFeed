package com.example.pakanotomatis;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfilActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView textUbahFoto;
    private EditText editName, editEmail, editPhone;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil); // pastikan nama XML-nya sesuai

        profileImage = findViewById(R.id.profile_image);
        textUbahFoto = findViewById(R.id.text_ubah_foto);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        btnSave = findViewById(R.id.btn_save);

        textUbahFoto.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur ubah foto belum tersedia", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = editName.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String telepon = editPhone.getText().toString().trim();

                if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty()) {
                    Toast.makeText(EditProfilActivity.this, "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("EditProfile", "Nama: " + nama + ", Email: " + email + ", Telepon: " + telepon);
                    Toast.makeText(EditProfilActivity.this, "Profil berhasil disimpan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
