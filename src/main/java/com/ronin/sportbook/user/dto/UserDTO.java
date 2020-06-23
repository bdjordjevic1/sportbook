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

package com.ronin.sportbook.user.dto;

import com.ronin.sportbook.media.dto.MediaDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserDTO implements Serializable {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private MediaDTO profilePicture;
}
