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

package com.ronin.sportbook.user.service;

import com.ronin.sportbook.repository.PasswordResetTokenRepository;
import com.ronin.sportbook.user.data.UserModelList;
import com.ronin.sportbook.user.dto.UserDTO;
import com.ronin.sportbook.user.model.PasswordResetTokenModel;
import com.ronin.sportbook.user.model.UserModel;
import com.ronin.sportbook.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

@Service("userService")
public class DefaultUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserModelList findAll() {
        List<UserModel> users = userRepository.findAll();
        UserModelList userModelList = new UserModelList();
        userModelList.setUsers(users);
        return userModelList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(
            () -> new UsernameNotFoundException(StringUtils.join("User with username :", username)));
    }

    @Override
    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (UserModel) authentication.getPrincipal();
    }

    @Override
    @Transactional
    public void updateUser(UserDTO user) {
        userRepository.updateUser(user.getFirstName(), user.getLastName(), user.getId());
    }

    @Override
    public PasswordResetTokenModel createPasswordResetTokenForUser(UserModel user) {

        PasswordResetTokenModel tokenModel = new PasswordResetTokenModel(user, UUID.randomUUID().toString(), getTokenExpirationTime());
        return passwordResetTokenRepository.save(tokenModel);
    }

    @Override
    public void changePassword(UserModel user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    private Date getTokenExpirationTime() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }
}
