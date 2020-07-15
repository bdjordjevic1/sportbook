/*********************************************************************
 * The Initial Developer of the content of this file is NETCONOMY.
 * All portions of the code written by NETCONOMY are property of
 * NETCONOMY. All Rights Reserved.
 *
 * NETCONOMY Software & Consulting GmbH
 * Hilmgasse 4, A-8010 Graz (Austria)
 * FN 204360 f, Landesgericht fuer ZRS Graz
 * Tel: +43 (316) 815 544
 * Fax: +43 (316) 815544-99
 * www.netconomy.net
 *
 * (c) 2020 by NETCONOMY Software & Consulting GmbH
 *********************************************************************/

package com.ronin.sportbook.user.controller;

import com.ronin.sportbook.common.controller.BaseApiController;
import com.ronin.sportbook.common.exception.ModelNotFoundException;
import com.ronin.sportbook.common.security.payload.ApiResponse;
import com.ronin.sportbook.common.storage.service.StorageService;
import com.ronin.sportbook.media.dto.MediaDTO;
import com.ronin.sportbook.media.model.MediaModel;
import com.ronin.sportbook.media.service.MediaService;
import com.ronin.sportbook.user.dto.ChangePasswordDTO;
import com.ronin.sportbook.user.dto.ResetPasswordDTO;
import com.ronin.sportbook.user.dto.UserDTO;
import com.ronin.sportbook.user.dto.UserDTOList;
import com.ronin.sportbook.user.exception.PasswordResetTokenExpiredException;
import com.ronin.sportbook.user.model.PasswordResetTokenModel;
import com.ronin.sportbook.user.model.UserModel;
import com.ronin.sportbook.repository.UserRepository;
import com.ronin.sportbook.user.service.CustomUserDetailsService;
import com.ronin.sportbook.user.service.EmailService;
import com.ronin.sportbook.user.service.PasswordResetTokenService;
import com.ronin.sportbook.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/users")
public class UsersController extends BaseApiController {

    public static final String HEADER_ORIGIN = "origin";
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private StorageService storageService;

    @GetMapping
    public UserDTOList getAllUsers() {
        return getMapper().map(userService.findAll(), UserDTOList.class);
    }

    @GetMapping("/current")
    public UserDTO getCurrentUser() {
        return getMapper().map(userService.getCurrentUser(), UserDTO.class);
    }

    @PatchMapping("/current/update")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse updateCurrentUser(@RequestBody UserDTO user) {
        UserModel userPrincipal = userService.getCurrentUser();
        if (StringUtils.equals(user.getEmail(), userPrincipal.getEmail()) && user.getId().equals(userPrincipal.getId())) {
            userService.updateUser(user);
            return new ApiResponse(true,
                                   getMessageSource().getMessage("user.profile.update.success", null, Locale.ENGLISH),
                                   "user.profile.update.success");
        }
        throw new InvalidDataAccessApiUsageException("User can not be updated!");
    }

    @PostMapping("/change-profile-picture")
    @ResponseStatus(HttpStatus.OK)
    public MediaDTO handleMediaUpload(@RequestParam("media") MultipartFile media) {
        UserModel userPrincipal = userService.getCurrentUser();
        String randomMediaUUID = storageService.store(media);
        String filename = org.springframework.util.StringUtils.cleanPath(media.getOriginalFilename());
        MediaModel profilePicture = mediaService.save(filename, randomMediaUUID);
        userPrincipal.setProfilePicture(profilePicture);
        userRepository.save(userPrincipal);
        return getMapper().map(profilePicture, MediaDTO.class);
    }

    @PostMapping("/reset-password")
    public ApiResponse sendForgotPasswordEmail(HttpServletRequest request, @RequestBody ResetPasswordDTO resetPasswordDTO) {
        if (!userRepository.existsByEmail(resetPasswordDTO.getEmail())) {
            return new ApiResponse(false, 
                                   getMessageSource().getMessage("email.passwordReset.invalidEmail", null, Locale.ENGLISH),
                                   "email.passwordReset.invalidEmail");
        }
        UserModel user = (UserModel) customUserDetailsService.loadUserByUsername(resetPasswordDTO.getEmail());
        PasswordResetTokenModel token = userService.createPasswordResetTokenForUser(user);
        SimpleMailMessage message =
            emailService.constructResetTokenEmail(request.getHeader(HEADER_ORIGIN), Locale.ENGLISH, token.getToken(), user);

        javaMailSender.send(message);
        return new ApiResponse(true,
                               getMessageSource().getMessage("email.passwordReset.successfullySent", null, Locale.ENGLISH),
                               "email.passwordReset.successfullySent");
    }

    @GetMapping("/validate-token")
    public ApiResponse validateToken(@RequestParam String token) throws PasswordResetTokenExpiredException, ModelNotFoundException {
        passwordResetTokenService.validate(token);
        return new ApiResponse(true,
                               getMessageSource().getMessage("passwordResetToken.valid", null, Locale.ENGLISH),
                               "passwordResetToken.valid");
    }

    @PostMapping("/change-password")
    public ApiResponse changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO)
        throws ModelNotFoundException, PasswordResetTokenExpiredException {
        PasswordResetTokenModel token = passwordResetTokenService.findByToken(changePasswordDTO.getToken());
        passwordResetTokenService.validate(token);
        userService.changePassword(token.getUser(), changePasswordDTO.getPassword());
        passwordResetTokenService.removeUsedToken(token);
        return new ApiResponse(true,
                               getMessageSource().getMessage("user.changePassword.success", null, Locale.ENGLISH),
                               "user.changePassword.success");
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserModel user) {
        return getMapper().map(userRepository.save(user), UserDTO.class);
    }


}
