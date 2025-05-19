package ru.sshibko.dentalfirstapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.sshibko.dentalfirstapp.api.ApiClient;
import ru.sshibko.dentalfirstapp.model.Product;
import ru.sshibko.dentalfirstapp.model.SearchResponse;
import ru.sshibko.dentalfirstapp.service.ApiService;
import ru.sshibko.dentalfirstapp.service.ProductsAdapter;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView resultsRecyclerView;
    private ProgressBar progressBar;
    private TextView emptyResultsTextView;

    private ProductsAdapter adapter;
    private List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        setupRecycleView();
        setupListeners();
    }

    private void initViews() {
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyResultsTextView = findViewById(R.id.emptyResultsTextView);
    }

    private void setupRecycleView() {
        adapter = new ProductsAdapter(products, product -> {
            Intent intent = new Intent(SearchActivity.this,
                    ProductDetailActivity.class);
            intent.putExtra("product", (CharSequence) product);
            startActivity(intent);
        });
    }

    private void setupListeners() {
        searchButton.setOnClickListener(v -> performSearch());

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Введите поисковый запрос", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

        searchProducts(query);
    }

    private void searchProducts(String query) {
        progressBar.setVisibility(View.VISIBLE);
        resultsRecyclerView.setVisibility(View.GONE);
        emptyResultsTextView.setVisibility(View.GONE);

        ApiService apiService = ApiClient.getApiService();
        Call<SearchResponse> call = apiService.searchProducts(query);

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Product> foundProducts = response.body().getProducts();

                    if (foundProducts.isEmpty()) {
                        emptyResultsTextView.setText("Ничего не найдено");
                        emptyResultsTextView.setVisibility(View.VISIBLE);
                        resultsRecyclerView.setVisibility(View.GONE);
                    } else {
                        adapter.updateProducts(foundProducts);
                        resultsRecyclerView.setVisibility(View.VISIBLE);
                        emptyResultsTextView.setVisibility(View.GONE);
                    }
                } else {
                    showError("Ошибка при получении данных");
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Ошибка сети: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        emptyResultsTextView.setText(message);
        emptyResultsTextView.setVisibility(View.VISIBLE);
        resultsRecyclerView.setVisibility(View.GONE);
    }
}