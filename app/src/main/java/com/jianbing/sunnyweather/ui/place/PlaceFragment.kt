package com.jianbing.sunnyweather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jianbing.sunnyweather.R
import kotlinx.android.synthetic.main.fragement_place.*

class PlaceFragment:Fragment() {
    val viewModel by lazy { ViewModelProviders.of(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragement_place,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager=LinearLayoutManager(activity)
        recycleView.layoutManager=layoutManager
        recycleView.adapter=PlaceAdapter(this,viewModel.placeList)
        searchPlaceEdit.addTextChangedListener{editable->
            val content=editable.toString()
            if (content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                recycleView.visibility=View.GONE
                bgImageView.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer {result->
            val places=result.getOrNull()
            if(places!=null){
                recycleView.visibility=View.VISIBLE
                bgImageView.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未查询到任何地点",Toast.LENGTH_SHORT)
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}