package com.example.clientvideos

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.clientvideos.api.RetrofitInstance
import com.example.clientvideos.api.Video
import com.example.clientvideos.api.VideosAPI
import kotlinx.android.synthetic.main.item.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoCardAdapter(
    val context: Context,
    private val videos:List<Video>

    ): RecyclerView.Adapter<VideoCardAdapter.VideoViewHolder>(){

    class VideoViewHolder(val context:Context, view: View):RecyclerView.ViewHolder(view) {
    //private val IMAGE_BASE = "https://df78d45ba321.ngrok.io/videos/preview"
        fun bindVideo(video:Video){
            try{
                itemView.name.text = video.name.replace(".mp4","")
                //Glide.with(itemView).load(video.preview).into(itemView.imgPreview)

                var requestOptions:RequestOptions = RequestOptions()
                requestOptions = requestOptions.transform(CenterCrop() ,RoundedCorners(10))

                val retIn = RetrofitInstance.getRetrofitInstance().create(VideosAPI::class.java)
                retIn.getPreview(video.preview).enqueue(object :Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val bytes = response.body()?.bytes();


                        Glide.with(itemView).load(bytes).apply(requestOptions).into(itemView.imgPreview)
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    }

                })

                itemView.videoLayout.setOnClickListener {
                    val intent = Intent(context,WatchVideo::class.java)
                    intent.putExtra("videoName", itemView.name.text as String +".mp4")
                    context.startActivity(intent)
                    //Toast.makeText(context, itemView.name.text as String + ".mp4", Toast.LENGTH_SHORT).show()
                }

                // Picasso.get().load(IMAGE_BASE+video.preview).into(itemView.imgPreview)
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(context,
            LayoutInflater.from(context).inflate(R.layout.item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
       holder.bindVideo(videos[position])
    }

    override fun getItemCount(): Int = videos.size
}


