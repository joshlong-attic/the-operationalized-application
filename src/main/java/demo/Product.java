package demo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Product {
    @Id
    @NotNull
    private String sku;

    @NotNull
    private float price = 0f;

    private String description;

    Product() {
    }

    public Product(String sku, String description, float price) {
        this.sku = sku;
        this.description = description;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public String getSku() {
        return sku;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("sku='").append(sku).append('\'');
        sb.append(", price=").append(price);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
