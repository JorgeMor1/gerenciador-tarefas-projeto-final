package com.jorge.projeto.tarefas.tarefas_java_jwt.infra.secuity;

import com.jorge.projeto.tarefas.tarefas_java_jwt.model.role.Role;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final Role role;

    public Role getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return Collections.emptyList();
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // Factory method para criar a partir do User
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), user.getRole());
    }
}