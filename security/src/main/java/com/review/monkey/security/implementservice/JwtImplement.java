package com.review.monkey.security.implementservice;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.review.monkey.security.model.Permission;
import com.review.monkey.security.model.Role;
import com.review.monkey.security.model.User;
import com.review.monkey.security.model.mapping.RolePermission;
import com.review.monkey.security.model.mapping.UserRole;
import com.review.monkey.security.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class JwtImplement implements JwtService {

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;


    @Override
    public String generateToken(User user) {
        // CREATE HEADER AND  ALGORITHM TO ENCODE
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // CREATE CLAIM FOR TOKEN
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("monkey.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                // jwtID is token id
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        // CREATE PAYLOAD
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // ASSIGN HEADER AND PAYLOAD TO GENERATE TOKENs
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.info("CAN'T CREATE TOKEN : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {

            for (UserRole userRole : user.getUserRoles()) {
                Role role = userRole.getRole();
                // DISTINGUISH BEETWEN ROLE AND PERMISSION
                stringJoiner.add("ROLE_" + role.getRoleName());

                if (role.getRolePermission() != null && !role.getRolePermission().isEmpty()) {

                    for (RolePermission rolePermission : role.getRolePermission()) {
                        Permission permission = rolePermission.getPermission();
                        stringJoiner.add(permission.getPermissionName());
                    }
                }
            }
        }
        return stringJoiner.toString();
    }
}
