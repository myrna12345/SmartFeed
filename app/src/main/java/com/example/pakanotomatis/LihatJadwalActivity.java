package com.example.pakanotomatis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

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
    ArrayList<Jadwal> daftarJadwal = new ArrayList<>();
    ImageView btnKembali;
    TextView tvKosong;

    LinearLayout navBeranda, navAturJadwal, navLihatJadwal, navProfil;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_jadwal);

        recyclerView = findViewById(R.id.recyclerViewJadwal);
        btnKembali = findViewById(R.id.btnKembali);
        tvKosong = findViewById(R.id.tvKosong);

        navBeranda = findViewById(R.id.navBeranda);
        navAturJadwal = findViewById(R.id.navAturJadwal);
        navLihatJadwal = findViewById(R.id.navLihatJadwal);
        navProfil = findViewById(R.id.navProfil);

        dbRef = FirebaseDatabase.getInstance(
                "https://pakan-otomatis-f1dd8-default-rtdb.asia-southeast1.firebasedatabase.app/"
        ).getReference("jadwal_pemberian_pakan");

        adapter = new JadwalAdapter(this, daftarJadwal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ambilDataFirebase();

        btnKembali.setOnClickListener(v -> finish());

        navBeranda.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        navAturJadwal.setOnClickListener(v -> {
            startActivity(new Intent(this, AturJadwalActivity.class));
            finish();
        });

        navLihatJadwal.setOnClickListener(v -> {
            // Halaman saat ini
        });

        navProfil.setOnClickListener(v -> {
            startActivity(new Intent(this, LihatprofilActivity.class));
            finish();
        });
    }

    private void ambilDataFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                daftarJadwal.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Jadwal jadwal = data.getValue(Jadwal.class);
                    if (jadwal != null) {
                        jadwal.id_jadwal = data.getKey(); // penting agar bisa dihapus dan diupdate
                        daftarJadwal.add(jadwal);
                    }
                }

                adapter.notifyDataSetChanged();

                if (daftarJadwal.isEmpty()) {
                    tvKosong.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvKosong.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LihatJadwalActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

            holder.txtJudul.setText(jadwal.judul);
            holder.txtJam.setText(jadwal.waktu_pakan);

            // Reset listener sebelum set ulang
            holder.switchAktif.setOnCheckedChangeListener(null);
            holder.switchAktif.setChecked(Boolean.TRUE.equals(jadwal.aktif)); // aman terhadap null

            holder.switchAktif.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (jadwal.id_jadwal != null) {
                    dbRef.child(jadwal.id_jadwal).child("aktif").setValue(isChecked)
                            .addOnSuccessListener(unused -> Toast.makeText(context, "Status diperbarui", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Gagal memperbarui status", Toast.LENGTH_SHORT).show());
                }
            });

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Konfirmasi")
                        .setMessage("Yakin ingin menghapus jadwal ini?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            if (jadwal.id_jadwal != null) {
                                dbRef.child(jadwal.id_jadwal).removeValue()
                                        .addOnSuccessListener(unused -> Toast.makeText(context, "Jadwal dihapus", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(context, "Gagal menghapus jadwal", Toast.LENGTH_SHORT).show());
                            }
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                        .show();
            });
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

    public static class Jadwal {
        public String id_jadwal;
        public String judul;
        public String waktu_pakan;
        public Boolean aktif; // gunakan Boolean (wrapper) agar bisa null-safe

        public Jadwal() {}

        public Jadwal(String id_jadwal, String judul, String waktu_pakan) {
            this.id_jadwal = id_jadwal;
            this.judul = judul;
            this.waktu_pakan = waktu_pakan;
            this.aktif = true;
        }
    }
}
