package id.lariss.store.repository;

import id.lariss.store.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    default Optional<Product> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Product> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Product> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select product from Product product left join fetch product.category",
        countQuery = "select count(product) from Product product"
    )
    Page<Product> findAllWithToOneRelationships(Pageable pageable);

    @Query("select product from Product product left join fetch product.category")
    List<Product> findAllWithToOneRelationships();

    @Query("select product from Product product left join fetch product.category where product.id =:id")
    Optional<Product> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        value = """
            SELECT *
            FROM product
            WHERE EXISTS (
              SELECT 1
              FROM unnest(:productNames) AS name
              WHERE to_tsvector('english', product_name) @@ plainto_tsquery('english', name)
            )
        """,
        nativeQuery = true
    )
    List<Product> findAllByNameFullText(@Param("productNames") String[] productNames);

    @Query(
        value = """
            SELECT *
            FROM product
            WHERE EXISTS (
              SELECT 1
              FROM unnest(:productNames) AS name
              WHERE LOWER(product_name) LIKE LOWER(CONCAT('%', name, '%'))
            )
        """,
        nativeQuery = true
    )
    List<Product> findAllByNameContainingIgnoreCase(@Param("productNames") String[] productNames);

    @Query(
        "SELECT p FROM Product p WHERE FUNCTION('similarity', p.productName, :productName) > 0.3 ORDER BY FUNCTION('similarity', p.productName, :productName) DESC"
    )
    List<Product> findAllByNameSimilar(@Param("productName") String productName);
}
