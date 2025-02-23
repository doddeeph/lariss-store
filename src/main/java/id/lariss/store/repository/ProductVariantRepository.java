package id.lariss.store.repository;

import id.lariss.store.domain.ProductVariant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductVariant entity.
 */
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    default Optional<ProductVariant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductVariant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductVariant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productVariant from ProductVariant productVariant left join fetch productVariant.product",
        countQuery = "select count(productVariant) from ProductVariant productVariant"
    )
    Page<ProductVariant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productVariant from ProductVariant productVariant left join fetch productVariant.product")
    List<ProductVariant> findAllWithToOneRelationships();

    @Query("select productVariant from ProductVariant productVariant left join fetch productVariant.product where productVariant.id =:id")
    Optional<ProductVariant> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        value = "WITH pv AS (" +
        "SELECT pv.* " +
        "FROM product_variant pv " +
        "JOIN product p ON p.id = pv.product_id " +
        "WHERE p.id IN :productIds)" +
        "SELECT * from pv",
        nativeQuery = true
    )
    List<ProductVariant> findAllByProductIds(@Param("productIds") List<Long> productIds);

    @Query(
        value = "WITH pv AS (" +
        "SELECT pv.* " +
        "FROM product_variant pv " +
        "JOIN product p ON p.id = pv.product_id " +
        "WHERE p.id IN :productIds)" +
        "SELECT * from pv " +
        "WHERE pv.price = (SELECT MIN(pv.price) FROM pv)",
        nativeQuery = true
    )
    List<ProductVariant> findCheapestByProductIds(@Param("productIds") List<Long> productIds);

    @Query(
        value = "WITH pv AS (" +
        "SELECT pv.* " +
        "FROM product_variant pv " +
        "JOIN product p ON p.id = pv.product_id " +
        "WHERE p.id IN :productIds)" +
        "SELECT * from pv " +
        "WHERE pv.price = (SELECT MAX(pv.price) FROM pv)",
        nativeQuery = true
    )
    List<ProductVariant> findMostExpensiveByProductIds(@Param("productIds") List<Long> productIds);

    @Query(
        value = "WITH pv AS (" +
        "SELECT pv.* " +
        "FROM product_variant pv " +
        "JOIN product p ON p.id = pv.product_id " +
        "JOIN category c ON c.id = p.category_id " +
        "WHERE c.id = :categoryId)" +
        "SELECT * from pv " +
        "WHERE pv.price = (SELECT MIN(pv.price) FROM pv)",
        nativeQuery = true
    )
    List<ProductVariant> findCheapestByCategoryId(@Param("categoryId") Long categoryId);

    @Query(
        value = "WITH pv AS (" +
        "SELECT pv.* " +
        "FROM product_variant pv " +
        "JOIN product p ON p.id = pv.product_id " +
        "JOIN category c ON c.id = p.category_id " +
        "WHERE c.id = :categoryId)" +
        "SELECT * from pv " +
        "WHERE pv.price = (SELECT MAX(pv.price) FROM pv)",
        nativeQuery = true
    )
    List<ProductVariant> findMostExpensiveByCategoryId(@Param("categoryId") Long categoryId);
}
