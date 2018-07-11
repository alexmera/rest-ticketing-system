package spring.ticketing.repositories;

import java.util.List;
import java.util.Optional;

public interface AppDao<PK, T> {

  T create(T entity);

  T getOne(PK id);

  Optional<T> findById(PK id);

  List<T> all();

  T update(T entity);

  T delete(PK id);
}
