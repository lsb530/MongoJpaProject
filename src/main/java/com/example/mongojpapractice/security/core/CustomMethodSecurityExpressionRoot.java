//package com.example.mongojpapractice.security.core;
//
//import com.aibizon.aitax.businesslogic.security.document.Role;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicLong;
//import lombok.extern.slf4j.Slf4j;
//import org.bson.types.ObjectId;
//import org.springframework.security.access.expression.SecurityExpressionRoot;
//import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
//import org.springframework.security.core.Authentication;
//import org.springframework.util.ObjectUtils;
//
//@Slf4j
//public class CustomMethodSecurityExpressionRoot
//    extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
//    private final ObjectMapper objectMapper;
//    private Object filterObject;
//    private Object returnObject;
//    private Object target;
//
//    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
//        super(authentication);
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(ObjectId.class, new ToStringSerializer());
//        this.objectMapper = JsonMapper.builder()
//            .findAndAddModules()
//            .addModule(simpleModule)
//            .build();
//    }
//
//    @Override
//    public void setFilterObject(Object filterObject) {
//        this.filterObject = filterObject;
//    }
//
//    @Override
//    public Object getFilterObject() {
//        return filterObject;
//    }
//
//    @Override
//    public void setReturnObject(Object returnObject) {
//        this.returnObject = returnObject;
//    }
//
//    @Override
//    public Object getReturnObject() {
//        return returnObject;
//    }
//
//    void setThis(Object target) {
//        this.target = target;
//    }
//
//    @Override
//    public Object getThis() {
//        return target;
//    }
//
//    /**
//     * 사용자 정보 접근 권한 체크
//     *
//     * @param userId 사용자 아이디
//     * @return true: 접근 가능, false: 접근 불가능
//     */
//    public boolean checkUserInfo(ObjectId userId) {
//        return SecurityUtils.getCurrentUserDetail().getId().equals(userId);
//    }
//
//    /**
//     * 기관 정보 접근 권한 체크
//     *
//     * @param target {@link List} 기관 Id가 포함된 대상 목록
//     * @param permission {@link String} 동작 명(read, write, modify, delete 등)
//     * @return true: 접근 가능, false: 접근 불가능
//     */
//    public boolean checkOrganizationInfos(List<Object> target, String fieldName, String permission) {
//        Set<ObjectId> organizationIds = new HashSet<ObjectId>();
//
//        target.forEach(d->{
//            var map = objectMapper.convertValue(d, Map.class);
//
//            if(map.containsKey(fieldName)){
//                organizationIds.add(new ObjectId(map.get(fieldName).toString()));
//            }
//        });
//
//        return organizationIds.stream().allMatch(d-> this.checkOrganizationInfo(d, permission));
//    }
//
//    /**
//     * 기관 정보 접근 권한 체크
//     *
//     * @param organizationId {@link ObjectId} 기관 도큐먼트 ID
//     * @param permission {@link String} 동작 명(read, write, modify, delete 등)
//     * @return true: 접근 가능, false: 접근 불가능
//     */
//    public boolean checkOrganizationInfo(ObjectId organizationId, String permission) {
//        String header = "ORGANIZATION";
//
//        if(SecurityUtils.isSuper()){ // 그냥 모두 OK
//            return true;
//        }
//
//        CustomUserDetails customUserDetails = SecurityUtils.getCurrentUserDetail();
//
//        if(ObjectUtils.isEmpty(customUserDetails)) {
//            return false;
//        }
//
//        if( SecurityUtils.isAdmin() || SecurityUtils.isGowid() || !SecurityUtils.checkPrivilege((header+"_"+permission).toUpperCase())) {
//            // 관리자 아이디거나 하위 Privilege 가 없으면
//            // 자신이 속한 기관일 경우에만 Pass
//            long isMatch =
//                customUserDetails.getResources().stream()
//                    .filter(resource -> resource.containsValue(organizationId))
//                    .count();
//
//            return isMatch>0;
//        }
//
//        List<Role> roles = customUserDetails.getRole();
//
//        AtomicLong denyCount = new AtomicLong();
//        AtomicLong allowCount = new AtomicLong();
//        AtomicLong isDeny = new AtomicLong();
//        AtomicLong isAllow = new AtomicLong();
//
//        roles.forEach(role -> {
//            if (role.getName().startsWith(header) &&
//                role.getName().contains((permission + "_deny").toUpperCase())) {
//                denyCount.addAndGet(1);
//            }
//
//            if(role.getName().startsWith(header) &&
//                role.getName().contains((permission + "_allow").toUpperCase())) {
//                allowCount.addAndGet(1);
//            }
//
//            if(ObjectUtils.nullSafeEquals(role.getOrganizationId(), organizationId) &&
//                role.getName().startsWith(header) &&
//                role.getName().contains((permission+"_deny").toUpperCase())) {
//                isDeny.addAndGet(1);
//            }
//
//            if(ObjectUtils.nullSafeEquals(role.getOrganizationId(), organizationId) &&
//                role.getName().startsWith(header) &&
//                role.getName().contains((permission+"_allow").toUpperCase())) {
//                isAllow.addAndGet(1);
//            }
//        });
//
//        return checkReturn(denyCount.get(), allowCount.get(), isDeny.get(), isAllow.get());
//    }
//
//    /**
//     * 사업자 정보 접근 권한 체크
//     *
//     * @param businessIds {@link List} 사업자 Id 목록
//     * @param permission {@link String} 동작 명(read, write, modify, delete 등)
//     * @return true: 접근 가능, false: 접근 불가능
//     */
//    public boolean checkBusinessIds(List<ObjectId> businessIds, String permission) {
//        Set<ObjectId> set = new HashSet<ObjectId>(businessIds);
//        return set.stream().allMatch(d-> this.checkBusinessInfo(d, permission));
//    }
//    /**
//     * 사업자 정보 접근 권한 체크
//     *
//     * @param target {@link List} 사업자 Id가 포함된 대상 목록
//     * @param permission {@link String} 동작 명(read, write, modify, delete 등)
//     * @return true: 접근 가능, false: 접근 불가능
//     */
//    public boolean checkBusinessInfos(List<Object> target, String fieldName, String permission) {
//        Set<ObjectId> businessIds = new HashSet<ObjectId>();
//
//        target.forEach(d->{
//            var map = objectMapper.convertValue(d, Map.class);
//
//            if(map.containsKey(fieldName)){
//                businessIds.add(new ObjectId(map.get(fieldName).toString()));
//            }
//        });
//
//        return businessIds.stream().allMatch(d-> this.checkBusinessInfo(d, permission));
//    }
//
//    /**
//     * 사업자 정보 접근 권한 체크
//     *
//     * @param businessId {@link ObjectId} 사업자 도큐먼트 ID
//     * @param permission {@link String} 동작 명(read, write, modify, delete 등)
//     * @return true: 접근 가능, false: 접근 불가능
//     */
//    public boolean checkBusinessInfo(ObjectId businessId, String permission) {
//        String header = "BUSINESS";
//
//        if(ObjectUtils.isEmpty(businessId)){
//            return false;
//        }
//
//        if(SecurityUtils.isSuper()){ // 그냥 모두 OK
//            return true;
//        }
//
//        CustomUserDetails customUserDetails = SecurityUtils.getCurrentUserDetail();
//
//        if(ObjectUtils.isEmpty(customUserDetails)) {
//            return false;
//        }
//
//        if( SecurityUtils.isAdmin() || SecurityUtils.isGowid() || !SecurityUtils.checkPrivilege((header+"_"+permission).toUpperCase())){
//            // 관리자 아이디거나 하위 Privilege 가 없으면 내가 속한 기관의 사업자 ID 목록과 비교
//            long isMatch =
//                customUserDetails.getResources().stream()
//                    .filter(resource -> resource.containsValue(businessId))
//                    .count();
//
//            return isMatch>0;
//        }
//
//        List<Role> roles = customUserDetails.getRole();
//
//        AtomicLong denyCount = new AtomicLong();
//        AtomicLong allowCount = new AtomicLong();
//        AtomicLong isDeny = new AtomicLong();
//        AtomicLong isAllow = new AtomicLong();
//
//        roles.forEach(role -> {
//            if (role.getName().startsWith(header) &&
//                role.getName().contains((permission + "_deny").toUpperCase())) {
//                denyCount.addAndGet(1);
//            }
//
//            if(role.getName().startsWith(header) &&
//                role.getName().contains((permission + "_allow").toUpperCase())) {
//                allowCount.addAndGet(1);
//            }
//
//            if(ObjectUtils.nullSafeEquals(role.getBusinessId(), businessId) &&
//                role.getName().startsWith(header) &&
//                role.getName().contains((permission+"_deny").toUpperCase())) {
//                isDeny.addAndGet(1);
//            }
//
//            if(ObjectUtils.nullSafeEquals(role.getBusinessId(), businessId) &&
//                role.getName().startsWith(header) &&
//                role.getName().contains((permission+"_allow").toUpperCase())) {
//                isAllow.addAndGet(1);
//            }
//        });
//
//        return checkReturn(denyCount.get(), allowCount.get(), isDeny.get(), isAllow.get());
//    }
//
//    /**
//     * 리소스 정보 접근 권한 체크
//     *
//     * @param target {@link List} 리소스 Id가 포함된 대상 목록
//     * @param permission {@link String} 동작 명(read, write, modify, delete 등)
//     * @return true: 접근 가능, false: 접근 불가능
//     */
//    public boolean checkScrapResourceInfos(List<Object> target, String permission) {
//        Set<ObjectId> scrapResourcesIds = new HashSet<ObjectId>();
//
//        target.forEach(d->{
//            var map = objectMapper.convertValue(d, Map.class);
//
//            if(map.containsKey("scrapResourcesId")){
//                scrapResourcesIds.add(new ObjectId(map.get("scrapResourcesId").toString()));
//            }
//        });
//
//        return scrapResourcesIds.stream().allMatch(d-> this.checkScrapResourceInfo(d, permission));
//    }
//
//    /**
//     * 리소스 정보 접근 권한 체크
//     *
//     * @param scrapResourcesId {@link ObjectId} 리소스 도큐먼트 ID
//     * @param permission {@link String} 동작 명(read, write, modify, delete 등)
//     * @return true: 접근 가능, false: 접근 불가능
//     */
//    public boolean checkScrapResourceInfo(ObjectId scrapResourcesId, String permission) {
//        String header = "RESOURCE";
//
//        if(ObjectUtils.isEmpty(scrapResourcesId)){
//            return false;
//        }
//
//        if(SecurityUtils.isSuper()){ // 그냥 모두 OK
//            return true;
//        }
//
//        CustomUserDetails customUserDetails = SecurityUtils.getCurrentUserDetail();
//
//        if(ObjectUtils.isEmpty(customUserDetails)) {
//            return false;
//        }
//
//        if( SecurityUtils.isAdmin() || SecurityUtils.isGowid() || !SecurityUtils.checkPrivilege(header+"_"+permission.toUpperCase())) {
//            // 관리자 아이디거나 하위 Privilege 가 없으면 내가 속한 기관, 사업자의 리소스 ID 목록과 비교
//            long isMatch =
//                customUserDetails.getResources().stream()
//                    .filter(resource -> {
//                        if(resource.containsKey("scrapResources")){
//                            return ((ArrayList<?>)resource.get("scrapResources")).contains(scrapResourcesId);
//                        }else{
//                            return false;
//                        }
//                    })
//                    .count();
//
//            return isMatch>0;
//        }
//
//        List<Role> roles = customUserDetails.getRole();
//
//        AtomicLong denyCount = new AtomicLong();
//        AtomicLong allowCount = new AtomicLong();
//        AtomicLong isDeny = new AtomicLong();
//        AtomicLong isAllow = new AtomicLong();
//
//        roles.forEach(role -> {
//            if (role.getName().startsWith(header) &&
//                    role.getName().contains((permission + "_deny").toUpperCase())) {
//                denyCount.addAndGet(1);
//            }
//
//            if(role.getName().startsWith(header) &&
//                role.getName().contains((permission + "_allow").toUpperCase())) {
//                allowCount.addAndGet(1);
//            }
//
//            if(ObjectUtils.nullSafeEquals(role.getResourceId(), scrapResourcesId) &&
//                role.getName().startsWith(header) &&
//                role.getName().contains((permission+"_deny").toUpperCase())) {
//                isDeny.addAndGet(1);
//            }
//
//            if(ObjectUtils.nullSafeEquals(role.getResourceId(), scrapResourcesId) &&
//                role.getName().startsWith(header) &&
//                role.getName().contains((permission+"_allow").toUpperCase())) {
//                isAllow.addAndGet(1);
//            }
//        });
//
//        return checkReturn(denyCount.get(), allowCount.get(), isDeny.get(), isAllow.get());
//    }
//
//    /**
//     * 데이터 접근 권한 체크
//     *
//     * @param targetSource {@link Object} 권한 체크 대상 ID
//     * @param permission {@link String} 동작 명(read, write, modify, delete 등)
//     * @return true: 접근 가능, false: 접근 불가능
//     */
//    public boolean checkDataInfo(String targetSource, String permission) {
//        String header = "DATA";
//
//        if(SecurityUtils.isSuper()){ // 그냥 모두 OK
//            return true;
//        }
//
//        CustomUserDetails customUserDetails = SecurityUtils.getCurrentUserDetail();
//
//        if(ObjectUtils.isEmpty(customUserDetails)) {
//            return false;
//        }
//
//        if( SecurityUtils.isAdmin() || SecurityUtils.isGowid() || !SecurityUtils.checkPrivilege(header+"_"+permission.toUpperCase())) {
//            // 관리자 아이디거나 하위 Privilege 가 없으면 접근 가능
//            return true;
//        }
//
//        List<Role> roles = customUserDetails.getRole();
//
//        AtomicLong denyCount = new AtomicLong();
//        AtomicLong allowCount = new AtomicLong();
//        AtomicLong isDeny = new AtomicLong();
//        AtomicLong isAllow = new AtomicLong();
//
//        roles.forEach(role -> {
//            if (role.getName().startsWith(header) &&
//                role.getName().contains((permission + "_deny").toUpperCase())) {
//                denyCount.addAndGet(1);
//            }
//
//            if(role.getName().startsWith(header) &&
//                role.getName().contains((permission + "_allow").toUpperCase())) {
//                allowCount.addAndGet(1);
//            }
//
//            if(ObjectUtils.nullSafeEquals(role.getTargetSource(), targetSource) &&
//                role.getName().startsWith(header) &&
//                role.getName().contains((permission+"_deny").toUpperCase())) {
//                isDeny.addAndGet(1);
//            }
//
//            if(ObjectUtils.nullSafeEquals(role.getTargetSource(), targetSource) &&
//                role.getName().startsWith(header) &&
//                role.getName().contains((permission+"_allow").toUpperCase())) {
//                isAllow.addAndGet(1);
//            }
//        });
//
//        return checkReturn(denyCount.get(), allowCount.get(), isDeny.get(), isAllow.get());
//    }
//
//
//    private boolean checkReturn(long denyCount, long allowCount, long isDeny, long isAllow) {
//
//        if(denyCount > 0 && allowCount == 0 && isDeny == 0 && isAllow == 0){
//            // 하나 이상이 Deny 인데, 입력받은 대상 사업자 Id가 아닐 경우 Allow 처리
//            return true;
//        } else if(allowCount > 0 && isDeny == 0 && isAllow == 0) {
//            // 하나 이상이 Allow 인데 입력 받은 대상 사업자 Id가 아닐 경우 Deny 처리
//            return false;
//        }
//
//        return (isDeny==0 && isAllow > 0);
//    }
//}
