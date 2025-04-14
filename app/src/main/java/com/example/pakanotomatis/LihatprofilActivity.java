package com.example.pakanotomatis;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class LihatprofilActivity extends AppCompatActivity {

    private ImageView imgProfile, btnKembali;
    private TextView tvNama, tvEmail;
    private Button btnKeluar;
    private LinearLayout navBeranda, navAturJadwal, navLihatJadwal, navProfil;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            imgProfile.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_profil);

        // Inisialisasi View
        imgProfile = findViewById(R.id.imgProfile);
        btnKembali = findViewById(R.id.btnKembali);
        tvNama = findViewById(R.id.tvNama);
        tvEmail = findViewById(R.id.tvEmail);
        btnKeluar = findViewById(R.id.btnKeluar);

        navBeranda = findViewById(R.id.navBeranda);
        navAturJadwal = findViewById(R.id.navAturJadwal);
        navLihatJadwal = findViewById(R.id.navLihatJadwal);
        navProfil = findViewById(R.id.navProfil);

        // Simulasi nama dan email user (kalau kamu punya data asli, tinggal load dari SharedPreferences atau DB)
        tvNama.setText("Azmi");
        tvEmail.setText("azmi@gmail.com");

        // Ganti foto profil
        imgProfile.setOnClickListener(v -> {
            Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.setType("image/*");
            imagePickerLauncher.launch(pickImage);
        });

        // Tombol kembali
        btnKembali.setOnClickListener(v -> finish());

        // Tombol keluar
        btnKeluar.setOnClickListener(v -> {
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show();
            finish(); // Ganti dengan intent ke Login jika perlu
        });

        // Bottom Navigation
        navBeranda.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        navAturJadwal.setOnClickListener(v -> {
            startActivity(new Intent(this, AturJadwalActivity.class));
            finish();
        });

        navLihatJadwal.setOnClickListener(v -> {
            startActivity(new Intent(this, LihatJadwalActivity.class));
            finish();
        });

        navProfil.setOnClickListener(v -> {
            // Sudah di halaman ini
        });
    }
}
