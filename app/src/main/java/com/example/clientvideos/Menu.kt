package com.example.clientvideos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clientvideos.api.RetrofitInstance
import com.example.clientvideos.api.Video
import com.example.clientvideos.api.VideosAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_menu.*
import java.util.ArrayList
import kotlin.Exception


class Menu : AppCompatActivity() {
    var videoList = ArrayList<Video>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val login = findViewById<TextView>(R.id.UserLogin)
        if(intent.hasExtra("login")){
            val getLogin: String? = this.intent.getStringExtra("login")
            login.text = getLogin
        }




        recyclerView.layoutManager = LinearLayoutManager(this@Menu)
        recyclerView.setHasFixedSize(true)


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){

                }
            }
        })

        pullToRefresh.setOnRefreshListener {
            videoList.clear()
            getVideoData()
            recyclerView.adapter=VideoCardAdapter(this@Menu, videoList)
            pullToRefresh.isRefreshing = false
        }

        try{
            getVideoData()
            recyclerView.adapter=VideoCardAdapter(this@Menu, videoList)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }


    private fun getVideoData(){
        val retIn = RetrofitInstance.getRetrofitInstance().create(VideosAPI::class.java)
        retIn.getAllVideos().enqueue(object : Callback<List<Video>> {
            override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                try {
                    if(response.body()!=null && response.isSuccessful){
                        videoList.addAll(response.body()!!)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<List<Video>>, t: Throwable) {

            }


        })
    }


}

