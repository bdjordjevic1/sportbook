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

package com.ronin.sportbook.media.controller;

import com.ronin.sportbook.controller.BaseApiController;
import com.ronin.sportbook.media.model.MediaModel;
import com.ronin.sportbook.media.service.MediaService;
import com.ronin.sportbook.storage.service.StorageService;
import com.ronin.sportbook.utility.FileUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/medias")
public class MediasController extends BaseApiController {

    @Resource
    private MediaService mediaService;

    @Resource
    private StorageService storageService;

    @GetMapping(value = "/{name:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public void getMedia(HttpServletResponse response, @PathVariable String name, @RequestParam("context") String context) throws IOException {
        response.setContentType(FileUtils.getMimeType(name));
        StreamUtils.copy(storageService.loadAsResource(name, context).getInputStream(), response.getOutputStream());
    }

    @PostMapping
    public ResponseEntity<String> handleMediaUpload(@RequestParam("media") MultipartFile media) {
        String randomMediaUUID = storageService.store(media);
        String filename = StringUtils.cleanPath(media.getOriginalFilename());
        mediaService.save(filename, randomMediaUUID);
        return ResponseEntity.ok("Media has been successfully uploaded");
    }
}
