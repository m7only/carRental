package m7.only.carrental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import m7.only.carrental.entity.user.PersonalityData;
import m7.only.carrental.entity.user.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * Сущность пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usrs")
public class User implements UserDetails {

    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Логин пользователя.
     */
    @NotBlank
    @Column(name = "username")
    private String username;

    /**
     * Пароль пользователя.
     */
    @NotBlank
    @Column(name = "password")
    private String password;

    /**
     * Дата регистрации пользователя.
     */
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * Дата измения данных пользователя.
     */
    @Column(name = "updated")
    private LocalDateTime updated;

    /**
     * Флаг удаления пользователя.
     */
    @Column(name = "active")
    private boolean active;

    /**
     * {@linkplain PersonalityData Персональные данные} пользователя.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "personality_data_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PersonalityData personalityData;

    /**
     * {@linkplain Role Роли} пользователя.
     */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Role> roles;

    /**
     * {@linkplain Order Заказы} пользователя.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Order> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
