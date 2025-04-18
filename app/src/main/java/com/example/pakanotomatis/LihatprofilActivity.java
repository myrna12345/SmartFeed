package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LihatprofilActivity extends AppCompatActivity {

    private ImageView imgProfile, btnKembali;
    private TextView tvNama, tvEmail;
    private Button btnKeluar;
    private LinearLayout navBeranda, navAturJadwal, navLihatJadwal, navProfil, btnEditProfil, btnEditPassword; // Tambahkan btnEditPassword

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

        btnEditProfil = findViewById(R.id.btnEditProfil); // Inisialisasi tombol Edit Profil
        btnEditPassword = findViewById(R.id.btnEditPassword); // Inisialisasi tombol Edit Kata Sandi

        // Simulasi nama dan email user (kalau kamu punya data asli, tinggal load dari SharedPreferences atau DB)
        tvNama.setText("Azmi");
        tvEmail.setText("azmi@gmail.com");

        // Ganti foto profil
        imgProfile.setOnClickListener(v -> {
            // Logic untuk memilih gambar profil
            // Misalnya, Anda bisa menggunakan image picker seperti sebelumnya
        });

        // Tombol kembali
        btnKembali.setOnClickListener(v -> finish());

        btnKeluar.setOnClickListener(v -> {
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ActivityLogin.class); // Ganti dengan activity login Anda
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Menambahkan flag agar halaman login muncul di stack dan menghapus aktivitas sebelumnya
            startActivity(intent);
            finish(); // Menutup aktivitas saat ini
        });


        // Tombol Edit Profil untuk mengarah ke halaman Edit Profil
        btnEditProfil.setOnClickListener(v -> {
            // Ketika tombol Edit Profil ditekan, buka EditProfilActivity
            Intent editProfilIntent = new Intent(LihatprofilActivity.this, EditProfilActivity.class);
            startActivity(editProfilIntent);
        });

        // Tombol Edit Kata Sandi untuk mengarah ke halaman Edit Kata Sandi
        btnEditPassword.setOnClickListener(v -> {
            // Ketika tombol Edit Kata Sandi ditekan, buka EditPasswordActivity
            Intent editPasswordIntent = new Intent(LihatprofilActivity.this, EditKataSandiActivity.class);
            startActivity(editPasswordIntent);
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
