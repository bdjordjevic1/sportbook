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

package com.ronin.sportbook.service;

import com.ronin.sportbook.data.UserModelList;
import com.ronin.sportbook.model.UserModel;
import com.ronin.sportbook.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

@Service("userService")
public class DefaultUserService implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserModelList findAll() {
        List<UserModel> users = userRepository.findAll();
        UserModelList userModelList = new UserModelList();
        userModelList.setUsers(users);
        return userModelList;
    }
}
