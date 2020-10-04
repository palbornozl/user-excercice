package cl.exercise.user.repository;

import cl.exercise.user.entities.UserPhoneEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhoneEntity, UUID> {

  void deleteByIdUser(@Param("id_user") UUID idUser);

  List<UserPhoneEntity> findByIdUser(@Param("id_user") UUID idUser);
}
