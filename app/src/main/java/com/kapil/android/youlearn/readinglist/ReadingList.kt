package com.kapil.android.youlearn.readinglist

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kapil.android.youlearn.R
import com.kapil.android.youlearn.api.db.ItemDataBase
import com.kapil.android.youlearn.databinding.FragmentReadingListBinding
import com.kapil.android.youlearn.models.search.Item
import com.kapil.android.youlearn.network.App
import com.kapil.android.youlearn.network.ConnectionManager
import com.kapil.android.youlearn.repository.Repository
import com.kapil.android.youlearn.youtubeplayer.YTPViewModelFactory
import com.kapil.android.youlearn.youtubeplayer.YouTubePlayerViewModel

class ReadingList : Fragment(), ReadingAdapter.OnClick {

    private lateinit var viewModel: YouTubePlayerViewModel
    private lateinit var readingRecyclerView: RecyclerView
    private lateinit var readingAdapter : ReadingAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var binding: FragmentReadingListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            // Inflate the layout for this fragment
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reading_list, container,
                false
            )

            readingRecyclerView = binding.readingRecyclerView

            val repository = Repository(ItemDataBase(activity as Context))
            val playerFactory = YTPViewModelFactory( repository)
            viewModel = ViewModelProviders.of(this, playerFactory).get(YouTubePlayerViewModel::class.java)

            setUpRecyclerView()

            val touchHelperListenerCallBack = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val addedVideo = readingAdapter.differ.currentList[position]
                    viewModel.deleteVideos(addedVideo)

                    view?.let { it1 -> Snackbar.make(it1, "Successfully deleted from readings",
                            Snackbar.LENGTH_LONG).apply {
                            setAction("Undo") {
                                viewModel.addVideos(addedVideo)
                                Snackbar.make(it1, "Successfully video added", Snackbar.LENGTH_SHORT).show()
                            }.show()
                        }
                    }
                }

            }

            ItemTouchHelper(touchHelperListenerCallBack).apply {
                attachToRecyclerView(readingRecyclerView)
            }

            viewModel.getAddedVideos().observe(viewLifecycleOwner, Observer { addedList ->
                if (addedList.isEmpty() || null == true){
                    binding.emptyList.isVisible = true
                    binding.progressBar.isVisible = false
                }else{
                    readingAdapter.differ.submitList(addedList)
                    binding.frameLayout.isVisible = false
                    binding.progressBar.isVisible = false
                }
            })
        return binding.root
    }

    private fun setUpRecyclerView() {
        readingAdapter =  ReadingAdapter(this)
        readingRecyclerView.apply {
            adapter = readingAdapter
            linearLayoutManager = LinearLayoutManager(activity as Context)
        }
    }

    override fun onItemClicked(item: Item) {
        findNavController().navigate(ReadingListDirections.actionReadingListToYouTubePlayer(item))
    }
}