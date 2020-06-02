package com.example.mynav.ui.BikeCeter

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynav.R
import com.example.mynav.ui.getJsonData.GetGSONData
import com.example.mynav.ui.map.MapFragment
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
    companion object{
        val instance : BikeCenterFragment by lazy {
            BikeCenterFragment()
        }
    }
    private lateinit var dataArray:JSONArray
    private lateinit var dataArrayForEName:MutableList<String>
    private lateinit var bikeCenterViewModel: BikeCenterViewModel
    private var displayList:MutableList<String> = ArrayList()
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
        val inToHomeFragment = inflater.inflate(R.layout.fragment_home,container,false)
        Log.d("TAG:Fragment","bike = ${BikeCenterFragment.instance.id}")
        return root
    }


    //在View創立後要做的事情(可使用物件做事)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch{ okHttpRequest() }
        bt_update.setOnClickListener { //okHttpRequest()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment,MapFragment.instance).commit()
        }
        setHasOptionsMenu(true)
        setRecyclerView()
    }

    //用來設定recyclerView的adapter
    fun setRecyclerView(){
        rcv.layoutManager = LinearLayoutManager(this.requireContext())
    }
    //okHttp的連線需求
    private fun okHttpRequest(){
        var client: OkHttpClient = OkHttpClient()
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
                        dataArrayForEName = ArrayList()
                        for (i in 0 until dataArray.length()){
                            dataArrayForEName.add(dataArray.getJSONObject(i).get("EName").toString())
                        }
                        displayList.addAll(dataArrayForEName)
                        Log.d("ename","$dataArrayForEName")
                        /*
                        getActivity()?.runOnUiThread{
                            setRecyclerView()
                            rcv.adapter = Adapter(jSonArrayFromGetGSONData)
                        }
                        */
                        withContext(Dispatchers.Main){
                            setRecyclerView()
                            rcv.adapter = Adapter(dataArray,this@BikeCenterFragment,dataArrayForEName)
                        }
                    }
                }
            })
    }

    //    inner class Adapter(array:JSONArray,val fg:Fragment): RecyclerView.Adapter<Adapter.CustomViewHolder>() {
//
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main,menu)
        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem != null){
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = "Fragment.."
            searchView.setBackgroundColor(Color.RED)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String): Boolean {
                    Toast.makeText(context,"See",Toast.LENGTH_SHORT).show()
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    displayList.clear()
                    if (newText.isNotEmpty()) {
                        val search = newText.toLowerCase()
                        /*
                        for (i in 0 until dataArrayForEName.size){
                            if (dataArrayForEName[i].toString().toLowerCase().contains(search)){
                                displayList.add(dataArrayForEName.get(i).toString().toLowerCase().contains(search).toString())
                            }
                        }
                        */
                        dataArrayForEName.forEach {
                            if (it.toLowerCase().contains(search)) {
                                displayList.add(it)
                            }
                        }
                    } else {
                        displayList.addAll(dataArrayForEName)
                    }
                    rcv.adapter = Adapter(dataArray,this@BikeCenterFragment,dataArrayForEName)
                    rcv.adapter?.notifyDataSetChanged()

                    return true
                }
            })
            }
    }
//        private val listTitle = array
//        private val mOnClickListener: View.OnClickListener = View.OnClickListener { }
//        //RecyclerView必備方法之1
//        //創立數量隨著listTitle的長度改變
//        override fun getItemCount(): Int {
//            return listTitle.length() ?: 0
//        }
//
//        //RecyclerView必備方法之2 - 建立ViewHolder
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
//            //val layoutInflater = LayoutInflater.from(parent?.context)
//            //以下是cellForRow這個變數可以參照R.layout.recycleview_rowstyle的XML樣板
//            val cellForRow = LayoutInflater.from(parent.context)
//                .inflate(R.layout.recycleview_rowstyle, parent, false)
//            //parent.setOnClickListener(mOnClickListener)
//            cellForRow.setOnClickListener {
//                Log.d("click3", " + asclickasclickasda")
//            }
//            return CustomViewHolder(cellForRow)
//        }
//
//        //RecyclerView必備方法之3 - 使用ViewHolder做出你要的東西(可搭配自訂的ViewHolder)
//        @SuppressLint("SetTextI18n")
//        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//            val index = listTitle.getJSONObject(position)
//            val x = index.get("X")
//            val y = index.get("Y")
//            //var bd = Bundle().putString("")
//            holder.position.text = "${index.get("Position")}"
//            holder.eName.text = "${index.get("EName")}"
//            holder.cArea.text = "${index.get("CArea")}"
//            holder.eArea.text = "${index.get("EArea")}"
//            holder.cAddress.text = "${index.get("CAddress")}"
//            holder.availableCNT.text = "可借車輛：${index.get("AvailableCNT")}"
//            holder.empCNT.text = "可停空位：${index.get("EmpCNT")}"
//
//            holder.itemView.setOnClickListener {
////              val inflater: LayoutInflater
////              val view = inflater.inflate(R.layout.fragment_login, container, false)
//                Log.d("click2", "${holder.position.text}")
//                fg.requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.nav_host_fragment,MapFragment.instance).commit()
//                //MapFragment.instance.takeMeToSomeWhereIClicked("$x","$y")
//                Log.d("TAG:Fragment","adapter = ${MapFragment.instance}")
//            }
//        }
//
//        //自訂的ViewHolder
//        inner class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {
//            val position: TextView = v.findViewById<TextView>(R.id.textView_Position)
//            val eName: TextView = v.findViewById<TextView>(R.id.textView_EName)
//            val cArea: TextView = v.findViewById<TextView>(R.id.textView_CArea)
//            val eArea: TextView = v.findViewById<TextView>(R.id.textView_EArea)
//            val cAddress: TextView = v.findViewById<TextView>(R.id.textView_CAddress)
//            val availableCNT: TextView = v.findViewById<TextView>(R.id.textView_AvailableCNT)
//            val empCNT: TextView = v.findViewById<TextView>(R.id.textView_EmpCNT)
//            val image = v.findViewById<ImageView>(R.id.imageView3)
//        }
//
//
//    }
}

