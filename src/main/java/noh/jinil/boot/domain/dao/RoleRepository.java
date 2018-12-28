package noh.jinil.boot.domain.dao;

import noh.jinil.boot.domain.model.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
	RoleEntity findByAuthority(String authority);
}
