//package com.example.mongojpapractice.security.core;
//
//import com.aibizon.aitax.businesslogic.security.document.Role;
//import com.aibizon.aitax.businesslogic.security.document.Role.TARGETTYPE;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.extern.slf4j.Slf4j;
//import org.bson.types.ObjectId;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.ObjectUtils;
//
//@Slf4j
//public class SecurityUtils {
//
//    private SecurityUtils() {
//    }
//
//    /**
//     * 세션 인증 여부
//     *
//     * @return true 인증, false 비인증
//     */
//    public static boolean isAuthenticated() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * 로그인 유저의 ObjectId 반환
//     *
//     * @return Object Id 유저 계정 도큐먼트 Id
//     */
//    public static ObjectId getCurrentLoginObjectId() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        CustomUserDetails springSecurityUser = null;
//        ObjectId userId = null;
//
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                springSecurityUser = (CustomUserDetails) authentication.getPrincipal();
//                userId = springSecurityUser.getId();
//            }
//        }
//
//        return userId;
//    }
//
//    /**
//     * 로그인 계정 명(E-mail) 반환
//     *
//     * @return String 로그인 계정 명(E-mail)
//     */
//    public static String getCurrentLoginAccount() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        CustomUserDetails springSecurityUser = null;
//        String userName = null;
//
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                springSecurityUser = (CustomUserDetails) authentication.getPrincipal();
//                userName = springSecurityUser.getUsername();
//            } else if (authentication.getPrincipal() instanceof String) {
//                userName = (String) authentication.getPrincipal();
//            }
//        }
//
//        return userName;
//    }
//
//    public static CustomUserDetails getCurrentUserDetail() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        CustomUserDetails springSecurityUser = null;
//
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                springSecurityUser = (CustomUserDetails) authentication.getPrincipal();
//            }
//        }
//
//        return springSecurityUser;
//    }
//
//    public static HashMap<String, List<ObjectId>> checkPermissionResourceReadList(){
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        HashMap<String, List<ObjectId>> returnMap = new HashMap<>();
//
//        if(authentication != null) {
//            if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                CustomUserDetails springSecurityUser = (CustomUserDetails) authentication.getPrincipal();
//                List<Role> roles = springSecurityUser.getRole();
//
//                roles = roles.stream().filter(role -> role.getName().startsWith("RESOURCE_READ"))
//                    .collect(Collectors.toList());
//
//                List<ObjectId> allowList = new ArrayList<>();
//                List<ObjectId> denyList = new ArrayList<>();
//
//                for(Role role : roles){
//                    if(role.getName().contains("READ_ALLOW")){
//                        allowList.add(role.getResourceId());
//                    } else {
//                        denyList.add(role.getResourceId());
//                    }
//                }
//
//                if(allowList.isEmpty() && !denyList.isEmpty()) {
//                    // Allow 는 없고 Deny 만 있을 경우
//                    returnMap.put("$nin",denyList);
//
//                } else if(!allowList.isEmpty() && denyList.isEmpty() ){
//                    // Allow 만 있고 Deny 만 없을 때
//                    returnMap.put("$in",allowList);
//                } else {
//                    // Allow 와 Deny 양쪽 다 있으면, Deny 와 겹치는 데이터는 Allow 에서 삭제
//                    allowList = allowList.stream().filter(s -> !denyList.contains(s)).collect(Collectors.toList());
//                    returnMap.put("$in", allowList);
//                }
//            }
//        }
//
//        return  returnMap;
//    }
//
//    /**
//     * 데이터 조회시 권한 필터링
//     *
//     * @param targetType {@link TARGETTYPE } 타겟 타입(CARDNO 등)
//     * @return {@link HashMap}{@literal  <}{@link String}, {@link List}{@literal  <}{@link String}>>
//     *         in 혹은 nin mongodb 조건절
//     */
//    public static HashMap<String, List<String>> checkPermissionTargetDataList(TARGETTYPE targetType){
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        HashMap<String, List<String>> returnMap = new HashMap<>();
//
//        if(authentication != null){
//            if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                CustomUserDetails springSecurityUser = (CustomUserDetails) authentication.getPrincipal();
//                List<Role> roles = springSecurityUser.getRole();
//
//                roles = roles.stream().filter(role -> ObjectUtils.nullSafeEquals(role.getTargetType(), targetType))
//                    .collect(Collectors.toList());
//
//                List<String> allowList = new ArrayList<>();
//                List<String> denyList = new ArrayList<>();
//
//                for(Role role : roles){
//                    if(role.getName().contains("READ_ALLOW")){
//                        allowList.add(role.getTargetSource());
//                    } else {
//                        denyList.add(role.getTargetSource());
//                    }
//                }
//
//                if(allowList.isEmpty() && !denyList.isEmpty()) {
//                    // Allow 는 없고 Deny 만 있을 경우
//                    returnMap.put("$nin",denyList);
//
//                } else if(!allowList.isEmpty() && denyList.isEmpty() ){
//                    // Allow 만 있고 Deny 만 없을 때
//                    returnMap.put("$in",allowList);
//                } else {
//                    // Allow 와 Deny 양쪽 다 있으면, Deny 와 겹치는 데이터는 Allow 에서 삭제
//                    allowList = allowList.stream().filter(s -> !denyList.contains(s)).collect(Collectors.toList());
//                    returnMap.put("$in", allowList);
//                }
//            }
//        }
//
//        return returnMap;
//    }
//
//    /**
//     * 하위 권한(Privilege)을 가지고 있는지 체크
//     *
//     * @param category ORGANIZATION_, BUSINESS_, BANK_STATEMENT_ 등의 하위 권한 소지 여부 확인
//     * @return true: 하위 권한이 있을 경우, false: 하위 권한이 없을 경우
//     */
//    public static boolean checkPrivilege(String category) {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                CustomUserDetails springSecurityUser = (CustomUserDetails) authentication.getPrincipal();
//                List<Role> roles = springSecurityUser.getRole();
//
//                long count = roles.stream().filter(s -> s.getName().startsWith(category)).count();
//
//                if (count > 0) { // 해당 카테고리의 롤을 하나라도 가지고 있을 경우
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 대상 역할이 존재하는지 체크
//     *
//     * @param roles String Array 복수의 역할 명
//     * @return true 권한 있음, false 권한 없음
//     */
//    public static boolean isCurrentUserInRole(String... roles) {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof CustomUserDetails) {
//                CustomUserDetails springSecurityUser = (CustomUserDetails) authentication.getPrincipal();
//                for (String role : roles) {
//                    if (springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(role))) {
//                        return true;
//                    }
//                }
//            } else if (authentication.getPrincipal() instanceof String) {
//                for (String role : roles) {
//                    if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(role))) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 임시 유저 여부 체크 ( ROLE_TEMPUSER 권한 소지 여부 )
//     *
//     * @return true 일반 유저, false 일반 유저 아님
//     */
//    public static boolean isTemp() {
//        boolean isTemp = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.TEMPUSER);
//
//        if (isTemp) {
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * 일반 유저 여부 체크 ( ROLE_USER 권한 소지 여부 )
//     *
//     * @return true 일반 유저, false 일반 유저 아님
//     */
//    public static boolean isUser() {
//        boolean isUser = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER);
//
//        if (isUser) {
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * 관리자 여부 체크 ( ROLE_ADMIN 권한 소지 여부 )
//     *
//     * @return true 관리자, false 관리자 아님
//     */
//    public static boolean isAdmin() {
//        boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
//
//        if (isAdmin) {
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * GOWID 여부 체크 ( ~/gowid/ url 권한 소지 여부 )
//     *
//     * @return true GOWID 계정, false GOWID 계정 아님
//     */
//    public static boolean isGowid() {
//        boolean isGowid = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.GOWID);
//
//        if (isGowid) {
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * 시프티 유저 여부 체크 ( ROLE_SHIFTEE 권한 소지 여부 )
//     *
//     * @return true 일반 유저, false 일반 유저 아님
//     */
//    public static boolean isShiftee() {
//        boolean isShiftee = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SHIFTEE);
//
//        if (isShiftee) {
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * 내부 관리자 여부 체크 ( ROLE_SUPER 권한 소지 여부 )
//     *
//     * @return true 내부 관리자, false 내부 관리자 아님
//     */
//    public static boolean isSuper() {
//        boolean isSuper = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPER);
//
//        if (isSuper) {
//            return true;
//        }
//
//        return false;
//    }
//
//}
