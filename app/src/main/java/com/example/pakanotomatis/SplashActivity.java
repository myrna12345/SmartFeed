package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Inisialisasi ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // Menunggu selama 3 detik dan kemudian pindah ke Login Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Pindah ke ActivityLogin setelah delay
                Intent intent = new Intent(SplashActivity.this, ActivityLogin.class);
                startActivity(intent);
                finish();  // Pastikan splash screen tidak bisa kembali
            }
        }, 3000);  // Waktu delay 3 detik
    }
}
