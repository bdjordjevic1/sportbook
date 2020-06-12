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

package com.ronin.sportbook.common.storage.service;

import com.google.common.base.Splitter;
import com.ronin.sportbook.common.storage.exceptions.StorageException;
import com.ronin.sportbook.common.storage.exceptions.StorageFileNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service("storageService")
public class FileSystemStorageService implements StorageService {

    @Value("#{T(java.nio.file.Paths).get('${media.folder.root.location}')}")
    private final Path rootLocation = null;
    @Value("${media.random.uuid.length}")
    private int randomUUIDLength = 0;
    @Value("${media.folder.name.length}")
    private int mediaFolderNameLength = 0;

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String randomUUID = RandomStringUtils.randomAlphanumeric(randomUUIDLength).toLowerCase();
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                    "Cannot store file with relative path outside current directory "
                        + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, createFolderStructure(randomUUID).resolve(filename),
                           StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return randomUUID;
    }

    private Path createFolderStructure(String randomUUID) throws IOException {
        String folderStructurePath = getFolderStructurePath(randomUUID);
        Path folderStructure = this.rootLocation.resolve(folderStructurePath);
        Files.createDirectories(folderStructure);
        return folderStructure;
    }

    @Override
    public Path load(String filename, String context) {
        String folderStructurePath = getFolderStructurePath(context);
        return this.rootLocation.resolve(folderStructurePath).resolve(filename);
    }

    private String getFolderStructurePath(String context) {
        return String.join("/", Splitter.fixedLength(mediaFolderNameLength).splitToList(context));
    }

    @Override
    public Resource loadAsResource(String filename, String context) {
        try {
            Path file = load(filename, context);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                    "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }
}

