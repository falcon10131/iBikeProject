package com.example.mynav.ui.getJsonData

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mynav.ui.BikeCeter.BikeCenterFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class GetGSONData  {
    private var client: OkHttpClient = OkHttpClient()
    var jSonArrayfromGetGSONData = JSONArray()

    fun handleJson(){
        val request = Request.Builder()
        .url("http://e-traffic.taichung.gov.tw/DataAPI/api/YoubikeAllAPI")
        .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                }
                @Throws(IOException::class)
                override fun onResponse(response: Response){
                    CoroutineScope(Dispatchers.IO).launch {
                        val resStr = response.body().string()
                        jSonArrayfromGetGSONData = JSONArray(resStr)
//                      val jsonData = Gson().fromJson<List<IbikeData>>(resStr, object : TypeToken<List<IbikeData>>() {
//                      }.type)
                        Log.d("AA",jSonArrayfromGetGSONData[0].toString())
                        withContext(Dispatchers.Main){
                            BikeCenterFragment().setRecyclerView()
                        }
                    }
                }
            })
        }
    }
