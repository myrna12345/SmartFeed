package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    private ProgressBar progressBarPakan;
    private TextView tvPersen, tvNamaPengguna, badgeNotifikasi;
    private Button btnNextSchedule;
    private ImageView iconNotifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi komponen UI
        progressBarPakan = findViewById(R.id.progress_pakan);
        tvPersen = findViewById(R.id.tv_persen);
        tvNamaPengguna = findViewById(R.id.tvNamaPengguna);
        btnNextSchedule = findViewById(R.id.btn_next_schedule);
        iconNotifikasi = findViewById(R.id.iconNotifikasi);
        badgeNotifikasi = findViewById(R.id.badgeNotifikasi);

        // Jadwal dummy
        btnNextSchedule.setText("Jadwal yang akan datang:\n12:00");

        // Klik nama pengguna ke halaman profil
        tvNamaPengguna.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, LihatprofilActivity.class);
            startActivity(intent);
        });

        // Klik notifikasi untuk menyembunyikan badge
        iconNotifikasi.setOnClickListener(v -> badgeNotifikasi.setVisibility(View.GONE));

        // Ambil data pakan dari Firebase Realtime Database (region asia-southeast1)
        ambilDataPakanDariFirebase();

        // Bottom navigation
        setupBottomNav();
    }

    private void ambilDataPakanDariFirebase() {
        DatabaseReference sensorRef = FirebaseDatabase
                .getInstance("https://pakan-otomatis-f1dd8-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Sensor");

        sensorRef.child("pakan_persentase").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer persen = snapshot.getValue(Integer.class);
                    if (persen != null) {
                        updateProgressUI(persen);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log atau tampilkan pesan error jika diperlukan
            }
        });
    }

    private void updateProgressUI(int persen) {
        progressBarPakan.setProgress(persen);
        tvPersen.setText(persen + " %");

        if (persen < 30) {
            badgeNotifikasi.setVisibility(View.VISIBLE);
            badgeNotifikasi.setText("1");
        } else {
            badgeNotifikasi.setVisibility(View.GONE);
        }
    }

    private void setupBottomNav() {
        LinearLayout bottomNav = findViewById(R.id.bottomNavigationView);

        LinearLayout navAturJadwal = (LinearLayout) bottomNav.getChildAt(1);
        LinearLayout navLihatJadwal = (LinearLayout) bottomNav.getChildAt(2);
        LinearLayout navProfil = (LinearLayout) bottomNav.getChildAt(3);

        navAturJadwal.setOnClickListener(v ->
                startActivity(new Intent(this, AturJadwalActivity.class))
        );

        navLihatJadwal.setOnClickListener(v ->
                startActivity(new Intent(this, LihatJadwalActivity.class))
        );

        navProfil.setOnClickListener(v ->
                startActivity(new Intent(this, LihatprofilActivity.class))
        );
    }
}
