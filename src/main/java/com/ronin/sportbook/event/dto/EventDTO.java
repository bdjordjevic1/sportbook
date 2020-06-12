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

package com.ronin.sportbook.event.dto;

import com.ronin.sportbook.user.dto.UserDTOList;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class EventDTO implements Serializable {

    private Long id;
    private Date time;
    private String weekDay;
    private UserDTOList users;
}
