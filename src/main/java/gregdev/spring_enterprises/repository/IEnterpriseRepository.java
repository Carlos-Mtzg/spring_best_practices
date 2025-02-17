package gregdev.spring_enterprises.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gregdev.spring_enterprises.model.EnterpriseModel;

public interface IEnterpriseRepository extends JpaRepository<EnterpriseModel, Integer> {

}
