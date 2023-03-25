package com.example.miniweacher

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val city_text = findViewById<TextView>(R.id.title_city_name)

        val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //处理返回结果
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val resultStr = data?.getStringExtra("data_return")
                Log.d("abc","返回的城市代码： ${resultStr}")
                // 处理返回结果
                //获取网络数据，并更新界面
            }
        }
        val city_manager = findViewById<ImageView>(R.id.title_city_manager)
        city_manager.setOnClickListener {
            val intent = Intent(this, SelectCity::class.java)
            //startActivity(intent)
            someActivityResultLauncher.launch(intent)
        }

        // 使用Thread类创建新线程
        val thread = Thread {
            val client = OkHttpClient()
            val url = "https://devapi.qweather.com/v7/weather/now?location=101010100&key=69f77d902aa74c689ad27f556b5eea52"
            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // 处理请求失败的情况
                    Log.e("abc", "请求失败：${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    // 处理请求成功的情况
                    val jsonString = response.body?.string()
                    Log.i("abc", "请求成功：$jsonString")

                    data class nowVo(
                        var temp: String,
                        var text: String
                    )
                    data class ResponseVo(
                        var code: Int,
                        var fxLink: String,
                        var now: nowVo
                    )


                    val gson = Gson()
                    val weather = gson.fromJson(jsonString, ResponseVo::class.java)
                    Log.i("abc", "请求成功：$jsonString")

                    Log.i("abc", "请求成功：${weather.now.text}")

                    // 可以在UI线程中更新UI
                    runOnUiThread {
                        // 更新UI
                        //city_text.text = "天气：${weather.now.text}℃"
                    }

                }
            })
        }
        thread.start()
    }
}