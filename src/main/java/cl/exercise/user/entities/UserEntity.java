package cl.exercise.user.entities;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tbl_user", schema = "exercise")
@IdClass(UserEntity.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@SelectBeforeUpdate
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID", updatable = false, nullable = false)
    @ColumnDefault("random_uuid()")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Timestamp createdAt;

    @Column(name = "modified")
    private Timestamp modifiedAt;

    @Column(name = "last_login", nullable = false)
    private Timestamp lastLogin;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) {
            createdAt = Timestamp.from(Instant.now());
        }
        if (this.lastLogin == null) {
            this.lastLogin = Timestamp.from(Instant.now());
            this.modifiedAt = Timestamp.from(Instant.now());
        }
    }
}
