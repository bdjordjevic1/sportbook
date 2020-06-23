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

package com.ronin.sportbook.common.controller;

import lombok.Getter;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

@Getter
public class BaseApiController {

    @Autowired
    private Mapper mapper;

    @Autowired
    private MessageSource messageSource;
}
