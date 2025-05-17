package com.example.pakanotomatis;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfilActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView textUbahFoto;
    private EditText editName, editEmail, editPhone;
    private Button btnSave;
    private LinearLayout backHeader;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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
    }

    private void setupListeners() {
        backHeader.setOnClickListener(v -> finish());

        textUbahFoto.setOnClickListener(v ->
                Toast.makeText(this, "Fitur ubah foto belum tersedia", Toast.LENGTH_SHORT).show());

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String uid = user.getUid();

            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nama = documentSnapshot.getString("nama");
                            String email = documentSnapshot.getString("email");
                            String telepon = documentSnapshot.getString("telepon");

                            if (nama != null) editName.setText(nama);
                            if (email != null) editEmail.setText(email);
                            if (telepon != null) editPhone.setText(telepon);
                        } else {
                            Toast.makeText(this, "Data tidak ditemukan di Firestore", Toast.LENGTH_SHORT).show();

                            // Fallback: gunakan data dari FirebaseAuth
                            String email = user.getEmail();
                            String displayName = user.getDisplayName();

                            if (email != null) {
                                editEmail.setText(email);

                                if (displayName == null || displayName.isEmpty()) {
                                    String namaDariEmail = email.split("@")[0];
                                    String namaPendek = ambilNamaPendek(namaDariEmail);
                                    String namaFinal = capitalizeFirstLetter(namaPendek);
                                    editName.setText(namaFinal);

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(namaFinal)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Log.d("EditProfil", "Nama default di-set dari email: " + namaFinal);
                                                }
                                            });
                                } else {
                                    editName.setText(displayName);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal memuat data profil", Toast.LENGTH_SHORT).show();
                        Log.e("EditProfil", "Gagal ambil data Firestore", e);
                    });
        } else {
            Toast.makeText(this, "User belum login!", Toast.LENGTH_SHORT).show();
        }
    }

    private String ambilNamaPendek(String username) {
        for (int i = 1; i < username.length(); i++) {
            if (Character.isUpperCase(username.charAt(i))) {
                return username.substring(0, i);
            }
        }
        return username.length() > 6 ? username.substring(0, 6) : username;
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void saveProfile() {
        String nama = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String telepon = editPhone.getText().toString().trim();

        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty()) {
            Toast.makeText(this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.PHONE.matcher(telepon).matches()) {
            Toast.makeText(this, "Format no telepon tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nama)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("EditProfil", "Display name updated.");
                            saveToFirestore(user.getUid(), nama, email, telepon);
                        } else {
                            Toast.makeText(this, "Gagal update profil", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveToFirestore(String userId, String nama, String email, String telepon) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("nama", nama);
        userMap.put("email", email);
        userMap.put("telepon", telepon);

        db.collection("users")
                .document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profil berhasil disimpan", Toast.LENGTH_SHORT).show();
                    Log.d("EditProfil", "Data Firestore berhasil disimpan.");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal simpan data ke Firestore", Toast.LENGTH_SHORT).show();
                    Log.e("EditProfil", "Error saving data", e);
                });
    }
}
