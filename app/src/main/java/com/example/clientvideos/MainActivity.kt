package com.example.clientvideos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clientvideos.api.User
import okhttp3.ResponseBody
import retrofit2.Call
import com.example.clientvideos.api.RetrofitInstance;
import com.example.clientvideos.api.VideosAPI
import com.google.gson.Gson
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val login = findViewById<EditText>(R.id.login);
        val password = findViewById<EditText>(R.id.password);
        val signin = findViewById<Button>(R.id.signin);


        signin.setOnClickListener {

            val nickname: String = login.text.toString()
            val pwd: String = password.text.toString()
            val retIn = RetrofitInstance.getRetrofitInstance().create(VideosAPI::class.java)
            if (nickname.isEmpty()) {
                Toast.makeText(this@MainActivity, "Введите логин", Toast.LENGTH_SHORT).show()
            } else if(pwd.isEmpty()){
                Toast.makeText(this@MainActivity, "Введите пароль", Toast.LENGTH_SHORT).show()
            } else{
                retIn.auth(nickname, pwd).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.body() !== null) {
                            if (response.code() == 200) {
                                Toast.makeText(this@MainActivity, "ОК", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@MainActivity, Menu::class.java)

                                val jsonString = response.body()?.string();
                                val userData: User = Gson().fromJson(jsonString, User::class.java);

                                println("id" + userData.id + "login" + userData.login + "pwd" + userData.password);
                                intent.putExtra("login", userData.login)
                                startActivity(intent)

                            }
                        }
                        if (response.code() == 400) {
                            Toast.makeText(
                                    this@MainActivity,
                                    "Неправильный логин или пароль",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                })
            }

        }
    }
    }

