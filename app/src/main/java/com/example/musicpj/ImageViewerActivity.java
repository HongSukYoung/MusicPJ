package com.example.musicpj;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

public class ImageViewerActivity extends AppCompatActivity {
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        img = findViewById(R.id.imageView);

        String imgUrl = getIntent().getStringExtra("url");

        GlideUrl url = new GlideUrl(imgUrl, new LazyHeaders.Builder().addHeader("User-Agent", "Android").build());
        Glide.with(ImageViewerActivity.this).load(url).into(img);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // back 버튼 설정법
        // 1. finish()
        // 2. 기계의 back 버튼 눌렀을때의 콜백 메소드 onBackPressed();
        finish();
        return true;
    }
}
