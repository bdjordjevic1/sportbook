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

package com.ronin.sportbook.controller;

import com.ronin.sportbook.model.EventModel;
import com.ronin.sportbook.model.UserModel;
import com.ronin.sportbook.repository.EventRepository;
import com.ronin.sportbook.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/api/events")
public class EventsController extends BaseApiController {

    @Resource
    private EventRepository eventRepository;

    @GetMapping
    public List<EventModel> getAllEvents() {
        return eventRepository.findAll();
    }

    @PostMapping
    public EventModel createEvent(@RequestBody EventModel event) {
        return eventRepository.save(event);
    }


}
