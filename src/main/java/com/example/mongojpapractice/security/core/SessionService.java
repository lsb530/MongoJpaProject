package com.example.mongojpapractice.security.core;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.mongo.MongoIndexedSessionRepository;
import org.springframework.session.data.mongo.MongoSession;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SessionService {

    private final MongoIndexedSessionRepository mongoIndexedSessionRepository;

    public SessionService(
        MongoIndexedSessionRepository mongoIndexedSessionRepository
    ) {
        this.mongoIndexedSessionRepository = mongoIndexedSessionRepository;
    }

    /**
     *
     * 현재 유저의 변경된 Role 권한으로 동일 계정명으로 이용되는 모든 세션의 권한을 업데이트
     *
     * @param email Role 변경 대상 유저의 회원 계정(email 주소)
     * @param currentSessionId Role 변경 대상 유저의 spring secure Session Id(X-Auth-Token)
     */
    public void updateSessionsRole(String email, String currentSessionId){
        MongoSession sourceSession = mongoIndexedSessionRepository.findById(currentSessionId);

        if (sourceSession == null) {
            return;
        }

        SecurityContext sourceAttribute = sourceSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        if (sourceAttribute == null) {
            return;
        }

        Map<String, MongoSession> sessionMap
            = mongoIndexedSessionRepository.findByIndexNameAndIndexValue(
            FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, email
        );

        sessionMap.remove(currentSessionId);

        for (Map.Entry<String, MongoSession> entry : sessionMap.entrySet()) {
            MongoSession targetSession = entry.getValue();
            SecurityContext securityContext = targetSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (securityContext == null) {
                continue;
            }
            targetSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sourceAttribute);
            mongoIndexedSessionRepository.save(targetSession);
        }
    }
}
