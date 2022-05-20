package com.gongtong.commuboard

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gongtong.*
import com.gongtong.R
import com.gongtong.databinding.FragmentDetailBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DetailFragment : Fragment(R.layout.fragment_detail) {
    private lateinit var articleDB: DatabaseReference
    private lateinit var gridAdapter: GridAdapter
    private var binding: FragmentDetailBinding? = null
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
        val fragmentDetailFragment = FragmentDetailBinding.bind(view)
        binding = fragmentDetailFragment
        gridList.clear()
        initDB()
        initArticleAdapter()
        initArticleRecyclerView()
        initListener()
    }

    private fun initDB() {
        val result = arguments?.getInt("result")
        if(result==1) {
            articleDB = Firebase.database.reference.child("noun").child("animal")// 디비 가져오기;
        }
        if(result==2) {
            articleDB = Firebase.database.reference.child("noun").child("color")
        }
        if(result==3) {
            articleDB = Firebase.database.reference.child("noun").child("entertainment")
        }
        if(result==4) {
            articleDB = Firebase.database.reference.child("noun").child("food")
        }
        if(result==5) {
            articleDB = Firebase.database.reference.child("noun").child("fruit")
        }
        if(result==6) {
            articleDB = Firebase.database.reference.child("noun").child("furniture")
        }
        if(result==7) {
            articleDB = Firebase.database.reference.child("noun").child("hospital")
        }
        if(result==8) {
            articleDB = Firebase.database.reference.child("noun").child("job")
        }
        if(result==9) {
            articleDB = Firebase.database.reference.child("noun").child("people")
        }
        if(result==10) {
            articleDB = Firebase.database.reference.child("noun").child("place")
        }
        if(result==11) {
            articleDB = Firebase.database.reference.child("noun").child("plant")
        }
        if(result==12) {
            articleDB = Firebase.database.reference.child("noun").child("position")
        }
        if(result==13) {
            articleDB = Firebase.database.reference.child("noun").child("shape")
        }
        if(result==14) {
            articleDB = Firebase.database.reference.child("noun").child("time")
        }
        if(result==15) {
            articleDB = Firebase.database.reference.child("noun").child("transportation")
        }
        if(result==16) {
            articleDB = Firebase.database.reference.child("noun").child("vegetable")
        }
        if(result==17) {
            articleDB = Firebase.database.reference.child("noun").child("weather")
        }
        if(result==18) {
            articleDB = Firebase.database.reference.child("verb").child("v_animal_plant")
        }
        if(result==19) {
            articleDB = Firebase.database.reference.child("verb").child("v_entertainment")
        }
        if(result==20) {
            articleDB = Firebase.database.reference.child("verb").child("v_food")
        }
        if(result==21) {
            articleDB = Firebase.database.reference.child("verb").child("v_furniture")
        }
        if(result==22) {
            articleDB = Firebase.database.reference.child("verb").child("v_people_job")
        }
        if(result==23) {
            articleDB = Firebase.database.reference.child("verb").child("v_place_position")
        }
        if(result==24) {
            articleDB = Firebase.database.reference.child("verb").child("v_shape_color")
        }
        if(result==25) {
            articleDB = Firebase.database.reference.child("verb").child("v_weather_time")
        }
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
        //val recyclerView = requireView().findViewById(R.id.recycler_view) as RecyclerView
        //recyclerView.addItemDecoration(DividerItemDecoration(requireView().context, 0)) //리사이클러뷰 가로
        //recyclerView.addItemDecoration(DividerItemDecoration(requireView().context, 1)) //리사이클러뷰 세로
        gridAdapter.notifyDataSetChanged() // view 를 다시 그림;
        val mainAct = activity as MainActivity
        mainAct.hideBottomNavi(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { // landscape
            binding!!.recyclerView.layoutManager = GridLayoutManager(context,4) // 가로모드
        }
        else { // portrait
            binding!!.recyclerView.layoutManager = GridLayoutManager(context, 3) // 세로모드
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        articleDB.removeEventListener(listener)
        // 네비게이션바 숨김
        val mainAct = activity as MainActivity
        mainAct.hideBottomNavi(false)
    }

}