package com.example.bfaa.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bfaa.R
import com.example.bfaa.data.model.User
import com.example.bfaa.databinding.ActivityMainBinding
import com.example.bfaa.ui.detail.DetailActivity
import com.example.bfaa.ui.favorite.FavoriteActivity
import com.example.bfaa.ui.mode.ModeActivity
import com.example.bfaa.ui.mode.ModeViewModel
import com.example.bfaa.ui.mode.SettingPreferences
import com.example.bfaa.ui.mode.ViewModelFactory
import com.example.bfaa.ui.mode.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var modeViewModel: ModeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.title = "Github User's Search"

        val pref = SettingPreferences.getInstance(application.dataStore)

        modeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[ModeViewModel::class.java]

        modeViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        adapter = UserAdapter()
        adapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_URL, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
        }

        binding.btnSearch.setOnClickListener {
            searchUser()
        }

        binding.etQuery.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchUser()
                true
            } else {
                false
            }
        }

        viewModel.getSearchUser().observe(this) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }

        if (savedInstanceState == null) {
            showLoading(true)
            viewModel.setSearchUser("Arif")
        } else {
            // Restore the search query and perform the search
            val savedQuery = savedInstanceState.getString(SEARCH_QUERY_KEY, "")
            binding.etQuery.setText(savedQuery)
            searchUser()
        }
    }

    private fun searchUser() {
        val query = binding.etQuery.text.toString()
        if (query.isEmpty()) {
            showLoading(true)
            viewModel.setSearchUser("Arif")
            return
        }
        showLoading(true)
        viewModel.setSearchUser(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current search query in the outState bundle
        outState.putString(SEARCH_QUERY_KEY, binding.etQuery.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore the search query from the savedInstanceState bundle and perform the search
        val savedQuery = savedInstanceState.getString(SEARCH_QUERY_KEY, "")
        binding.etQuery.setText(savedQuery)
        searchUser()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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

    companion object {
        private const val SEARCH_QUERY_KEY = "search_query"
    }
}
