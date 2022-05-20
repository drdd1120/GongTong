package com.gongtong.commuboard

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gongtong.*
import com.gongtong.R
import com.gongtong.databinding.FragmentCommuboardBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CommuboardFragment : Fragment(R.layout.fragment_commuboard) {
    private lateinit var articleDB: DatabaseReference
    private lateinit var gridAdapter: GridAdapter
    private var binding: FragmentCommuboardBinding? = null
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
            binding?.recyclerView?.layoutManager = mLayoutManager
        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentCommuboardBinding = FragmentCommuboardBinding.bind(view)
        binding = fragmentCommuboardBinding
        gridList.clear()
        initDB()
        initArticleAdapter()
        initArticleRecyclerView()
        initListener()
    }

    private fun initDB() {
        articleDB = Firebase.database.reference.child("represent").child("r_noun")// 디비 가져오기;
    }

    private fun initArticleAdapter() {
        gridAdapter = GridAdapter {gridList->
            if (gridList.name=="동물") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 1)
            }
            if (gridList.name=="색깔") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 2)
            }
            if (gridList.name=="놀이/문화/운동") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 3)
            }
            if (gridList.name=="음식") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 4)
            }
            if (gridList.name=="과일") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 5)
            }
            if (gridList.name=="가구/전자제품") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 6)
            }
            if (gridList.name=="병원/증상") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 7)
            }
            if (gridList.name=="직업") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 8)
            }
            if (gridList.name=="사람") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 9)
            }
            if (gridList.name=="장소") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 10)
            }
            if (gridList.name=="식물") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 11)
            }
            if (gridList.name=="위치") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 12)
            }
            if (gridList.name=="모양") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 13)
            }
            if (gridList.name=="시간") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 14)
            }
            if (gridList.name=="교통") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 15)
            }
            if (gridList.name=="채소") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 16)
            }
            if (gridList.name=="날씨") {
                (activity as MainActivity).replaceFragment(DetailFragment(), 17)
            }
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
        //val recyclerView = requireView().findViewById(R.id.recycler_view) as RecyclerView
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