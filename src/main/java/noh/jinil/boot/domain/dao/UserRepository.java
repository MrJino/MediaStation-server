package noh.jinil.boot.domain.dao;

import noh.jinil.boot.domain.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByUsername(String username);
}