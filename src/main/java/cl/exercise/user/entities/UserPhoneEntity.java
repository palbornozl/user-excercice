package cl.exercise.user.entities;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tbl_user_phone", schema = "exercise")
@IdClass(UserPhoneEntity.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserPhoneEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID", updatable = false, nullable = false)
    @ColumnDefault("random_uuid()")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "id_user", updatable = false, nullable = false)
    @Type(type = "uuid-char")
    @NotNull
    private UUID idUser;

    @Column(name = "number")
    @NotNull
    private Integer number;

    @Column(name = "city_code")
    @NotNull
    private Integer cityCode;

    @Column(name = "country_code")
    @NotNull
    private Integer countryCode;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) {
            createdAt = Timestamp.from(Instant.now());
        }
        if (this.updatedAt == null) {
            updatedAt = Timestamp.from(Instant.now());
        }
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
