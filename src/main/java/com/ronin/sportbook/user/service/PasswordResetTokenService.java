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

import com.ronin.sportbook.common.exception.ModelNotFoundException;
import com.ronin.sportbook.user.exception.PasswordResetTokenExpiredException;
import com.ronin.sportbook.user.model.PasswordResetTokenModel;

public interface PasswordResetTokenService {

    PasswordResetTokenModel findByToken(String token) throws ModelNotFoundException;

    boolean validate(String token) throws PasswordResetTokenExpiredException, ModelNotFoundException;

    boolean validate(PasswordResetTokenModel token) throws PasswordResetTokenExpiredException;

    void removeUsedToken(PasswordResetTokenModel token);
}
