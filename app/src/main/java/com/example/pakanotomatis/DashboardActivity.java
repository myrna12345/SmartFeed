package com.example.pakanotomatis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private ProgressBar progressBarPakan;
    private TextView tvPersen, tvNamaPengguna, badgeNotifikasi;
    private Button btnNextSchedule;
    private ImageView iconNotifikasi, btnTutupNotifikasi;
    private RelativeLayout notifikasiPakan;
    private View dimBackground; // Tambahkan ini untuk background redup

    @SuppressLint("MissingInflatedId")
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
        notifikasiPakan = findViewById(R.id.notifikasiPakan);
        btnTutupNotifikasi = findViewById(R.id.btnTutupNotifikasi);
        dimBackground = findViewById(R.id.dimBackground); // Inisialisasi background redup

        // Placeholder awal jadwal
        btnNextSchedule.setText("Jadwal Pemberian Pakan Terdekat:");

        // Navigasi ke profil
        tvNamaPengguna.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, LihatprofilActivity.class);
            startActivity(intent);
        });

        // Listener untuk ikon notifikasi
        iconNotifikasi.setOnClickListener(v -> {
            if (badgeNotifikasi.getVisibility() == View.VISIBLE) {
                // Jika badge muncul, tampilkan notifikasi pakan dan background redup, sembunyikan badge
                notifikasiPakan.setVisibility(View.VISIBLE);
                dimBackground.setVisibility(View.VISIBLE);
                badgeNotifikasi.setVisibility(View.GONE);
            }
        });

        // Tutup notifikasi saat tombol tutup ditekan
        btnTutupNotifikasi.setOnClickListener(v -> {
            notifikasiPakan.setVisibility(View.GONE);
            dimBackground.setVisibility(View.GONE);
        });

        // Tutup notifikasi saat background redup ditekan
        dimBackground.setOnClickListener(v -> {
            notifikasiPakan.setVisibility(View.GONE);
            dimBackground.setVisibility(View.GONE);
        });

        // Ambil data Firebase
        ambilDataPakanDariFirebase();
        ambilJadwalAktifTerdekat();
        ambilNamaPengguna();

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
                // Bisa tampilkan log error jika perlu
            }
        });
    }

    private void updateProgressUI(int persen) {
        progressBarPakan.setProgress(persen);
        tvPersen.setText(persen + " %");

        if (persen < 30) {
            badgeNotifikasi.setVisibility(View.VISIBLE);
            badgeNotifikasi.setText("1");
            // Jangan tampilkan dimBackground atau notifikasi pakan di sini,
            // biarkan hanya muncul saat ikon notifikasi ditekan
            dimBackground.setVisibility(View.GONE);
            notifikasiPakan.setVisibility(View.GONE);
        } else {
            badgeNotifikasi.setVisibility(View.GONE);
            dimBackground.setVisibility(View.GONE);
            notifikasiPakan.setVisibility(View.GONE);
        }
    }

    private void ambilJadwalAktifTerdekat() {
        DatabaseReference dbRef = FirebaseDatabase
                .getInstance("https://pakan-otomatis-f1dd8-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("jadwal_pemberian_pakan");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> waktuAktifList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Boolean aktif = data.child("aktif").getValue(Boolean.class);
                    String waktu = data.child("waktu_pakan").getValue(String.class);

                    if (aktif != null && aktif && waktu != null) {
                        waktuAktifList.add(waktu);
                    }
                }

                if (waktuAktifList.isEmpty()) {
                    btnNextSchedule.setText("Belum ada jadwal aktif");
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Calendar sekarang = Calendar.getInstance();

                String jadwalTerdekat = null;
                long selisihTerdekat = Long.MAX_VALUE;

                for (String waktu : waktuAktifList) {
                    try {
                        Date waktuDate = sdf.parse(waktu);
                        if (waktuDate != null) {
                            Calendar jadwalCal = (Calendar) sekarang.clone();
                            Calendar waktuTarget = Calendar.getInstance();
                            waktuTarget.setTime(waktuDate);

                            jadwalCal.set(Calendar.HOUR_OF_DAY, waktuTarget.get(Calendar.HOUR_OF_DAY));
                            jadwalCal.set(Calendar.MINUTE, waktuTarget.get(Calendar.MINUTE));
                            jadwalCal.set(Calendar.SECOND, 0);
                            jadwalCal.set(Calendar.MILLISECOND, 0);

                            if (jadwalCal.before(sekarang)) {
                                jadwalCal.add(Calendar.DAY_OF_MONTH, 1);
                            }

                            long selisih = jadwalCal.getTimeInMillis() - sekarang.getTimeInMillis();

                            if (selisih < selisihTerdekat) {
                                selisihTerdekat = selisih;
                                jadwalTerdekat = waktu;
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (jadwalTerdekat != null) {
                    btnNextSchedule.setText("Jadwal Pemberian Pakan Terdekat:\n" + jadwalTerdekat);
                } else {
                    btnNextSchedule.setText("Belum Ada Jadwal Aktif");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                btnNextSchedule.setText("Gagal Mengambil Jadwal");
            }
        });
    }

    private void ambilNamaPengguna() {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance("https://pakan-otomatis-f1dd8-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users/user123/nama"); // Ganti 'user123' sesuai ID pengguna

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nama = snapshot.getValue(String.class);
                    tvNamaPengguna.setText(nama);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
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
