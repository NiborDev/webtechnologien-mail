package de.thm.mni.mail_backend.storage

import de.thm.mni.mail_backend.config.StorageProperties
import de.thm.mni.mail_backend.exceptions.StorageException
import de.thm.mni.mail_backend.exceptions.StorageFileNotFoundException
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

/**
 * This Service implements the physically file storage for binary data.
 * It follows the example on: https://spring.io/guides/gs/uploading-files
 */

@Service
class FileStorageService(properties: StorageProperties) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val rootLocation: Path = Paths.get(properties.location).toAbsolutePath().normalize()

    @PostConstruct
    fun init() {
        try {
            Files.createDirectories(rootLocation)
            logger.info("Storage initialized at: {}", rootLocation)
        } catch (e: IOException) {
            logger.error("Failed to initialize storage directory", e)
            throw StorageException("Could not initialize storage", e)
        }
    }

    /**
     * Stores the file on the storage and returns the path
     *
     */
    fun store(file: MultipartFile): Path {
        if (file.isEmpty) {
            logger.warn("Attempted to store empty file: {}", file.originalFilename)
            throw StorageException("File is empty")
        }
        
        val orgFilename = file.originalFilename ?: "attachment"
        // Generates unique präfix, to avoid name conflicts
        val destFilename= "${UUID.randomUUID()}_$orgFilename"
        val destFile = rootLocation.resolve(destFilename).normalize()

        if (destFile.parent != rootLocation) {
            logger.error("File path traversal attempt detected for file: {}", orgFilename)
            throw StorageException("Cannot store file outside of storage")
        }

        try {
            file.inputStream.use { inputStream -> Files.copy(inputStream, destFile, StandardCopyOption.REPLACE_EXISTING) }
            logger.debug("File stored successfully: {} ({}bytes)", destFilename, file.size)
            return destFile
        } catch (e: IOException) {
            logger.error("Error storing file: {}", file.originalFilename, e)
            throw StorageException("Error storing file ${file.originalFilename}", e)
        }
    }

    /**
     * Load the File to download it
     */
    fun loadAsResource(filePathString: String): Resource {
        try {
            val file = Paths.get(filePathString)
            val resource = UrlResource(file.toUri())
            if (resource.exists() || resource.isReadable) {
                logger.debug("File loaded successfully: {}", filePathString)
                return resource
            }else {
                logger.warn("File not readable or does not exist: {}", filePathString)
                throw StorageFileNotFoundException("Could not read file: $filePathString")
            }
        }catch (e: Exception) {
            logger.error("Error loading file resource: {}", filePathString, e)
            throw StorageFileNotFoundException("Could not read file: $filePathString", e)
        }
    }

    /**
     * Deletes the file at the given path from the file storage.
     */
    fun delete(filePathString: String) {
        try {
            val file = Paths.get(filePathString)
            val deleted = Files.deleteIfExists(file)
            if (deleted) {
                logger.debug("File deleted successfully: {}", filePathString)
            } else {
                logger.warn("File to delete was not found on disk: {}", filePathString)
            }
        } catch (e: IOException) {
            logger.error("Error deleting file: {}", filePathString, e)
            throw StorageException("Could not delete file: $filePathString", e)
        }
    }

}
