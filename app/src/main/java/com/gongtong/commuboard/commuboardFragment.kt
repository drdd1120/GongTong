package com.gongtong.commuboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gongtong.GridAdapter
import com.gongtong.GridData
import com.gongtong.R
import com.gongtong.SignUpActivity
import com.gongtong.databinding.FragmentCommuboardBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class commuboardFragment : Fragment(R.layout.fragment_commuboard) {
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

            val mLayoutManager = GridLayoutManager(activity,3)
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
        val fragmentCommuboardBinding = FragmentCommuboardBinding.bind(view)
        binding = fragmentCommuboardBinding

        gridList.clear() //리스트 초기화;

        initDB()

        initArticleAdapter()

        initArticleRecyclerView()

        initListener()
    }

    private fun initDB() {
        articleDB = Firebase.database.reference.child("represent").child("r_noun")// 디비 가져오기;
    }

    private fun initArticleAdapter() {
        gridAdapter = GridAdapter { GridData ->
            Intent(activity, SignUpActivity()::class.java).apply {
                putExtra("title", GridData.name)
                putExtra("imageurl", GridData.image_url)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run { context?.startActivity(this) }
        }
    }
    private fun initArticleRecyclerView() {
        // activity 일 때는 그냥 this 로 넘겼지만 (그자체가 컨텍스트라서) 그러나
        // 프레그 먼트의 경우에는 아래처럼. context
        binding ?: return
        binding!!.recyclerView.layoutManager = GridLayoutManager(context,3)
        binding!!.recyclerView.adapter = gridAdapter
    }

    private fun initListener() {
        articleDB.addChildEventListener(listener)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val recyclerView = requireView().findViewById(R.id.recycler_view) as RecyclerView
        //recyclerView.addItemDecoration(DividerItemDecoration(requireView().context, 0))
        //recyclerView.addItemDecoration(DividerItemDecoration(requireView().context, 1))
        gridAdapter.notifyDataSetChanged() // view 를 다시 그림;

    }

    override fun onDestroy() {
        super.onDestroy()
        articleDB.removeEventListener(listener)
    }
}