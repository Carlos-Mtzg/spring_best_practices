package gregdev.spring_enterprises.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import gregdev.spring_enterprises.model.EnterpriseModel;
import gregdev.spring_enterprises.repository.IEnterpriseRepository;
import jakarta.transaction.Transactional;

@Service
@Primary
@Transactional
public class EnterpriseService {
    private final IEnterpriseRepository repository;

    EnterpriseService(IEnterpriseRepository repository) {
        this.repository = repository;
    }

    public List<EnterpriseModel> getAll() {
        return this.repository.findAll(Sort.by("id").descending());
    }

    public EnterpriseModel findById(Integer id) {
        Optional<EnterpriseModel> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public void save(EnterpriseModel enterprise) {
        this.repository.save(enterprise);
    }

    public void delete(Integer id) {
        this.repository.deleteById(id);
    }
}
