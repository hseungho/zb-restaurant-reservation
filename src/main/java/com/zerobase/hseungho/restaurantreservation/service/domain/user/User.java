package com.zerobase.hseungho.restaurantreservation.service.domain.user;

import com.zerobase.hseungho.restaurantreservation.global.util.Generator;
import com.zerobase.hseungho.restaurantreservation.global.util.SeoulDateTime;
import com.zerobase.hseungho.restaurantreservation.service.domain.base.BaseAuditingEntity;
import com.zerobase.hseungho.restaurantreservation.service.type.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseAuditingEntity implements UserDetails {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private final String id = Generator.generateUUID();
    @Column(name = "user_id", unique = true, updatable = false)
    private String userId;
    @Column(name = "password")
    private String password;
    @Column(name = "nickname", unique = true)
    private String nickname;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type;
    @Column(name = "logged_in_at")
    private LocalDateTime loggedInAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Transient
    private Boolean isOnLoginRequest = false;

    private User(String userId, String password, String nickname, UserType type) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.type = type;
    }

    public static User createDefaultEntity(String userId, String password, String nickname) {
        return new User(userId, password, nickname, UserType.ROLE_CUSTOMER);
    }

    public boolean isResigned() {
        return deletedAt != null;
    }

    public void login() {
        loggedInAt = SeoulDateTime.now();
        isOnLoginRequest = true;
    }

    public void setType(UserType type) {
        if (type == null) {
            return;
        }
        this.type = type;
    }

    public boolean isPartner() {
        return type == UserType.ROLE_PARTNER;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void preUpdate() {
        if (isOnLoginRequest) {
            isOnLoginRequest = false;
            return;
        }
        super.preUpdate();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(type.name()));
    }

    @Override
    public String getUsername() {
        return userId;
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
        return true;
    }

}
