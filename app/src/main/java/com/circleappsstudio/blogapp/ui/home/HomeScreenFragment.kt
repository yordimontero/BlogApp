package com.circleappsstudio.blogapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.circleappsstudio.blogapp.R
import com.circleappsstudio.blogapp.data.model.Post
import com.circleappsstudio.blogapp.databinding.FragmentHomeScreenBinding
import com.circleappsstudio.blogapp.ui.home.adapter.HomeScreenAdapter
import com.google.firebase.Timestamp

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeScreenBinding.bind(view)

        val postList = listOf(
            Post(
                "https://scontent.fsjo5-1.fna.fbcdn.net/v/t1.6435-1/p200x200/100051499_1885735311563260_4167003418340098048_n.jpg?_nc_cat=106&ccb=1-3&_nc_sid=7206a8&_nc_ohc=UOOOVG1hm_cAX_9sh9v&_nc_ht=scontent.fsjo5-1.fna&oh=0c8c2116cb984625b5d74cd7c6cefbc8&oe=60FE150D",
                "Yordi Montero",
                Timestamp.now(),
                "https://scontent.fsjo5-1.fna.fbcdn.net/v/t1.6435-9/220432072_2294711840665603_1517550066817866975_n.jpg?_nc_cat=107&ccb=1-3&_nc_sid=8bfeb9&_nc_ohc=g0t5J24afAoAX-Xkree&_nc_ht=scontent.fsjo5-1.fna&oh=2cd40d2839c9ef66094a5e02d64017d4&oe=60FCC2C4"
            )
        )

        binding.rvHome.adapter = HomeScreenAdapter(
            postList
        )

    }

}