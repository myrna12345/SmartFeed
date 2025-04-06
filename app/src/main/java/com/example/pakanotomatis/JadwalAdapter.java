package com.example.pakanotomatis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pakanotomatis.DatabaseHelper;
import com.example.pakanotomatis.Jadwal;
import com.example.pakanotomatis.R;

import java.util.ArrayList;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.ViewHolder> {

    private ArrayList<Jadwal> list;
    private Context context;
    private OnDeleteListener onDeleteListener;

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public JadwalAdapter(ArrayList<Jadwal> list, Context context, OnDeleteListener onDeleteListener) {
        this.list = list;
        this.context = context;
        this.onDeleteListener = onDeleteListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtJudul, txtJam;
        Switch switchAktif;
        ImageButton btnHapus;

        public ViewHolder(View itemView) {
            super(itemView);
            txtJudul = itemView.findViewById(R.id.txtJudul);
            txtJam = itemView.findViewById(R.id.txtJam);
            switchAktif = itemView.findViewById(R.id.switchAktif);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
    }

    @NonNull
    @Override
    public JadwalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jadwal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalAdapter.ViewHolder holder, int position) {
        Jadwal j = list.get(position);
        holder.txtJudul.setText(j.getJudul());
        holder.txtJam.setText(j.getJam());
        holder.switchAktif.setChecked(j.isAktif());

        holder.switchAktif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update status aktif/nonaktif di database
            new DatabaseHelper(context).updateAktif(j.getId(), isChecked);
        });

        holder.btnHapus.setOnClickListener(v -> {
            onDeleteListener.onDelete(position); // Menggunakan listener untuk menghapus
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
