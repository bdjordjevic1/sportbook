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

package com.ronin.sportbook.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ronin.sportbook.model.ResponseTypeModel;
import com.ronin.sportbook.user.model.UserModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "recurring_events")
@Getter
@Setter
public class RecurringEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private EventModel parent;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date time;

    private String status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "event_user_response_mapping",
               joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "response_type_id", referencedColumnName = "name")})
    @MapKeyJoinColumn(name = "user_id")
    private Map<UserModel, ResponseTypeModel> userResponseMap;
}
