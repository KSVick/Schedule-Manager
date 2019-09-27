package com.example.schedulemanagerapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.adapter.NewsAdapter;
import com.example.schedulemanagerapplication.api.ApiClient;
import com.example.schedulemanagerapplication.api.ApiInterface;
import com.example.schedulemanagerapplication.model.Article;
import com.example.schedulemanagerapplication.model.News;
import com.example.schedulemanagerapplication.utility.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAPIActivity extends AppCompatActivity {

    public static final String API_KEY = "53c83196ab2c4007a153e7f956983f15";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private NewsAdapter adapter;
    private String TAG = NewsAPIActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_api);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(NewsAPIActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        loadJSON();

    }

    public void loadJSON(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();

        Call<News> call;
        call = apiInterface.getNews(country,API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body().getArticle() != null){
                    if(!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    adapter = new NewsAdapter(articles, NewsAPIActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(NewsAPIActivity.this, "Empty Result!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });
    }
}