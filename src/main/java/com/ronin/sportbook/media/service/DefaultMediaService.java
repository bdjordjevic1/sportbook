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

package com.ronin.sportbook.media.service;

import com.ronin.sportbook.media.model.MediaModel;
import com.ronin.sportbook.media.repository.MediaRepository;
import com.ronin.sportbook.common.storage.service.StorageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import javax.annotation.Resource;

@Service("mediaService")
public class DefaultMediaService implements MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private StorageService storageService;

    @Override
    public MediaModel save(String filename, String context) {
        MediaModel media = new MediaModel();
        media.setName(filename);
        media.setContext(context);
        media.setUrl(StringUtils.join("/medias/", filename, "?context=", context));
        media.setRealPath(storageService.load(filename, context).toString());
        return mediaRepository.save(media);
    }

    @Override
    public MediaModel updateExistingMedia(MediaModel media) {
        return null;
    }

    @Override
    public ClassPathResource getImage(String name) {
        return new ClassPathResource("medias/" + name);
    }

    @Override
    public Path getPath(String filename, String context) {
        return null;
    }
}
