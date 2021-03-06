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


import com.ronin.sportbook.user.data.UserModelList;
import com.ronin.sportbook.user.dto.UserDTO;
import com.ronin.sportbook.user.model.PasswordResetTokenModel;
import com.ronin.sportbook.user.model.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserModelList findAll();

    UserModel getCurrentUser();

    void updateUser(UserDTO user);

    PasswordResetTokenModel createPasswordResetTokenForUser(UserModel user);

    void changePassword(UserModel user, String password);
}
