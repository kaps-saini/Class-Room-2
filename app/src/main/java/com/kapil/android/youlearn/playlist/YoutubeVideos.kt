package com.kapil.android.youlearn.playlist

import android.app.Application
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kapil.android.youlearn.R
import com.kapil.android.youlearn.api.db.ItemDataBase
import com.kapil.android.youlearn.databinding.FragmentYoutubeVideosBinding
import com.kapil.android.youlearn.models.search.Item
import com.kapil.android.youlearn.network.App
import com.kapil.android.youlearn.network.ConnectionManager
import com.kapil.android.youlearn.network.Resource
import com.kapil.android.youlearn.repository.Repository
import retrofit2.Response

class YoutubeVideos : Fragment(), VideoAdapter.OnItemClickListener {

    private lateinit var binding: FragmentYoutubeVideosBinding
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var playListViewModel: PlayListViewModel
    private lateinit var token : String
    private lateinit var frameLayout: FrameLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_youtube_videos, container, false
        )

        videoRecyclerView = binding.videosRecyclerView
        frameLayout = binding.frameLayout
        progressBar = binding.progressBar

        val repository = Repository(ItemDataBase(activity as Context))
        val playListViewModelFactory = PlayListViewModelFactory(repository)
        playListViewModel =
            ViewModelProvider(this, playListViewModelFactory).get(PlayListViewModel::class.java)

            playListResponse()

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val searchQ = query?.trim().toString()
                    val options: HashMap<String, String> = HashMap()
                    options["part"] = "snippet"
                    options["channelType"] = "Any"
                    options["regionCode"] = "in"
                    options["maxResults"] = "50"
                    options["relevanceLanguage"] = "hi"
                    options["type"] = "video"
                    options["videoCaption"] = "any"
                    options["videoCategoryId"] = "27"
                    options["videoType"] = "any"
                    options["q"] = searchQ
                    options["key"] = "AIzaSyAlISFY1uFzPGfBoJP8ewrf5HAYtKCtE4U"

                    playListViewModel.getPlayList(options)

                    recyclerView()

                    playListViewModel.listResponse.observe(viewLifecycleOwner, Observer { response ->
                            when(response){
                                is Resource.Success -> {
                                    hideProgressBar()
                                    response.data?.let { apiResponse ->
                                        videoAdapter.differ.submitList(apiResponse.items)
                                    }
                                }
                                is Resource.Error -> {
                                    hideProgressBar()
                                    binding.txtNoInternet.isVisible = true
                                    binding.imgNoInternet.isVisible = true
                                    response.message?.let { message ->
                                        Toast.makeText(activity as Context, message, Toast.LENGTH_LONG).show()
                                    }
                                }
                                is Resource.Loading ->{
                                    frameLayout.isVisible = true
                                    progressBar.isVisible = true
                                }
                            }
                        })

                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        return binding.root
    }

    private fun playListResponse(){
        val options : HashMap<String, String> = HashMap()
        options["part"] = "snippet"
        options["channelType"] = "Any"
        options["regionCode"] = "in"
        options["maxResults"] = "50"
        options["relevanceLanguage"] = "hi"
        options["type"] = "video"
        options["videoCaption"] = "any"
        options["videoCategoryId"] = "27"
        options["videoType"] = "any"
        options["key"] = "AIzaSyAlISFY1uFzPGfBoJP8ewrf5HAYtKCtE4U"

        playListViewModel.getPlayList(options)

        recyclerView()

        playListViewModel.listResponse.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { apiResponse ->
                        videoAdapter.differ.submitList(apiResponse.items)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    binding.imgNoInternet.isVisible = true
                    binding.txtNoInternet.isVisible = true
                    response.message?.let { message ->
                        Toast.makeText(activity as Context, message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading ->{
                    frameLayout.isVisible = true
                    progressBar.isVisible = true
                    Toast.makeText(activity as Context, "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onClick(items: Item) {
        findNavController().navigate(YoutubeVideosDirections.actionYoutubeVideosToYouTubePlayer(items))
    }

    private fun hideProgressBar(){
        frameLayout.isVisible = false
        progressBar.isVisible = false
    }

    private fun recyclerView() {
        videoAdapter = VideoAdapter(this)
        videoRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(activity as Context)
        }
    }
}