package com.example.bfaa.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.bfaa.R
import com.example.bfaa.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_USERNAME = "extra_username"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)

        if (username != null) {
            viewModel.setUserDetail(username)
            viewModel.getUserDetail().observe(this, {
                if (it != null) {
                    binding.tvName.text = it.name
                    binding.tvUsername.text = it.login
                    binding.tvFollowers.text = "${it.followers} Followers"
                    binding.tvFollowing.text = "${it.following} Following"
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(binding.ivAvatar)
                }
            })
        } else {
            // Handle the case when the username is null, e.g., show an error message or finish the activity.
            // You might add a toast message or a Snackbar to inform the user about the missing username.
            // For example:
            Toast.makeText(this, "Username not found.", Toast.LENGTH_SHORT).show()
            finish()
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, this, binding.tabs, binding.viewPager, bundle)
        binding.viewPager.adapter = sectionPagerAdapter
        sectionPagerAdapter.setupTabs()
    }


    //val SectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
    //    binding.apply {
    //        viewPager.adapter = SectionPagerAdapter
    //        tabs.setupWithViewPager(viewPager)
    //    }
    //}
}