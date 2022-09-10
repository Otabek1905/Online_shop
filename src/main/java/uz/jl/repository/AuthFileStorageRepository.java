package uz.jl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.jl.domains.AuthRole;
import uz.jl.domains.Uploads;

public interface AuthFileStorageRepository extends JpaRepository<Uploads, Long> {
}
