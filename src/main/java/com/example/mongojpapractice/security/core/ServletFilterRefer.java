//package com.example.mongojpapractice.security.core;
//
//import com.aibizon.aitax.backbone.constants.StatusCodes;
//import com.aibizon.aitax.backbone.exception.ApiErrorException;
//import com.aibizon.aitax.backbone.exception.ApiErrorResponse;
//import com.aibizon.aitax.businesslogic.security.core.AuthoritiesConstants;
//import com.aibizon.aitax.businesslogic.security.core.CustomUserDetails;
//import com.aibizon.aitax.businesslogic.security.document.Business;
//import com.aibizon.aitax.businesslogic.security.document.Organization;
//import com.aibizon.aitax.businesslogic.security.document.Role;
//import com.aibizon.aitax.businesslogic.security.document.User;
//import com.aibizon.aitax.businesslogic.security.service.BusinessService;
//import com.aibizon.aitax.businesslogic.security.service.OrganizationService;
//import com.aibizon.aitax.businesslogic.security.service.RoleService;
//import com.aibizon.aitax.businesslogic.security.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.bson.types.ObjectId;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//
//@Slf4j
//@Component
//public class ServletFilter extends OncePerRequestFilter {
//
//    private final ObjectMapper objectMapper;
//
//    private final UserService userService;
//
//    private final RoleService roleService;
//
//    private final OrganizationService organizationService;
//
//    private final BusinessService businessService;
//
//    public ServletFilter(ObjectMapper objectMapper,
//        UserService userService,
//        RoleService roleService,
//        OrganizationService organizationService,
//        BusinessService businessService
//    ) {
//
//        this.objectMapper = objectMapper;
//        this.userService = userService;
//        this.roleService = roleService;
//        this.organizationService = organizationService;
//        this.businessService = businessService;
//    }
//
//    private boolean checkExclude(String value) {
//        // 로그인 혹은 로그아웃, 등록(회원가입) URL 일 경우
//        return (value.equals("143d85801cf356fb")
//            || value.equals("login")
//            || value.equals("login-with-gowid")
//            || value.equals("code")
//            || value.equals("legacy-login")
//            || value.equals("logout")
//            || value.equals("find-passwd")
//            || value.equals("register")
//            || value.equals("login-with-shiftee")
//            || value.equals("shiftee")
//            || value.equals("gowid"));
//    }
//
//    /**
//     * 필터를 타지 않을 경로 설정
//     *
//     * @param request HttpServletRequest
//     * @return boolean
//     */
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getRequestURL().toString();
//
//        try {
//            URL aURL = new URL(path);
//
//            String[] segments = aURL.getPath().split("/");
//
//            if ((!(segments[3].isEmpty()) && this.checkExclude(segments[3]))) {
//
//                return true;
//            }
//
//        } catch (MalformedURLException mue) {
//            return false;
//        }
//
//        return false;
//    }
//
//    private void setErrorResponse(ContentCachingResponseWrapper httpServletResponse, StatusCodes statusCodes)
//        throws IOException {
//        ApiErrorResponse apiErrorResponse
//            = new ApiErrorResponse(statusCodes.name(), statusCodes.description);
//
//        httpServletResponse.setStatus(statusCodes.status.value());
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
//
//        objectMapper.writeValue(httpServletResponse.getWriter(), apiErrorResponse);
//
//        httpServletResponse.copyBodyToResponse();
//    }
//
//    @Override
//    protected void doFilterInternal(
//        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//        throws ServletException, IOException {
//        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper(request);
//        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper(response);
//
//        // 로그인 Auth 정보
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (ObjectUtils.isEmpty(auth)) {
//            setErrorResponse(httpServletResponse, StatusCodes.E001);
//        }
//
//        // User 컬렉션에서 데이터 조회
//        User userEntity = userService.findByEmail(auth.getName()).orElse(null);
//
//        if (ObjectUtils.isEmpty(userEntity)) {
//            setErrorResponse(httpServletResponse, StatusCodes.UE005);
//        }
//
//        // 할당 권한 정보 수집
//        List<Role> roles = roleService.findRoleByIdIsIn(userEntity.getRoles());
//
//        long tempUser = roles.stream()
//            .filter(s -> ObjectUtils.nullSafeEquals(s.getName(), AuthoritiesConstants.TEMPUSER)).count();
//
//        CustomUserDetails customUserDetails;
//
//        if(tempUser > 0){
//            customUserDetails = CustomUserDetails.builder()
//                .id(userEntity.getId())
//                .email(userEntity.getEmail())
//                .password(userEntity.getPassword())
//                .isEnabled(userEntity.getIsEnabled())
//                .role(roles)
//                .build();
//
//        } else {
//            List<Organization> organizations = organizationService.findOrganizationsByUsers(userEntity.getId());
//
//            if (ObjectUtils.isEmpty(organizations) || organizations.size() == 0) { // 대상 기관이 없을 경우
//                throw new ApiErrorException(StatusCodes.OE001);
//            }
//
//            List<ObjectId> businesses = new ArrayList<>();
//
//            for (Organization organization : organizations) {
//                businesses.addAll(organization.getBusinesses());
//            }
//
//            List<Business> businessList = businessService.findBusinessByIdIsIn(businesses);
//
//            List<HashMap<String, Object>> resources = new ArrayList<>();
//
//            for (Business business : businessList) {
//                HashMap<String, Object> hm = new LinkedHashMap<>();
//
//                List<ObjectId> scrapResourceList = new ArrayList<>(
//                    business.getScrapResources().stream().map(s -> s.getId()).collect(Collectors.toList()));
//
//                organizations.stream().forEachOrdered(s -> {
//                    if (s.getBusinesses().contains(business.getId())) {
//                        hm.put("organizationId", s.getId());
//                        hm.put("businessId", business.getId());
//                        hm.put("scrapResources", scrapResourceList);
//                    }
//                });
//
//                resources.add(hm);
//            }
//
//            customUserDetails = CustomUserDetails.builder()
//                .id(userEntity.getId())
//                .email(userEntity.getEmail())
//                .password(userEntity.getPassword())
//                .isEnabled(userEntity.getIsEnabled())
//                .resources(resources)
//                .role(roles)
//                .build();
//        }
//
//        // Auth 설정
//        auth =
//            new UsernamePasswordAuthenticationToken(customUserDetails, customUserDetails.getPassword(),
//                customUserDetails.getAuthorities());
//
//        SecurityContext sc = SecurityContextHolder.getContext();
//        sc.setAuthentication(auth);
//
//        // 권한 체크 및 에러 핸들링
//        // 필터 적용
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//
//        // 헤더 Content Type 설정
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.copyBodyToResponse();
//    }
//}
