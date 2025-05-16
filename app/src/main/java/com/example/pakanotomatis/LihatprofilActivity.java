package com.example.pakanotomatis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class LihatprofilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private static final String FILENAME = "profile_image.png";

    private ImageView imgProfile, btnKembali;
    private TextView tvNama, tvEmail;
    private Button btnKeluar;
    private LinearLayout navBeranda, navAturJadwal, navLihatJadwal, navProfil, btnEditProfil, btnEditPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_profil);

        mAuth = FirebaseAuth.getInstance();

        imgProfile = findViewById(R.id.imgProfile);
        btnKembali = findViewById(R.id.btnKembali);
        tvEmail = findViewById(R.id.tvEmail);
        btnKeluar = findViewById(R.id.btnKeluar);

        navBeranda = findViewById(R.id.navBeranda);
        navAturJadwal = findViewById(R.id.navAturJadwal);
        navLihatJadwal = findViewById(R.id.navLihatJadwal);
        navProfil = findViewById(R.id.navProfil);

        btnEditProfil = findViewById(R.id.btnEditProfil);
        btnEditPassword = findViewById(R.id.btnEditPassword);

        // Ambil data user dari FirebaseAuth
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String uid = user.getUid();

            tvEmail.setText(email);
            // Opsional: Ambil nama dari Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance("https://pakan-otomatis-f1dd8-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("pengguna").child(uid);
        } else {
            Toast.makeText(this, "Tidak ada user yang login", Toast.LENGTH_SHORT).show();
            tvEmail.setText("-");
            tvNama.setText("-");
        }

        // Load gambar profil lokal jika ada
        loadProfileImage();

        // Klik gambar untuk memilih gambar baru
        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        btnKembali.setOnClickListener(v -> finish());

        btnKeluar.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Logout")
                    .setMessage("Apakah Anda yakin ingin keluar?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        mAuth.signOut();
                        Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, ActivityLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });


        btnEditProfil.setOnClickListener(v -> {
            Intent editProfilIntent = new Intent(LihatprofilActivity.this, EditProfilActivity.class);
            startActivity(editProfilIntent);
        });

        btnEditPassword.setOnClickListener(v -> {
            Intent editPasswordIntent = new Intent(LihatprofilActivity.this, EditKataSandiActivity.class);
            startActivity(editPasswordIntent);
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                imgProfile.setImageBitmap(selectedImage);
                saveProfileImage(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProfileImage(Bitmap bitmap) {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProfileImage() {
        try {
            File file = new File(getFilesDir(), FILENAME);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imgProfile.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}