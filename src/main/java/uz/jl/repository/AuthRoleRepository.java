package uz.jl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.jl.domains.AuthRole;
import uz.jl.domains.AuthUser;

import java.util.Optional;

public interface AuthRoleRepository extends JpaRepository<AuthRole, Long> {
    @Query("select t from AuthRole t where t.code = :code")
    AuthRole findByCode(@Param("code") String code);
}
