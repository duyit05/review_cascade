package com.review.monkey.security.implementservice;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.review.monkey.security.exception.AppException;
import com.review.monkey.security.exception.ErrorCode;
import com.review.monkey.security.model.InvalidatedToken;
import com.review.monkey.security.model.User;
import com.review.monkey.security.repository.InvalidatedTokenRepository;
import com.review.monkey.security.repository.UserRepository;
import com.review.monkey.security.request.AuthenticationRequest;
import com.review.monkey.security.request.IntrospectRequest;
import com.review.monkey.security.request.LogoutRequest;
import com.review.monkey.security.request.RefreshTokenRequest;
import com.review.monkey.security.response.AuthenticationResponse;
import com.review.monkey.security.response.IntrospectResponse;
import com.review.monkey.security.service.AuthenticationService;
import com.review.monkey.security.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationImplement implements AuthenticationService {

    UserRepository userRepository;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duaration}")
    protected long REFRESABLE_DURATION;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        User checkUserExisted = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), checkUserExisted.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = jwtService.generateToken(checkUserExisted);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        // GET TOKEN FROM REQUEST
        String token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            // GET TOKEN FROM REQUEST
            SignedJWT signToken = verifyToken(request.getToken(), true);

            // GET UUID OF TOKEN
            String jit = signToken.getJWTClaimsSet().getJWTID();
            // CHECK TOKEN EXPIRITIME OF NOT
            Date expirtyTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expirtyTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already expired!");
        }

    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        // GET SIGNER KEY OF SYSTEM
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        // GET TOKEN FROM REQUEST
        SignedJWT signedJWT = SignedJWT.parse(token);
        // CHECK TOKEN EXPIRYTIME OR NOT
        // IF isRefresh is true to refresh token , opposite to authenticate
        Date expiryTime = (isRefresh) ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()) : signedJWT.getJWTClaimsSet().getExpirationTime();

        // CHECK VERIFIED TRUE OR FALSE
        boolean vierified = signedJWT.verify(verifier);

        // IF CONDITION NOT TRUE THEN THROW EXCEPTION
        if (!(vierified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        // CHECK UUID EXIST IN DATABASE OR NOT
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;

    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }
}
