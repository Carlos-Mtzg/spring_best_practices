package gregdev.spring_enterprises.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import gregdev.spring_enterprises.model.EnterpriseModel;

public interface IEnterpriseRepository extends JpaRepository<EnterpriseModel, Integer> {
    Optional<EnterpriseModel> findByUuid(UUID uuid);
}
