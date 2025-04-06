package com.example.pakanotomatis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LihatJadwalActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    JadwalAdapter adapter;
    DatabaseHelper db;
    ArrayList<Jadwal> daftarJadwal;
    Button btnKembaliLihatJadwal;  // Tombol Kembali

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_jadwal);

        // Inisialisasi komponen UI
        recyclerView = findViewById(R.id.recyclerViewJadwal);
        btnKembaliLihatJadwal = findViewById(R.id.btnKembaliLihatJadwal);  // Tombol Kembali

        db = new DatabaseHelper(this);

        // Ambil semua jadwal dari database
        daftarJadwal = db.getAllJadwal();
        adapter = new JadwalAdapter(daftarJadwal, this, new JadwalAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                // Konfirmasi penghapusan jadwal
                new AlertDialog.Builder(LihatJadwalActivity.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin menghapus jadwal ini?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            // Hapus jadwal dari database dan update RecyclerView
                            db.deleteJadwal(daftarJadwal.get(position).getId());
                            daftarJadwal.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(LihatJadwalActivity.this, "Jadwal dihapus", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });

        // Set RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Tombol Kembali - Kembali ke Activity sebelumnya
        btnKembaliLihatJadwal.setOnClickListener(v -> {
            onBackPressed();  // Fungsi untuk kembali ke activity sebelumnya
        });
    }
}
