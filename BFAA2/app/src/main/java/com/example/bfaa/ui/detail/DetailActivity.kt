package com.example.bfaa.ui.detail

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.example.bfaa.R
import com.example.bfaa.databinding.ActivityDetailBinding
import com.example.bfaa.ui.favorite.FavoriteActivity
import com.example.bfaa.ui.mode.ModeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import loadImageWithGlide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailUserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showLoading(true)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        if (username != null) {
            viewModel.setUserDetail(this, username)
            viewModel.getUserDetail().observe(this) {
                if (it != null) {
                    with(binding) {
                        constraintDetail.visibility = View.VISIBLE
                        tvName.text = it.name
                        tvUsername.text = it.login
                        tvFollowers.text =
                            resources.getString(R.string.data_followers, it.followers)
                        tvFollowing.text =
                            resources.getString(R.string.data_followers, it.following)
                        ivAvatar.loadImageWithGlide(it.avatarUrl)
                    }

                    showLoading(false)

                    var isChecked = false
                    CoroutineScope(Dispatchers.IO).launch {
                        val count = viewModel.checkUser(id)
                        withContext(Dispatchers.Main) {
                            Log.d("debug", id.toString())
                            Log.d("debug", count.toString())
                            if (count != null) {
                                isChecked = if (count > 0) {
                                    binding.toggleFavorite.setImageDrawable(
                                        AppCompatResources.getDrawable(this@DetailActivity, R.drawable.ic_heart_fill)
                                    )
                                    true
                                } else {
                                    binding.toggleFavorite.setImageDrawable(
                                        AppCompatResources.getDrawable(this@DetailActivity, R.drawable.ic_heart_outline)
                                    )
                                    false
                                }
                            }
                        }
                    }

                    binding.toggleFavorite.setOnClickListener {
                        isChecked = !isChecked
                        if (isChecked) {
                            binding.toggleFavorite.setImageDrawable(
                                AppCompatResources.getDrawable(this, R.drawable.ic_heart_fill)

                            )
                            viewModel.addToFavorite(username, id, avatarUrl)

                        } else {
                            binding.toggleFavorite.setImageDrawable(
                                AppCompatResources.getDrawable(this, R.drawable.ic_heart_outline)
                            )
                            viewModel.removeFromFavorite(id)
                        }
                    }


                    val sectionPagerAdapter =
                        SectionPagerAdapter(this, this, binding.tabs, binding.viewPager, bundle)
                    binding.viewPager.adapter = sectionPagerAdapter
                    sectionPagerAdapter.setupTabs()
                }
            }
        } else {
            // Handle the case when the username is null
            showLoading(false)
            Toast.makeText(this, "Username not found.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_change_settings -> {
                Intent(this, ModeActivity::class.java).also {
                    startActivity(it)
                }
                true
            }
            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }
}