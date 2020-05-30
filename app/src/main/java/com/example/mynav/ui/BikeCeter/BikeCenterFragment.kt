package com.example.mynav.ui.BikeCeter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
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
//        rcv.setOnClickListener {
//            it.isVisible
//        }
        //okHttpRequest()
        return root
    }


    //在View創立後要做的事情(可使用物件做事)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch{ okHttpRequest() }
        bt_update.setOnClickListener { okHttpRequest() }
    }
    //用來設定recyclerView的adapter
    fun setRecyclerView(){
        val getGSONData = GetGSONData()
        val dataArray = getGSONData.jSonArrayfromGetGSONData
        rcv.layoutManager = LinearLayoutManager(context)
        rcv.adapter = Adapter(dataArray)
    }
    //okHttp的連線需求
    private fun okHttpRequest(){
        var client: OkHttpClient = OkHttpClient()
        var jSonArrayFromGetGSONData = JSONArray()
        val request = Request.Builder()
            .url("http://e-traffic.taichung.gov.tw/DataAPI/api/YoubikeAllAPI")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show()
            }
                @Throws(IOException::class)
                override fun onResponse(response: Response){
                    CoroutineScope(Dispatchers.IO).launch {
                        val resStr = response.body().string()
                        dataArray = JSONArray(resStr)
//                        getActivity()?.runOnUiThread{
//                            setRecyclerView()
//                            rcv.adapter = Adapter(jSonArrayFromGetGSONData)
//                        }
                        withContext(Dispatchers.Main){
                            setRecyclerView()
                            rcv.adapter = Adapter(dataArray)
                        }
                    }
                }
            })
    }
}

