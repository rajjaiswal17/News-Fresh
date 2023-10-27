package com.example.newsfresh

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsfresh.databinding.ActivityMainBinding

class MainActivity : Activity(), newsItemClicked {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter : newsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()

        mAdapter = newsListAdapter(this)
        binding.recyclerView.adapter = mAdapter
    }
    private fun fetchData() {

        val url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=c259fb0ea866424d842d80f41c8bac5e"
        val jsonObjectRequest = object :JsonObjectRequest(Method.GET, url, null,
            { response ->
                val newsJsonArray = response.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    if (newsJsonObject.isNull("urlToImage") or newsJsonObject.isNull("title")){
                        continue
                    }
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            {
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder();
        val customTabsIntent= builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}