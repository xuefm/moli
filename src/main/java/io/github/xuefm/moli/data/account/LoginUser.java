package io.github.xuefm.moli.data.account;

import io.github.xuefm.moli.entity.SysAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private SysAccount sysAccount;

    /**
     * 权限
     */
    private List<String> permissions;

    /**
     * 权限
     */
    private List<GrantedAuthority> authorities;

    public LoginUser(SysAccount accountInfo, List<String> permissions) {
        this.sysAccount = accountInfo;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> list = new ArrayList<>();
//        for (String permission : permissions) {
//            list.add(new SimpleGrantedAuthority(permission));
//        }
        if (Objects.nonNull(authorities)) {
            return authorities;
        }
        authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return authorities;
    }

    @Override
    public String getPassword() {
        return sysAccount.getLoginPassword();
    }

    @Override
    public String getUsername() {
        return sysAccount.getLoginName();
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
