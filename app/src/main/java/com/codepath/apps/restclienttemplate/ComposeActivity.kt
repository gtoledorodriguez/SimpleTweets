package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var tvCharCount: TextView

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etCompose)
        btnTweet = findViewById(R.id.btnTweet)
        tvCharCount = findViewById(R.id.tvCharCount)
        client = TwitterApplication.getRestClient(this)

        //Handling the user's click on the tweet button
        btnTweet.setOnClickListener{
            //Grab content of edittext (etCompose)
            val tweetContent = etCompose.text.toString()

            //1. Make sure the tweet isn't empty
            if(tweetContent.isEmpty()){
                Toast.makeText(this,"Empty tweets not allowed!",Toast.LENGTH_SHORT).show()
            }
            //2. Make sure the tweets is under character count
            if (tweetContent.length > 280){
                Toast.makeText(this,"Tweet is too long! Limit is 280 characters", Toast.LENGTH_SHORT).show()
            }else{
                //TODO: Make an api call to Twitter to publish tweet
                //Toast.makeText(this,tweetContent, Toast.LENGTH_SHORT).show()
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler(){
                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG,"Successfully published tweet")

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet",tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }
                })
            }
        }

        //user can see a character counter with characters remaining for tweet out of 280
//        etCompose.addTextChangedListener(object :TextWatcher{
//            val tweetContent = etCompose.text.toString()
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(str: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                if (tweetContent.length > 280){
//                    Log.i(TAG,"Tweet is too long! Limit is 280 characters")
//                    btnTweet.setEnabled(false)
//                }else{
//                    btnTweet.setEnabled(true)
//                }
//            }
//
//        })
    }

    companion object{
        val TAG = "ComposeActivity"
    }
}