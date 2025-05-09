package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AturJadwalActivity extends AppCompatActivity {

    EditText edtJudul;
    TimePicker timePicker;
    ImageView btnKembali;

    LinearLayout navBeranda, navAturJadwal, navLihatJadwal, navProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_jadwal);

        // Inisialisasi komponen
        edtJudul = findViewById(R.id.edtJudul);
        timePicker = findViewById(R.id.timePicker);
        Button btnSimpan = findViewById(R.id.btnSimpan);
        btnKembali = findViewById(R.id.btnKembali);

        // Inisialisasi navigasi bawah
        navBeranda = findViewById(R.id.navBeranda);
        navAturJadwal = findViewById(R.id.navAturJadwal);
        navLihatJadwal = findViewById(R.id.navLihatJadwal);
        navProfil = findViewById(R.id.navProfil);

        // Tombol simpan jadwal
        btnSimpan.setOnClickListener(v -> {
            String judul = edtJudul.getText().toString().trim();
            int jam = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? timePicker.getHour() : timePicker.getCurrentHour();
            int menit = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? timePicker.getMinute() : timePicker.getCurrentMinute();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, jam);
            calendar.set(Calendar.MINUTE, menit);

            // Set zona waktu Makassar
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Makassar");
            calendar.setTimeZone(timeZone);

            // Format waktu
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(timeZone);
            String waktu = sdf.format(calendar.getTime());

            if (!judul.isEmpty()) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance(
                        "https://pakan-otomatis-f1dd8-default-rtdb.asia-southeast1.firebasedatabase.app/"
                ).getReference("jadwal_pemberian_pakan");

                String id = dbRef.push().getKey(); // Generate ID unik

                // Set aktif = true saat membuat objek Jadwal
                Jadwal jadwal = new Jadwal(id, judul, waktu, true);

                if (id != null) {
                    dbRef.child(id).setValue(jadwal)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Jadwal berhasil disimpan", Toast.LENGTH_SHORT).show();
                                finish(); // kembali
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Gagal menyimpan jadwal", Toast.LENGTH_SHORT).show();
                                Log.e("FIREBASE_ERROR", e.getMessage(), e);
                            });
                }
            } else {
                Toast.makeText(this, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol kembali
        btnKembali.setOnClickListener(v -> finish());

        // Navigasi bawah
        navBeranda.setOnClickListener(v -> startActivity(new Intent(this, DashboardActivity.class)));
        navAturJadwal.setOnClickListener(v -> {});
        navLihatJadwal.setOnClickListener(v -> startActivity(new Intent(this, LihatJadwalActivity.class)));
        navProfil.setOnClickListener(v -> startActivity(new Intent(this, LihatprofilActivity.class)));
    }

    // Model data jadwal dengan properti aktif
    public static class Jadwal {
        public String id_jadwal;
        public String judul;
        public String waktu_pakan;
        public boolean aktif;

        public Jadwal() {
            // Diperlukan untuk Firebase
        }

        public Jadwal(String id_jadwal, String judul, String waktu_pakan, boolean aktif) {
            this.id_jadwal = id_jadwal;
            this.judul = judul;
            this.waktu_pakan = waktu_pakan;
            this.aktif = aktif;
        }
    }
}
