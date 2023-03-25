package com.example.miniweacher

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

class SelectCity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_city)
        val newItems = mutableListOf<City>(
            City("北京", "101010100"),
            City("上海", "101020100"),
            City("广州", "101280101"),
            City("深圳", "101280601")
        )
        val cityAdapter = CityAdapter(this, newItems)

        val dbHelper = MyDatabaseHelper(this)

        val editText = findViewById<EditText>(R.id.search_edit)

        val title_name = findViewById<TextView>(R.id.title_name)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is being changed.
                val text = s.toString()
                Log.d("abc", "onTextChanged->New text: $text")


                // 查询城市数据并更新 Adapter数据
                newItems.clear()
                val citylist = dbHelper.searchCitiesByName(text)
                // 处理查询结果
                for (item in citylist) {
                    Log.d("abc", "$item")
                    newItems.add(item)
                }
                cityAdapter.notifyDataSetChanged()

            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed.
            }
        })


        val cityListView = findViewById<ListView>(R.id.city_list)



        cityListView.adapter = cityAdapter

        cityListView.setOnItemClickListener { _, _, position, _ ->
            val city = newItems[position]
            val cityName = city.name
            val cityCode = city.code
            // TODO: 获取天气预报数据
            Log.d("abc", "cityName: $cityName  cityCode: $cityCode")
            title_name.setText("当前选择：$cityName")
            // 获取SharedPreferences对象
            val preferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            // 写入数据
            val editor = preferences.edit()
            editor.putString("cityname", cityName)
            editor.putString("citycode", cityCode)
            editor.apply()
        }

        val title_back = findViewById<ImageView>(R.id.title_back)
        title_back.setOnClickListener {

            // 获取SharedPreferences对象
            val preferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

            // 读取数据
            val cityname = preferences.getString("cityname", "北京")
            val citycode = preferences.getString("citycode", "101010100")

            Log.d("abc",cityname.toString())
            Log.d("abc",citycode.toString())

            val intent = Intent()
            intent.putExtra("data_return", citycode)
            setResult(RESULT_OK, intent)

            finish()
        }


        val data = dbHelper.queryData()

        // 处理查询结果
        for (name in data) {
            Log.d("abc", "Name: $name")
        }

        val dataCity = dbHelper.queryCityData()

        // 处理查询结果
        for (name in dataCity) {
            Log.d("abc", "Name: $name")
        }
    }
}