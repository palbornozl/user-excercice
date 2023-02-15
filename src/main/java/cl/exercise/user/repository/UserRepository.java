package cl.exercise.user.repository;

import cl.exercise.user.entities.UserEntity;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  UserEntity findByEmail(@Param("email") String email);

  Optional<UserEntity> findById(@Param("id") UUID id);

  @Modifying
  @Query(
      value =
          "UPDATE EXERCISE.TBL_USER "
              + " SET "
              + " name = :name , "
              + " email = :email , "
              + " password = :password , "
              + " token = :token , "
              + " modified = :modified "
              + "WHERE id = :id",
      nativeQuery = true)
  void updateUserInfo(
      @Param("id") UUID idUser,
      @Param("name") String userName,
      @Param("email") String userEmail,
      @Param("password") String userPassword,
      @Param("token") String userToken,
      @Param("modified") Timestamp modified);

  @Transactional
  @Modifying
  @Query(
      value = "UPDATE EXERCISE.TBL_USER SET last_login = :lastlogin WHERE id = :id",
      nativeQuery = true)
  void updateUserLastLogin(@Param("id") UUID idUser, @Param("lastlogin") Timestamp lastLogin);
}
