package com.example.android.nextmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nextmovies.adapters.MovieAdapter;
import com.example.android.nextmovies.architecture.MainViewModel;
import com.example.android.nextmovies.network.ApiFactory;
import com.example.android.nextmovies.pojo.Movie;
import com.example.android.nextmovies.pojo.MovieResponse;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;
    private MovieAdapter adapter;
    private Menu optionMenu;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textViewPopular, textViewTopRated;
    private SwitchCompat switchSort;
    private LinearLayout linearTopPanel;
    private LinearLayout linearSearch;
    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        initViews();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        observeViewModel();
        setClickListeners();
    }

    private void setClickListeners() {
        adapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                viewModel.loadMovies();
            }
        });
        adapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                startActivity(DetailsActivity.newIntent(MainActivity.this, movie));
            }
        });
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchSorting(isChecked);
            }
        });
        textViewPopular.setOnClickListener(v -> {
            if (!switchSort.isChecked()) return;
            switchSort.setChecked(false);
        });
        textViewTopRated.setOnClickListener(v -> {
            if (switchSort.isChecked()) return;
            switchSort.setChecked(true);
        });
        switchSort.setChecked(false);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                viewModel.startSearch(v.getText().toString());
                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }
        });
    }

    private void observeViewModel() {
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovies(movies);
            }
        });
        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getSort().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                viewModel.loadMovies();
            }
        });
        viewModel.getIsSearching().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSearching) {
                if (isSearching) {
                    linearTopPanel.setVisibility(View.GONE);
                    linearSearch.setVisibility(View.VISIBLE);
                } else {
                    linearTopPanel.setVisibility(View.VISIBLE);
                    linearSearch.setVisibility(View.GONE);
                }
            }
        });
    }

    private void switchSorting(boolean isChecked) {
        viewModel.switchSorting(isChecked);
        if (isChecked) {
            textViewPopular.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
            textViewTopRated.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pink));
        } else {
            textViewPopular.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.pink));
            textViewTopRated.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.pbar_loading);
        textViewPopular = findViewById(R.id.tv_popular);
        textViewTopRated = findViewById(R.id.tv_top_rated);
        switchSort = findViewById(R.id.switch_sort);
        linearTopPanel = findViewById(R.id.linear_top_panel);
        linearSearch = findViewById(R.id.linear_search);
        editSearch = findViewById(R.id.edit_search);
        recyclerView = findViewById(R.id.recycler_view_movies);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        optionMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_favs:
                startActivity(FavoritesActivity.newIntent(this));
                break;
            case R.id.item_search:
                item.setVisible(false);
                optionMenu.findItem(R.id.item_main).setVisible(true);
                editSearch.setText("");
                viewModel.preStartSearch();
                break;
            case R.id.item_main:
                item.setVisible(false);
                optionMenu.findItem(R.id.item_search).setVisible(true);
                viewModel.stopSearch();
        }
        return super.onOptionsItemSelected(item);
    }
}