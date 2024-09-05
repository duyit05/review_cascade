package com.review.monkey.security.config;

import com.nimbusds.jose.JOSEException;
import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.request.IntrospectRequest;
import com.review.monkey.security.response.IntrospectResponse;
import com.review.monkey.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;



// CONFIG TO REPLACE JWTDECODER IN CLASS SECURITY
@Component
public class CustomeJwtDecoder implements JwtDecoder {

    @Autowired
    private AuthenticationService authenticationService;

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    private NimbusJwtDecoder nuNimbusJwtDecoder = null;
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            IntrospectResponse isValidToken =  authenticationService.introspect(IntrospectRequest.builder().token(token).build());

            if(!isValidToken.isValid())
                throw new AppException(ErrorCode.UNAUTHENTICATED);

        }catch (JOSEException | ParseException e){
            throw new JwtException(e.getMessage());
        }

        if(Objects.isNull(nuNimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes() , "HS512");
            nuNimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nuNimbusJwtDecoder.decode(token);
    }
}
