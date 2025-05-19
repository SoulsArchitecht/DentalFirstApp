package ru.sshibko.dentalfirstapp.model;

import java.util.List;

public class SearchResponse {
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
