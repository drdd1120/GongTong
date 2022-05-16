package com.gongtong.everydaylife

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gongtong.*
import com.gongtong.R
import com.gongtong.databinding.FragmentCommuboardBinding
import com.gongtong.databinding.FragmentEmergencyBinding
import com.gongtong.databinding.FragmentEverydayLifeBinding
import com.gongtong.databinding.ItemRecyclerBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class EverydayLifeFragment : Fragment(R.layout.fragment_everyday_life) {
    private lateinit var articleDB: DatabaseReference
    private lateinit var gridAdapter: GridAdapter
    private var binding: FragmentEverydayLifeBinding? = null
    private val gridList = mutableListOf<GridData>()


    private val listener = object : ChildEventListener {

        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(GridData::class.java)
            articleModel ?: return
            gridList.add(articleModel) // 리스트에 새로운 항목을 더해서;
            gridAdapter.submitList(gridList) // 어뎁터 리스트에 등록;

            var spanCount=3
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                spanCount = 4
            }
            val mLayoutManager = GridLayoutManager(context,spanCount)
            //mLayoutManager.reverseLayout = true
            //mLayoutManager.stackFromEnd = true
            binding?.recyclerView?.setLayoutManager(mLayoutManager)
        }


        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentEverydayLifeBindingBinding = FragmentEverydayLifeBinding.bind(view)
        binding = fragmentEverydayLifeBindingBinding

        gridList.clear() //리스트 초기화;

        initDB()

        initArticleAdapter()

        initArticleRecyclerView()

        initListener()

    }

    private fun initDB() {
        articleDB = Firebase.database.reference.child("everydaylife")// 디비 가져오기;
    }

    private fun initArticleAdapter() {
        gridAdapter = GridAdapter {gridList->
            val mActivity = activity as MainActivity
            mActivity.receiveData(gridList.name)
        }
    }
    private fun initArticleRecyclerView() {
        // activity 일 때는 그냥 this 로 넘겼지만 (그자체가 컨텍스트라서) 그러나
        // 프레그 먼트의 경우에는 아래처럼. context
        var spanCount=3
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4
        }
        binding ?: return
        binding!!.recyclerView.layoutManager = GridLayoutManager(context,spanCount)
        binding!!.recyclerView.adapter = gridAdapter
    }

    private fun initListener() {
        articleDB.addChildEventListener(listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val recyclerView = requireView().findViewById(R.id.recycler_view) as RecyclerView
        //recyclerView.addItemDecoration(DividerItemDecoration(requireView().context, 0)) //리사이클러뷰 가로
        //recyclerView.addItemDecoration(DividerItemDecoration(requireView().context, 1)) //리사이클러뷰 세로
        gridAdapter.notifyDataSetChanged() // view 를 다시 그림;

    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) { // landscape
            binding!!.recyclerView.layoutManager = GridLayoutManager(context,4) // grid layout
        }
        else { // portrait
            binding!!.recyclerView.layoutManager = GridLayoutManager(context, 3) // linear layout
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        articleDB.removeEventListener(listener)
    }
}