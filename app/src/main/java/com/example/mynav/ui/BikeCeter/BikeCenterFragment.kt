package com.example.mynav.ui.BikeCeter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynav.R
import com.example.mynav.ui.getJsonData.GetGSONData
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.android.synthetic.main.fragment_slideshow.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException

class BikeCenterFragment : Fragment() {
    private var dataArray:JSONArray? = null
    private lateinit var bikeCenterViewModel: BikeCenterViewModel
//1.0.11111555
    //創立CreateView期間不可做事情，但是可以宣告資料型態
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bikeCenterViewModel =
                ViewModelProviders.of(this).get(BikeCenterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        bikeCenterViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        //Thread(BackgroundFetcher()).start()
        okHttpRequest()
        return root
    }
    private fun setRecyclerView(){
        rcv.layoutManager = LinearLayoutManager(context)
        rcv.adapter = Adapter(dataArray)
    }

    //在View創立後要做的事情(可使用物件做事)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var data = resources.openRawResource(R.raw.data)
        var readdata = data.bufferedReader().use { it.readText() }
        var jobjArray = JSONArray(readdata)
        //CallBack
        // val callback: Callback? = null
        var getGSONData = GetGSONData()
        //request
        getGSONData.handleJson()

        CoroutineScope(Dispatchers.IO).launch{
            okHttpRequest()
            withContext(Dispatchers.Main){
                //request
                val dataArray = getGSONData.jSonArrayfromGetGSONData

                //setRCV()
            }
        }
        //Thread.sleep(1000)
        bt_update.setOnClickListener {
            val dataArray = getGSONData.jSonArrayfromGetGSONData
            rcv.layoutManager = LinearLayoutManager(context)
            rcv.adapter = Adapter(dataArray)
        }
        //getHttpJsonData()
    }
    private fun asdasd(dataArray:JSONArray){
        rcv.layoutManager = LinearLayoutManager(context)
        rcv.adapter = Adapter(dataArray)
    }
    private fun okHttpRequest(){

            var client: OkHttpClient = OkHttpClient()
            var jSonArrayFromGetGSONData = JSONArray()
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
                        dataArray = JSONArray(resStr)
                        jSonArrayFromGetGSONData = JSONArray(resStr)
                        Log.d("CoroutineAA", jSonArrayFromGetGSONData[0].toString())
                        withContext(Dispatchers.Main){ rcv.adapter = Adapter(jSonArrayFromGetGSONData)}
                    }
                }
            })
    }

    private fun getHttpJsonData(){
        CoroutineScope(Dispatchers.IO).launch {
            okHttpRequest()
            withContext(Dispatchers.Main){
                var getGSONData = GetGSONData()
                //request
                val dataArray = getGSONData.jSonArrayfromGetGSONData
                rcv.layoutManager = LinearLayoutManager(context)
                rcv.adapter = Adapter(dataArray)
            }
        }
    }//getHttpJsonData()
/*
    inner class BackgroundFetcher : Runnable {
        override fun run() {
            var getGSONData = GetGSONData()
            val dataArray = getGSONData.jSonArrayfromGetGSONData
            rcv.layoutManager = LinearLayoutManager(context)
            rcv.adapter = Adapter(dataArray)
        }
    }
    */

}

