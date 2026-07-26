package com.com.pri_vrat.authentication.service;

import com.com.pri_vrat.authentication.config.UserNameContext;
import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.AuthUserDto;
import com.com.pri_vrat.authentication.dto.VerificationDto;
import com.com.pri_vrat.authentication.entity.AuthUserPk;
import com.com.pri_vrat.authentication.entity.UserAuth;
import com.com.pri_vrat.authentication.exception.customException.RegistrationException;
import com.com.pri_vrat.authentication.exception.customException.VerificationException;
import com.com.pri_vrat.authentication.repository.AuthUserRepository;
import com.com.pri_vrat.authentication.util.Constants;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountManagementServiceImplementation implements AccountManagementService {

    private final AuthUserRepository authUserRepository;
    private final MessageSource messageSource;
    private final Keycloak keycloak;
    private final FlywayMigrationService migrationService;

    @Value("${docmanager.keycloak.realm}")
    private String realm;

    @Transactional
    @Override
    public AppResponse registerUser(AuthUserDto authDto) throws RegistrationException {
        log.info("Entered into user registration----!");
        AppResponse response = new AppResponse();
        String userId = null;
        final String workSpaceId = UUID.randomUUID().toString();
        AuthUserPk userPk = AuthUserPk.builder()
                .userName(authDto.getUserName())
                .email(authDto.getEmail())
                .build();
        Optional<UserAuth> userOptional = authUserRepository.findById(userPk);
        if (userOptional.isPresent()) {
            throw new RegistrationException(messageSource.getMessage("MESSAGE.REGISTRATION.DUPLICATE.ID", null, Locale.ENGLISH));
        }
        List<UserAuth> userList = authUserRepository.findByUserPrimaryKey_UserName(authDto.getUserName());
        if (!userList.isEmpty()) {
            throw new RegistrationException(messageSource.getMessage("MESSAGE.REGISTRATION.DUPLICATE.USER", null, Locale.ENGLISH));
        }
        List<UserAuth> userListByMail = authUserRepository.findByUserPrimaryKey_Email(authDto.getEmail());
        if (!userListByMail.isEmpty()) {
            throw new RegistrationException(messageSource.getMessage("MESSAGE.REGISTRATION.DUPLICATE.EMAIL", null, Locale.ENGLISH));
        }
        try {
            JSONObject keycloakResponse = registerUserInKeycloak(authDto);
            if (!(keycloakResponse.get("CODE").equals(Constants.RESPONSE_CODE.SUCCESS))) {
                throw new RegistrationException(keycloakResponse.get("MESSAGE").toString());
            }
            userId = keycloakResponse.get("USER_ID").toString();
            if (keycloakResponse.get("CODE").equals(Constants.RESPONSE_CODE.FAILED)) {
                throw new RegistrationException(keycloakResponse.get("MESSAGE").toString());
            }
            final String tenantId =
                    authDto.getUserName().toUpperCase() + "_" + authDto.getApplicationName().toUpperCase();
            UserAuth registrarUser = getAuthUserBuilder(authDto, userPk);
            registrarUser.setTenantId(tenantId);
            UserAuth savedUser = authUserRepository.save(registrarUser);
            migrationService.tenantMigration(tenantId);
            response.setCode(Constants.RESPONSE_CODE.SUCCESS);
            response.setMessage(messageSource
                    .getMessage("MESSAGE.REGISTRATION.SUCCESS", null, Locale.ENGLISH));
            response.setDetails(Collections.emptyList());
            log.info("Leave from user registration----!");
            return response;
        } catch (Exception e) {
            log.info("Exception occurred in registerUser()...{} ", e.toString());
            if (userId != null && userId.isEmpty()) {
                rollBack(userId);
            }
            log.info("Exception occurred in registerUser()...{} ", e.toString());
            throw e;
        }
    }

    @Override
    public AppResponse verifyUser(VerificationDto verificationDto) {
        UserResource currentUserResource = null;
        UserAuth currentUser = new UserAuth();
        try {
            if (verificationDto.getEmail() == null) {
                throw new VerificationException(messageSource.getMessage("MESSAGE.VERIFY.EXCEPTION.EMAIL.INVALID", null, Locale.ENGLISH));
            }
            List<UserAuth> userListByEmail = authUserRepository.findByUserPrimaryKey_Email(verificationDto.getEmail());
            if (userListByEmail.isEmpty()) {
                throw new VerificationException(messageSource.getMessage("MESSAGE.AUTH.INVALID.EMAIL", null, Locale.ENGLISH));
            }
            currentUser = userListByEmail.getFirst();
            if (currentUser.getStatus().equals(Constants.STATUS.APPROVED)) {
                throw new VerificationException(messageSource.getMessage("MESSAGE.ACCOUNT.ALREADY.VERIFIED", null, Locale.ENGLISH));
            }
            if (currentUser.getStatus().equals(Constants.STATUS.DELETED) || currentUser.getStatus().equals(Constants.STATUS.DELETED)) {
                throw new VerificationException(messageSource.getMessage("MESSAGE.ACCOUNT.ALREADY.DELETED", null, Locale.ENGLISH));
            }
            List<UserRepresentation> keycloakUsers = keycloak.realm(realm).users().search(verificationDto.getEmail());
            if (keycloakUsers.isEmpty()) {
                throw new VerificationException(messageSource.getMessage("MESSAGE.ACCOUNT.DOES.NOT.EXISTS", null, Locale.ENGLISH));
            }
            String userId = keycloakUsers.getFirst().getId();
            currentUserResource = keycloak.realm(realm).users().get(userId);
            UserRepresentation currentUserRepresentation = currentUserResource.toRepresentation();
            currentUserRepresentation.setEnabled(true);
            currentUser.setStatus(Constants.STATUS.APPROVED);
            currentUserResource.update(currentUserRepresentation);
            authUserRepository.save(currentUser);
            AppResponse response = new AppResponse();
            response.setCode(Constants.RESPONSE_CODE.SUCCESS);
            response.setMessage(messageSource.getMessage("MESSAGE.VERIFY.USER.SUCCESSFULLY", null, Locale.ENGLISH));
            response.setDetails(Collections.emptyList());
            return response;
        } catch (VerificationException ex) {
            throw ex;
        } catch (Exception e) {
            rollBackVerification(currentUser, currentUserResource);
            log.info("Exception at verifyUser()..{}", e.getMessage());
            throw e;
        }
    }

    @Override
    public AppResponse getUserDetails() {
        try {
            log.info("Entering into getUserDetails()");
            JwtAuthenticationToken authenticationToken =
                    (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = authenticationToken.getToken();
            String loggedInUser = jwt.getClaimAsString("LOG-USER-NAME");
            if (loggedInUser == null || loggedInUser.isBlank()) {
                return AppResponse.builder()
                        .code("FAILED")
                        .message(messageSource.getMessage("USER.DETAILS.FETCH.ANONYMOUS", null, Locale.ENGLISH))
                        .details(List.of())
                        .build();
            }
            List<UserAuth> authenticatedUserList = authUserRepository.findByUserPrimaryKey_UserName(loggedInUser);
            UserAuth authenticatedUser = authenticatedUserList.getFirst();
            JSONObject response = new JSONObject();
            response.put("firstName", authenticatedUser.getFirstName());
            response.put("lastName", authenticatedUser.getLastName());
            response.put("email", authenticatedUser.getUserPrimaryKey().getEmail());
            response.put("userName", authenticatedUser.getUserPrimaryKey().getUserName());
            return AppResponse.builder()
                    .code("SUCCESS")
                    .message(messageSource.getMessage("USER.DETAILS.FETCH.SUCCESS", null, Locale.ENGLISH))
                    .details(List.of(response))
                    .build();
        } catch (Exception ex) {
            log.error("Failed for message {} at method {}", ex.getMessage(), "getUserDetails()");
            throw ex;
        }
    }


    private UserAuth getAuthUserBuilder(AuthUserDto authDto, AuthUserPk userPk) {
        return UserAuth.builder()
                .firstName(authDto.getFirstName())
                .lastName(authDto.getLastName())
                .dob(authDto.getDob())
                .status(Constants.STATUS.PENDING)
                .userPrimaryKey(userPk)
                .applicationName(authDto.getApplicationName())
                .password(authDto.getPassword())
                .build();
    }

    public JSONObject registerUserInKeycloak(AuthUserDto authDto) {
        log.info("Entered into keycloak registration----!");
        JSONObject keycloakResponse = new JSONObject();
        try {
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(authDto.getEmail());
            userRepresentation.setFirstName(authDto.getFirstName());
            userRepresentation.setLastName(authDto.getLastName());
            userRepresentation.setEmail(authDto.getEmail());
            userRepresentation.setEnabled(true);

            final String tenantId = authDto.getUserName().toUpperCase() + "_" + authDto.getApplicationName().toUpperCase();

            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("LOG-TENANT-ID", Collections.singletonList(tenantId));
            attributes.put("LOG-USER-TYPE", Collections.singletonList(authDto.getUserType()));
            attributes.put("LOG-USER-NAME", Collections.singletonList(authDto.getUserName()));
            attributes.put("LOG-APP-NAME", Collections.singletonList(authDto.getApplicationName()));

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType("password");
            credential.setValue(authDto.getPassword());
            credential.setTemporary(false);


            userRepresentation.setAttributes(attributes);
            userRepresentation.setCredentials(Collections.singletonList(credential));
            Response response = keycloak.realm(realm).users().create(userRepresentation);
            if (response.getStatus() == 409) {
                keycloakResponse.put("CODE", Constants.RESPONSE_CODE.FAILED);
                keycloakResponse.put("MESSAGE", "User already exists..!");
            } else if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);
                keycloakResponse.put("CODE", Constants.RESPONSE_CODE.SUCCESS);
                keycloakResponse.put("MESSAGE", "User created");
                keycloakResponse.put("USER_ID", userId);
                keycloakResponse.put("tenantId", tenantId);
            } else {
                keycloakResponse.put("CODE", Constants.RESPONSE_CODE.FAILED);
                keycloakResponse.put("MESSAGE", response.getStatus());
            }
            log.info("Leave from keycloak registration----!");
            return keycloakResponse;
        } catch (Exception e) {
            keycloakResponse.put("CODE", Constants.RESPONSE_CODE.FAILED);
            keycloakResponse.put("MESSAGE", "User creation failed");
            log.info("Exception occurred at registerUserInKeycloak()...{}", e.toString());
        }
        return keycloakResponse;
    }

    private void rollBack(String keyCloackUserId) {
        keycloak.realm(realm).users().delete(keyCloackUserId);
    }

    private void rollBackVerification(UserAuth userAuth, UserResource userResource) {
        if (!userAuth.equals(null)) {
            userAuth.setStatus(Constants.STATUS.PENDING);
            authUserRepository.save(userAuth);
        }
        if (userResource != null && !userResource.equals(null)) {
            UserRepresentation userRepresentation = userResource.toRepresentation();
            userRepresentation.setEnabled(false);
            userResource.update(userRepresentation);
        }
    }

}
