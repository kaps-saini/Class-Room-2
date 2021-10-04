package com.kapil.android.youlearn.youtubeplayer

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kapil.android.youlearn.R
import com.kapil.android.youlearn.api.db.ItemDataBase
import com.kapil.android.youlearn.databinding.FragmentYTBinding
import com.kapil.android.youlearn.models.search.Item
import com.kapil.android.youlearn.network.App
import com.kapil.android.youlearn.network.ConnectionManager
import com.kapil.android.youlearn.network.Resource
import com.kapil.android.youlearn.playlist.VideoAdapter
import com.kapil.android.youlearn.repository.Repository
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import retrofit2.Response

class YouTubePlayer : Fragment(), VideoAdapter.OnItemClickListener {

    private lateinit var youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
    private lateinit var youTubePlayerViewModel: YouTubePlayerViewModel
    private lateinit var id: String
    private lateinit var binding : FragmentYTBinding
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var playerRecyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var token: String
    private lateinit var frameLayout: FrameLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            // Inflate the layout for this fragment
             binding = DataBindingUtil.inflate(inflater, R.layout.fragment_y_t, container, false)

            val bundle: YouTubePlayerArgs by navArgs()
            val items = bundle.item
            id = items.id.searchVideoId.toString()
            val channelTitle = items.snippet.channelTitle
            val publishedAt = items.snippet.publishedAt
            val title = items.snippet.title

            binding.lifecycleOwner = this

            youTubePlayer = binding.youTubePlayerView
            playerRecyclerView = binding.playerRecyclerView
            frameLayout = binding.frameLayout
            progressBar = binding.progressBar

            binding.channelTitle.text = channelTitle
            binding.title.text = title
            binding.publishedAt.text = publishedAt
            var tokenPage = ""

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
            options["relatedToVideoId"] = id
            options["key"] = "AIzaSyAlISFY1uFzPGfBoJP8ewrf5HAYtKCtE4U"

            val repository = Repository(ItemDataBase(activity as Context))

            val playerFactory = YTPViewModelFactory(repository)
            youTubePlayerViewModel =
                ViewModelProviders.of(this, playerFactory).get(YouTubePlayerViewModel::class.java)
            youTubePlayerViewModel.getV3(options)

                youTubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        if (id != null) {
                            youTubePlayer.loadVideo(id, 0F)
                        }
                    }
                })

                youTubePlayer.addFullScreenListener(object : YouTubePlayerFullScreenListener {
                    override fun onYouTubePlayerEnterFullScreen() {
                        youTubePlayer.isFullScreen()
                    }

                    override fun onYouTubePlayerExitFullScreen() {
                        youTubePlayer.exitFullScreen()
                    }
                })

                recyclerView()

                youTubePlayerViewModel.responseV3.observe(viewLifecycleOwner, Observer { response ->
                    when(response){
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { apiResponse ->
                                videoAdapter.differ.submitList(apiResponse.items)
                            }
                        }
                        is Resource.Error ->{
                            hideProgressBar()
                            binding.txtNoInternet.isVisible = true
                            binding.imgnoInternet.isVisible = true
                            response.message?.let { message ->
                                Log.e("logMessage", "An error occur: $message")
                                Toast.makeText(activity as Context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        is Resource.Loading ->{
                            frameLayout.isVisible = true
                            progressBar.isVisible = true
                        }
                    }
                })

                binding.save.setOnClickListener {
                    youTubePlayerViewModel.addVideos(items)
                    binding.save.visibility = INVISIBLE
                    binding.txtReadings.visibility = INVISIBLE
                    binding.added.visibility = VISIBLE
                    binding.txtAdded.visibility = VISIBLE
                    view?.let { it1 ->
                        Snackbar.make(
                            it1, "Saved successfully in Readings Tab",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    binding.share.setOnClickListener {
                        val shareIntent = Intent().apply {
                            this.action = Intent.ACTION_SEND
                            val id = items.id.searchVideoId
                            this.putExtra("Sharing Video Id", "www.youtube.com/${id}")
                            this.type = "text/plain"
                        }
                        startActivity(shareIntent)
                    }
                }

        return binding.root
    }

    private fun recyclerView() {
        videoAdapter = VideoAdapter(this)
        playerRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(activity as Context)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayer.release()
    }

    override fun onClick(items: Item) {
        findNavController().navigate(YouTubePlayerDirections.actionYouTubePlayerSelf(items))
    }

    private fun hideProgressBar(){
        frameLayout.isVisible = false
        progressBar.isVisible = false
    }
}