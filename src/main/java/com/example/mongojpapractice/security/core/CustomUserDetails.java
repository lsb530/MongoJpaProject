//package com.example.mongojpapractice.security.core;
//
//import com.aibizon.aitax.businesslogic.security.document.Role;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//import javax.validation.constraints.NotNull;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import lombok.extern.slf4j.Slf4j;
//import org.bson.types.ObjectId;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//@Getter
//@Setter
//@ToString
//@Builder
//@Slf4j
//public class CustomUserDetails implements UserDetails {
//    /**
//     * 도큐먼트 기본키
//     */
//    @NotNull
//    private ObjectId id;
//    /**
//     * 이메일(로그인의 아이디)
//     */
//    @NotNull
//    private String email;
//
//    /**
//     * 로그인에 사용되는 암호(bcrypt 함수의 해싱값을 저장한다)
//     */
//    @NotNull
//    private String password;
//
//    /**
//     * 계정 활성화 여부
//     */
//    @NotNull
//    @Builder.Default()
//    private boolean isEnabled = true;
//
//    /**
//     * 권한 목록
//     */
//    @NotNull
//    @Builder.Default()
//    private List<Role> role = new ArrayList<>();
//
//    /**
//     * 소속 리소스 목록
//     */
//    @NotNull
//    @Builder.Default()
//    List<HashMap<String, Object>> resources = new ArrayList<>();
//
//    @Override
//    public List<? extends GrantedAuthority> getAuthorities() {
//        return this.role.stream()
//            .filter(data -> data.getName().startsWith("ROLE_"))
//            .map(data -> new SimpleGrantedAuthority(data.getName()))
//            .collect(Collectors.toList());
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return isEnabled;
//    }
//}
