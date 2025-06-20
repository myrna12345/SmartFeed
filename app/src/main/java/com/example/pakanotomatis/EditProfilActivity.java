package com.example.pakanotomatis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;
    private static final String PROFILE_IMAGE_NAME = "profile_image.png";

    private ImageView profileImage;
    private TextView textUbahFoto;
    private EditText editName, editEmail, editPhone;
    private Button btnSave;
    private LinearLayout backHeader;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        initViews();
        setupListeners();
        loadUserData();
    }

    private void initViews() {
        profileImage = findViewById(R.id.profile_image);
        textUbahFoto = findViewById(R.id.text_ubah_foto);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        btnSave = findViewById(R.id.btn_save);
        backHeader = findViewById(R.id.back_header);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    private void setupListeners() {
        backHeader.setOnClickListener(v -> finish());
        textUbahFoto.setOnClickListener(v -> openImageChooser());
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                saveProfileImageLocally(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal menyimpan foto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfileImageLocally(Bitmap bitmap) {
        try {
            FileOutputStream fos = openFileOutput(PROFILE_IMAGE_NAME, MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Toast.makeText(this, "Foto berhasil disimpan", Toast.LENGTH_SHORT).show();
            loadProfileImage();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileImage() {
        File file = new File(getFilesDir(), PROFILE_IMAGE_NAME);
        Glide.with(this)
                .load(file.exists() ? file : R.drawable.ic_user_default)
                .transform(new CircleCrop())
                .skipMemoryCache(true)
                .into(profileImage);
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String uid = user.getUid();

            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    editName.setText(documentSnapshot.getString("nama"));
                    editEmail.setText(documentSnapshot.getString("email"));
                    editPhone.setText(documentSnapshot.getString("telepon"));
                }

                loadProfileImage();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            });
        }
    }

    private void saveProfile() {
        String nama = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String telepon = editPhone.getText().toString().trim();

        if (nama.isEmpty()) {
            editName.setError("Nama tidak boleh kosong");
            editName.requestFocus();
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Email tidak valid");
            editEmail.requestFocus();
            return;
        }

        if (telepon.isEmpty()) {
            editPhone.setError("Telepon tidak boleh kosong");
            editPhone.requestFocus();
            return;
        }

        progressDialog.setMessage("Menyimpan data...");
        progressDialog.show();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("nama", nama);
            userMap.put("email", email);
            userMap.put("telepon", telepon);

            db.collection("users").document(uid).set(userMap)
                    .addOnSuccessListener(aVoid -> {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nama)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task -> {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileImage(); // ⬅️ Refresh gambar setiap kembali ke halaman
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
        new Thread(() -> Glide.get(this).clearDiskCache()).start(); // Optional, untuk bersihkan cache
    }
}
