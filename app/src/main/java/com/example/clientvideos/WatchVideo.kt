package com.example.clientvideos

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.media2.exoplayer.external.trackselection.DefaultTrackSelector
import com.example.clientvideos.api.RetrofitInstance
import com.example.clientvideos.api.VideosAPI
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.norulab.exofullscreen.MediaPlayer
import com.norulab.exofullscreen.preparePlayer
import com.norulab.exofullscreen.setSource
import kotlinx.android.synthetic.main.activity_watch_video.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WatchVideo : AppCompatActivity(), Player.EventListener{
    private lateinit var url :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_video)

        loadWebpage()
        pageLoadProgressBar.visibility = View.VISIBLE
        if(savedInstanceState!=null){
            webview.post{
                webview.loadUrl("https://1d57c6ddcecf.ngrok.io/videos/getVideo?title=%D0%9C%D0%B0%D0%B9%D0%BA%D0%BB%20%D0%B8%20%D0%94%D0%B6%D0%B8%D0%BC.mp4")
            }
        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webview.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webview.restoreState(savedInstanceState)
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebpage() {
        webview.settings.javaScriptEnabled = true
        webview.settings.allowFileAccess = true
        @Suppress("DEPRECATION")
        webview.settings.setAppCacheEnabled(true)


        if(intent.hasExtra("videoName")){
            val name: String? = this.intent.getStringExtra("videoName")
            if (name != null) {
                videoName.text = name.replace(".mp4", "")
            }
            val retIn = RetrofitInstance.getRetrofitInstance().create(VideosAPI::class.java)
            if (name != null) {
                retIn.getVideo(name).enqueue(object : Callback<ResponseBody> {

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                        url = response.raw().request().url().toString()
                        updateProgress()
                        try{
                            webview.loadUrl(url)
                        }catch (e: UnsupportedOperationException){
                            e.printStackTrace()
                        }
                        MediaPlayer.startPlayer()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    }

                })

            }
        }

    }
    fun updateProgress(){
        webview.webViewClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (view != null) {
                    title = view.title
                }
                pageLoadProgressBar.visibility = View.GONE
            }
        }

        webview.webChromeClient = object : WebChromeClient(){
            private  var mCustomView: View? = null
            private  var mCustomViewCallback: WebChromeClient.CustomViewCallback? = null
            private lateinit var mFullscreenContainer: FrameLayout
            private var mOriginalOrientation:Int = 0
            private  var mOriginalSystemUiVisibility:Int = 0


            override fun getDefaultVideoPoster(): Bitmap? {
                return if (mCustomView == null) {
                    null
                } else BitmapFactory.decodeResource(applicationContext.resources, 2130837573)
            }

            override fun onHideCustomView() {
                (window.decorView as FrameLayout).removeView(mCustomView)
                mCustomView = null
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
                requestedOrientation = mOriginalOrientation
                mCustomViewCallback?.onCustomViewHidden()
                mCustomViewCallback = null
            }

            override fun onShowCustomView(
                paramView: View?,
                paramCustomViewCallback: CustomViewCallback?
            ) {
                if (mCustomView != null) {
                    onHideCustomView()
                    return
                }
                mCustomView = paramView
                @Suppress("DEPRECATION")
                mOriginalSystemUiVisibility = window.decorView.systemUiVisibility
                mOriginalOrientation = requestedOrientation
                mCustomViewCallback = paramCustomViewCallback
                (window.decorView as FrameLayout).addView(
                    mCustomView,
                    FrameLayout.LayoutParams(-1, -1)
                )
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = 3846
            }
        }
    }

}



