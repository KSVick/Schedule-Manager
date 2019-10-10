package edu.bluejack18_2.schedulemanagerapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import edu.bluejack18_2.schedulemanagerapplication.adapter.NewsAdapter;
import edu.bluejack18_2.schedulemanagerapplication.api.ApiClient;
import edu.bluejack18_2.schedulemanagerapplication.api.ApiInterface;
import edu.bluejack18_2.schedulemanagerapplication.model.Article;
import edu.bluejack18_2.schedulemanagerapplication.model.News;
import edu.bluejack18_2.schedulemanagerapplication.utility.Utils;

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
        layoutManager = new LinearLayoutManager(this);
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

                    initListener();
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

    private void initListener(){
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(NewsAPIActivity.this,NewsDetailActivity.class);
//
                Article article = articles.get(position);
                Intent intentUrl = new Intent("android.intent.action.VIEW", Uri.parse(article.getUrl()));
                startActivity(intentUrl);
//                intent.putExtra("url",article.getUrl());
//                intent.putExtra("title",article.getTitle());
//                intent.putExtra("img",article.getUrlToImage());
//                intent.putExtra("date",article.getPublishedAt());
//                intent.putExtra("source",article.getSource().getName());
//                intent.putExtra("author",article.getAuthor());
//
//                startActivity(intent);
            }
        });
    }


}
