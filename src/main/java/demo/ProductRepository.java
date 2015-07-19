package demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;

public interface ProductRepository extends JpaRepository<Product, String> {

    @RestResource(path = "by-sku")
    Collection<Product> findBySku(@Param("sku") String sku);
}
