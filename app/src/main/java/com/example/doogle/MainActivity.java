package com.example.doogle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ImageView goHome, micIcon;
    RecyclerView recyclerView;
    TextView searchLabel;
    RelativeLayout searchBar;
    String baseUrl, apikey, newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        baseUrl = "https://newsapi.org/v2";
        searchLabel = findViewById(R.id.searchLabel);
        apikey = "fe4bff67d522423090cac4564ccbeaee";
        searchBar = findViewById(R.id.searchbar);
        goHome = findViewById(R.id.goHome);
        micIcon = findViewById(R.id.micIcon);
        searchLabel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);

                    }
                }
        );

        NewsApiClient newsApiClient = new NewsApiClient(apikey);
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .pageSize(100)
                        .language("en")
                        .country("in")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<Article> articles = response.getArticles();
                        NewsAdapter adapter = new NewsAdapter(articles);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(String newsUrl) {
                                Intent intent3 = new Intent(MainActivity.this, BasicWebView.class);
                                intent3.putExtra("newsUrl", newsUrl);
                                startActivity(intent3);

                            }
                        });

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        micIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());

                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, 1);

                } catch (Exception e){
                    Toast.makeText(MainActivity.this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                Intent textIntent = new Intent(MainActivity.this, SearchActivity.class);
                textIntent.putExtra("textSearch", Objects.requireNonNull(result).get(0));
                startActivity(textIntent);

            }
        }
    }
}