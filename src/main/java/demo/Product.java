package demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String sku;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", sku='").append(sku).append('\'');
        sb.append('}');
        return sb.toString();
    }

    Product() { // why JPA why??
    }

    public Product(String sku) {

        this.sku = sku;
    }

    public Long getId() {

        return id;
    }

    public String getSku() {
        return sku;
    }
}
