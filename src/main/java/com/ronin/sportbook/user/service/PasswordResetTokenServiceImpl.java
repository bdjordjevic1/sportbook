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
import com.ronin.sportbook.repository.PasswordResetTokenRepository;
import com.ronin.sportbook.user.exception.PasswordResetTokenExpiredException;
import com.ronin.sportbook.user.model.PasswordResetTokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@Service("passwordResetTokenService")
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public PasswordResetTokenModel findByToken(String token) throws ModelNotFoundException {
        return passwordResetTokenRepository.findByToken(token)
                                           .orElseThrow(() -> new ModelNotFoundException(
                                               messageSource.getMessage("passwordResetToken.notFound", null, Locale.ENGLISH)));
    }

    @Override
    public boolean validate(String token) throws PasswordResetTokenExpiredException, ModelNotFoundException {
        PasswordResetTokenModel tokenModel = this.findByToken(token);
        return isValid(tokenModel);
    }

    @Override
    public boolean validate(PasswordResetTokenModel token) throws PasswordResetTokenExpiredException {
        return isValid(token);
    }

    @Override
    public void removeUsedToken(PasswordResetTokenModel token) {
        passwordResetTokenRepository.delete(token);
    }

    private boolean isValid(PasswordResetTokenModel tokenModel) throws PasswordResetTokenExpiredException {
        if (new Date().after(tokenModel.getExpiryDate())) {
            throw new PasswordResetTokenExpiredException(
                messageSource.getMessage("passwordResetToken.expired", null, Locale.ENGLISH));
        }
        return true;
    }
}
