package com.jun.naverapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jun.data.NaverAPI
import com.jun.adapter.NewsAdapter
import com.jun.data.ResultGetSearchNews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var totalCount =20 // 전체 아이템 개수
    private var start = 1       // 현재 페이지
    private var isLoading=false
    private lateinit var adapter : NewsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val API=NaverAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter=NewsAdapter()
        setupRecyclerView()
        getUsers(false)
        initScrollListener()
        swipeRefreshLayout= findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefreshLayout.setOnRefreshListener(this)
        }

    private fun getUsers(isOnRefresh:Boolean){
        val progressbar: ProgressBar =findViewById(R.id.progressBar)
        isLoading=true
        if(!isOnRefresh) progressbar.visibility= View.VISIBLE

        API.getSearchNews("test",10,start).enqueue(object : Callback<ResultGetSearchNews> {
            override fun onResponse(
                call: Call<ResultGetSearchNews>,
                response: Response<ResultGetSearchNews>
            ) {
                response.body()?.let{
                    adapter.addPosts(it.items)
                    // totalCount=it.total
                }
                Log.d("loadPosts", "$start, $totalCount")
                plusStart()
                if(start>= totalCount){
                    progressbar.visibility= View.GONE
                }else{
                progressbar.visibility=View.INVISIBLE
                }
                isLoading=false
            }

            override fun onFailure(call: Call<ResultGetSearchNews>, t: Throwable) {
                Log.d("response", "fail")
                Log.d("response", "fail")
            }

        })
    }
    private fun setupRecyclerView() {
        val rvNews: RecyclerView=findViewById(R.id.rv_news)
        rvNews.setHasFixedSize(true)
        rvNews.layoutManager = LinearLayoutManager(this)
        rvNews.adapter=adapter
    }

    private fun initScrollListener(){
        val rvNews: RecyclerView=findViewById(R.id.rv_news)
        rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = rvNews.layoutManager
                val visibleItemCount= layoutManager!!.childCount
                val pastVisibleItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()
                val total=adapter.itemCount
                if(!isLoading && start< totalCount){
                    if(visibleItemCount+pastVisibleItem>=total){
                        plusStart()
                        getUsers(false)
                    }
                }

            }
            })
    }
    private fun plusStart(): Int {
        start+=10
        return start
    }
    override fun onRefresh(){
        adapter.clear()
        start=1
        getUsers(true)
        swipeRefreshLayout.isRefreshing = false
    }
}