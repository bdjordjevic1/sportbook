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
import com.ronin.sportbook.common.security.payload.ApiResponse;
import com.ronin.sportbook.user.dto.UserDTO;
import com.ronin.sportbook.user.dto.UserDTOList;
import com.ronin.sportbook.user.model.UserModel;
import com.ronin.sportbook.repository.UserRepository;
import com.ronin.sportbook.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/users")
public class UsersController extends BaseApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
            return new ApiResponse(true, "Your profile has been successfully updated");
        }
        throw new InvalidDataAccessApiUsageException("User can not be updated!");
    }

//    @PostMapping
//    public UserDTO createUser(@Valid @RequestBody UserModel user) {
//        return getMapper().map(userRepository.save(user), UserDTO.class);
//    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserModel user) {
        System.out.println(user);
        return getMapper().map(userRepository.save(user), UserDTO.class);
    }


}
