package com.example.musicpj;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Youtube_search extends AppCompatActivity {
    RecyclerView rv;
    YoutubeAdapter adapter;
    ArrayList<Youtube> youtubeList;
    String pageToken;

    ProgressBar pb;
    ImageView imgSearch;
    EditText editSearch;

    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_search);

        pb = findViewById(R.id.progressBar);

        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);

        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(Youtube_search.this));

        // 검색 이미지 클릭시 검색어 검색
        imgSearch.setOnClickListener(view -> {
            String searchURL = "https://www.googleapis.com/youtube/v3/search?part=snippet" + Config.YOUTUBE_API_KEY;
            String keyword = "&q=" + editSearch.getText().toString().trim();
            String maxResultsParam = "&maxResults=20";
            URL = searchURL + keyword + maxResultsParam;
            // Json 네트워크 통신 메소드
            getData();
        });

        // 리스트가 마지막까지 스크롤 될 때 이벤트
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 리스트의 행 정보 저장
                int lastPosition = ((LinearLayoutManager) rv.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                // 리스트의 총 행의 갯수 확인
                int totalCount = rv.getAdapter().getItemCount();

                // 리스트의 마지막에 도달 할 경우, 인덱스는 0부터 시작이므로 +1
                if ( lastPosition+1 == totalCount ) {
                    // 페이지 토큰이 없으면 pass
                    if ( pageToken == null ) {
                        return;
                    }
                    // 페이지 토큰이 있으면 다음 페이지로 이동
                    else {
                        String pageTokenURL = "&pageToken=";
                        URL = URL + pageTokenURL + pageToken;
                        // Json 네트워크 통신 메소드
                        getData();
                    }
                }
            }
        });
    }

    public void getData() {
        pb.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(Youtube_search.this);
        // 네트워크 통신을 위한 JsonObjectRequest 객체 생성
        // 생성자 : http Method, API URL, 전달 할 데이터, 실행 코드(Listener)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    // API 호출 결과 실행
                    try {

                        // 다음 페이지가 존재하는지 유무 확인
                        if (response.has("nextPageToken")) {
                            pageToken = response.getString("nextPageToken");
                        } else {
                            pageToken = null;
                        }

                        youtubeList = new ArrayList<Youtube>();

                        JSONArray jarr = response.getJSONArray("items");

                        for (int i = 0; i < jarr.length(); i++) {
                            JSONObject jdata = jarr.optJSONObject(i);

                            // 유튜브 비디오 ID
                            JSONObject jid = jdata.optJSONObject("id");
                            String videoId = jid.optString("videoId");

                            // 유튜브 제목과 내용
                            JSONObject jsnippet = jdata.optJSONObject("snippet");
                            String title = jsnippet.optString("title");
                            String description = jsnippet.optString("description");

                            // 유튜브 썸네일 이미지 디폴트
                            JSONObject jthumb = jsnippet.optJSONObject("thumbnails");
                            JSONObject jdefault = jthumb.optJSONObject("default");
                            String thumbUrl = jdefault.optString("url");

                            // 유튜브 썸네일 이미지 가장 큰 크기
                            JSONObject jhigh = jthumb.optJSONObject("high");
                            String thumbUrlHigh = jhigh.optString("url");

                            Youtube youtube = new Youtube(videoId, title, description, thumbUrl, thumbUrlHigh);
                            youtubeList.add(youtube);
                        }
                        adapter = new YoutubeAdapter(Youtube_search.this, youtubeList);
                        rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        pb.setVisibility(View.INVISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pb.setVisibility(View.INVISIBLE);
                    }
                }, error -> {
            Log.i("onErrorResponse", "" + error);
            pb.setVisibility(View.INVISIBLE);
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add((jsonObjectRequest));
    }
}