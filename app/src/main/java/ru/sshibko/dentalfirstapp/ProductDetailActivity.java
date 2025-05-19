package ru.sshibko.dentalfirstapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Locale;

import ru.sshibko.dentalfirstapp.model.Product;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Product product = getIntent().getParcelableExtra("product");
        if (product != null) {
            displayProductDetails(product);
        }
    }

    private void displayProductDetails(Product product) {
        ImageView imageView = findViewById(R.id.productDetailImageView);
        TextView nameTextView = findViewById(R.id.productDetailNameTextView);
        TextView priceTextView = findViewById(R.id.productDetailPriceTextView);
        TextView descriptionTextView = findViewById(R.id.productDetailDescriptionTextView);

        Glide.with(this)
                .load(product.getImage())
                .into(imageView);

        nameTextView.setText(product.getName());
        priceTextView.setText(String.format(Locale.getDefault(), "%.2f ₽", product.getPrice()));
        descriptionTextView.setText(product.getDescription());
    }
}
