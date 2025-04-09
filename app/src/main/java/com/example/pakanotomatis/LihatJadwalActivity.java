package com.example.pakanotomatis;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LihatJadwalActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    JadwalAdapter adapter;
    DatabaseHelper db;
    ArrayList<Jadwal> daftarJadwal;
    ImageView btnKembali;
    TextView tvKosong; // Tambahkan variabel untuk TextView jadwal kosong

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_jadwal);

        // Inisialisasi View
        recyclerView = findViewById(R.id.recyclerViewJadwal);
        btnKembali = findViewById(R.id.btnKembali);
        tvKosong = findViewById(R.id.tvKosong); // Inisialisasi TextView

        // Inisialisasi database dan data
        db = new DatabaseHelper(this);
        daftarJadwal = db.getAllJadwal();

        // Tampilkan/hidden berdasarkan data
        if (daftarJadwal.isEmpty()) {
            tvKosong.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvKosong.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        // Atur RecyclerView
        adapter = new JadwalAdapter(this, daftarJadwal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fungsi klik tombol panah kembali
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // kembali ke activity sebelumnya
            }
        });
    }

    // ============================================
    // Inner Class Adapter RecyclerView
    // ============================================
    public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.ViewHolder> {

        Context context;
        ArrayList<Jadwal> listJadwal;

        public JadwalAdapter(Context context, ArrayList<Jadwal> listJadwal) {
            this.context = context;
            this.listJadwal = listJadwal;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_jadwal, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Jadwal jadwal = listJadwal.get(position);
            holder.txtJudul.setText(jadwal.getJudul());
            holder.txtJam.setText(jadwal.getJam());

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin menghapus jadwal ini?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            db.deleteJadwal(jadwal.getId());
                            listJadwal.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Jadwal dihapus", Toast.LENGTH_SHORT).show();

                            // Perbarui tampilan jika kosong
                            if (listJadwal.isEmpty()) {
                                tvKosong.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                        .show();
            });

            holder.switchAktif.setChecked(true); // Default aktif
        }

        @Override
        public int getItemCount() {
            return listJadwal.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtJudul, txtJam;
            ImageView btnDelete;
            Switch switchAktif;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtJudul = itemView.findViewById(R.id.txtJudul);
                txtJam = itemView.findViewById(R.id.txtJam);
                btnDelete = itemView.findViewById(R.id.btnDelete);
                switchAktif = itemView.findViewById(R.id.switchAktif);
            }
        }
    }
}