package uz.jl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.jl.domains.Product;

public interface ProductRepository extends JpaRepository<Product , Long> {

    @Query("select t from Product t where (lower(t.name) like %:query% or lower(t.color) like %:query% or lower(t.category) like %:query%)")
    Page<Product> findAll(@Param("query") String searchQuery, Pageable pageable);

}
