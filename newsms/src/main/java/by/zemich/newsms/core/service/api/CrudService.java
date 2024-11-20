package by.zemich.newsms.core.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CrudService<T, I> {

    T save(T t);

    Optional<T> findById(I id);

    void deleteById(I id);

    Page<T> findAll(Pageable pageable);

    boolean existsById(I id);

}
