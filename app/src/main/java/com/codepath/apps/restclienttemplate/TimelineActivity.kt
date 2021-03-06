package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class TimelineActivity : AppCompatActivity() {

    lateinit var client:TwitterClient

    lateinit var  rvTweets: RecyclerView

    lateinit var adapter: TweetAdapter

    val tweets = ArrayList<Tweet>()

    lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)

        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing Timeline")
            populateHomeTimeline()
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )


        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetAdapter(tweets)

        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

        populateHomeTimeline()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //Handles clicks on menue item
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.compose){
            //Toast.makeText(this,"Ready to compose tweet!", Toast.LENGTH_SHORT).show()
            //Navigate to compose screen
            val intent = Intent(this, ComposeActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
        return super.onOptionsItemSelected(item)
    }

    //This method is called when we comeback from ComposeActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //Get Data from our intent (Tweet)
            val tweet = data?.getParcelableExtra("tweet") as Tweet

            //Update timeline

            //Modify the data source of tweets
            tweets.add(0,tweet)
            //Update adapter
            adapter.notifyItemInserted(0)

            rvTweets.smoothScrollToPosition(0)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun populateHomeTimeline(){
        client.getHomeTimeline(object: JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess!")

                val jsonArray = json.jsonArray

                try {
                    //CLear our our currently fetched tweets
                    adapter.clear()
                    val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listOfNewTweetsRetrieved)
                    adapter.addAll(tweets)
                    adapter.notifyDataSetChanged()
                    swipeContainer.setRefreshing(false)
                } catch (e: Exception) {
                    Log.e(TAG,"JSON Exception: $e")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure $statusCode")
                Log.d("DEBUG", "Fetch timeline error", throwable)
            }
        })
    }
    companion object{
        val TAG = "TimelineActivity"
        val REQUEST_CODE = 10
    }
}