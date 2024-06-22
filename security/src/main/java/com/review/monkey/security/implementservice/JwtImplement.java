package com.review.monkey.security.implementservice;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.review.monkey.security.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class JwtImplement implements JwtService {

    @NonFinal
    protected static final String SIGNER_KEY = "K97FiFxePl6FgFGz3oSpIGFrCVwAalDU7i3MH/bnGRiqYGb8UVDcxuhREqttjuue";

    @Override
    public String generateToken(String username) {
        // CREATE HEADER AND  ALGORITHM TO ENCODE
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // CREATE CLAIM FOR TOKEN
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("monkey.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("customeClaim" , "Custom")
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
}
