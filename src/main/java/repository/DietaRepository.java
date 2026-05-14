package repository;

import model.Dieta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DietaRepository extends JpaRepository<Dieta, Long> {
    Optional<Dieta> findByMiembroId(Long miembroId);
    boolean existsByMiembroId(Long miembroId);
}
