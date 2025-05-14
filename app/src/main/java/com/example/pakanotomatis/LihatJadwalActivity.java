package com.example.pakanotomatis;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LihatJadwalActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    JadwalAdapter adapter;
    ArrayList<AturJadwalActivity.Jadwal> daftarJadwal;
    ImageView btnKembali;
    TextView tvKosong;

    // Firebase Database Reference
    private DatabaseReference dbRef;

    // 👇 Tambahan: komponen navigasi bawah
    LinearLayout navBeranda, navAturJadwal, navLihatJadwal, navProfil;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_jadwal);

        // Inisialisasi Firebase
        dbRef = FirebaseDatabase.getInstance(
                "https://pakan-otomatis-f1dd8-default-rtdb.asia-southeast1.firebasedatabase.app/"
        ).getReference("jadwal_pemberian_pakan");

        // Inisialisasi View
        recyclerView = findViewById(R.id.recyclerViewJadwal);
        btnKembali = findViewById(R.id.btnKembali);
        tvKosong = findViewById(R.id.tvKosong);

        // 🔧 Inisialisasi Bottom Navigation
        navBeranda = findViewById(R.id.navBeranda);
        navAturJadwal = findViewById(R.id.navAturJadwal);
        navLihatJadwal = findViewById(R.id.navLihatJadwal);
        navProfil = findViewById(R.id.navProfil);

        // 🔄 Set klik navigasi
        navBeranda.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        navAturJadwal.setOnClickListener(v -> {
            startActivity(new Intent(this, AturJadwalActivity.class));
            finish();
        });

        navLihatJadwal.setOnClickListener(v -> {
            // Sudah di halaman ini, jadi tidak perlu pindah
        });

        navProfil.setOnClickListener(v -> {
            startActivity(new Intent(this, LihatprofilActivity.class));
            finish();
        });

        // Inisialisasi data
        daftarJadwal = new ArrayList<>();

        // Atur RecyclerView
        adapter = new JadwalAdapter(this, daftarJadwal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load data dari Firebase
        loadDataFromFirebase();

        // Tombol kembali
        btnKembali.setOnClickListener(v -> finish());
    }

    private void loadDataFromFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                daftarJadwal.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    AturJadwalActivity.Jadwal jadwal = data.getValue(AturJadwalActivity.Jadwal.class);
                    if (jadwal != null) {
                        jadwal.id_jadwal = data.getKey(); // Set ID dari Firebase
                        daftarJadwal.add(jadwal);
                    }
                }
                adapter.notifyDataSetChanged();
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LihatJadwalActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FIREBASE_ERROR", error.getMessage());
            }
        });
    }

    private void updateUI() {
        if (daftarJadwal.isEmpty()) {
            tvKosong.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvKosong.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    // ============================================
    // Inner Class Adapter RecyclerView
    // ============================================
    public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.ViewHolder> {

        Context context;
        ArrayList<AturJadwalActivity.Jadwal> listJadwal;

        public JadwalAdapter(Context context, ArrayList<AturJadwalActivity.Jadwal> listJadwal) {
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
            AturJadwalActivity.Jadwal jadwal = listJadwal.get(position);
            holder.txtJudul.setText(jadwal.judul);
            holder.txtJam.setText(jadwal.waktu_pakan);

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin menghapus jadwal ini?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            // Hapus dari Firebase
                            dbRef.child(jadwal.id_jadwal).removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Jadwal dihapus", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Gagal menghapus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                        .show();
            });

            holder.switchAktif.setChecked(jadwal.aktif);
            holder.tvStatusAktif.setText(jadwal.aktif ? "On" : "Off");

            // Update warna berdasarkan status
            updateItemAppearance(holder, jadwal.aktif);

            holder.switchAktif.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Update status di Firebase
                dbRef.child(jadwal.id_jadwal).child("aktif").setValue(isChecked)
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Gagal update status", Toast.LENGTH_SHORT).show();
                        });

                // Update tampilan lokal
                jadwal.aktif = isChecked;
                updateItemAppearance(holder, isChecked);
            });
        }

        private void updateItemAppearance(ViewHolder holder, boolean isActive) {
            if (isActive) {
                // Mode ON - warna normal
                holder.txtJudul.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                holder.txtJam.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                holder.tvStatusAktif.setText("On");
                holder.tvStatusAktif.setTextColor(ContextCompat.getColor(context, R.color.blue));
                holder.itemView.setBackgroundResource(R.drawable.bg_card_rounded);
            } else {
                // Mode OFF - warna redup
                holder.txtJudul.setTextColor(ContextCompat.getColor(context, R.color.text_disabled));
                holder.txtJam.setTextColor(ContextCompat.getColor(context, R.color.text_disabled));
                holder.tvStatusAktif.setText("Off");
                holder.tvStatusAktif.setTextColor(ContextCompat.getColor(context, R.color.text_disabled));
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_disabled));
            }
        }

        @Override
        public int getItemCount() {
            return listJadwal.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtJudul, txtJam, tvStatusAktif;
            ImageView btnDelete;
            Switch switchAktif;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtJudul = itemView.findViewById(R.id.txtJudul);
                txtJam = itemView.findViewById(R.id.txtJam);
                btnDelete = itemView.findViewById(R.id.btnDelete);
                switchAktif = itemView.findViewById(R.id.switchAktif);
                tvStatusAktif = itemView.findViewById(R.id.tvStatusAktif);
            }
        }
    }
}