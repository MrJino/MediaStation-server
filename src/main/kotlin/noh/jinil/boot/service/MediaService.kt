package noh.jinil.boot.service

import noh.jinil.boot.entity.Video
import noh.jinil.boot.define.DBField
import noh.jinil.boot.repository.VideoRepository
import noh.jinil.boot.utils.FileUtils
import noh.jinil.boot.utils.MovieUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File

@Component("MediaService")
class MediaService {

    @Autowired
    private val videoRepository: VideoRepository? = null

    var scanPath: String? = null
    var dataPath: String? = null
    var toolPath: String? = null

    fun scanFiles() {
        val videoCount = FileUtils.findContentsCountViaFolder("$dataPath${getSourcePath(MediaType.VIDEO)}", MAX_FILE_COUNT_IN_FOLDER)
        logger.info("--current video count:$videoCount")

        FileUtils.getMediaFileList(scanPath)?.forEach { file ->
            if (MovieUtils.isMovieFormat(file.path)) {
                val path = String.format("%06d/", (videoCount / MAX_FILE_COUNT_IN_FOLDER * MAX_FILE_COUNT_IN_FOLDER))
                insertFileIntoSystem(MediaType.VIDEO, null, file, path)
            }
        }
    }

    private fun insertFileIntoSystem(type: MediaType, data: Any?, file: File, countPath: String): Boolean {
        logger.debug("insertFileIntoSystem() : ${file.name}")

        val sourceUri: String?
        val thumbsUri: String?
        val metaMap: HashMap<String, String>?

        when (data) {
            is Video -> {
                sourceUri = data.sourceUri
                thumbsUri = data.thumbsUri
            }
            else -> {
                sourceUri = FileUtils.checkDuplicateName(dataPath, "${getSourcePath(type)}$countPath", file.name)
                thumbsUri = FileUtils.checkDuplicateName(dataPath, "${getThumbsPath(type)}$countPath", FileUtils.changeExtName(file.name, type.thumbExt))
            }
        }

        when (type) {
            MediaType.VIDEO -> {
                metaMap = MovieUtils.readMetaData(file, toolPath)
                if (metaMap == null) {
                    logger.error("metaMap is null!!")
                    return false
                }
                MovieUtils.extractThumb(file, File(dataPath+thumbsUri), metaMap, THUMBS_WIDTH, dataPath).let { success ->
                    if (!success) {
                        logger.error("extractThumb is failed!!")
                        return false
                    }
                }
            }
        }

        FileUtils.move(file, File(dataPath+sourceUri)).let { success ->
            if (!success) {
                logger.error("file move is failed!!")
                return false
            }
        }

        val mediaData = data ?: when (type) {
            MediaType.VIDEO -> Video()
        }

        when (mediaData) {
            is Video -> {
                mediaData.sourceUri = sourceUri
                mediaData.thumbsUri = thumbsUri
                mediaData.width = metaMap[DBField.KEY_WIDTH]?.toInt()
                mediaData.height = metaMap[DBField.KEY_HEIGHT]?.toInt()
                mediaData.tags = ""

                videoRepository?.save(mediaData)
            }
        }

        return true
    }

    private fun getSourcePath(type: MediaType): String = "${type.folder}$SOURCE_FOLDER"
    private fun getThumbsPath(type: MediaType): String = "${type.folder}$THUMBS_FOLDER"
}

enum class MediaType(val folder: String, val thumbExt: String) {
    VIDEO("video/", "gif")
}

private val logger = LoggerFactory.getLogger(MediaService::class.java)
private const val SOURCE_FOLDER = "source/"
private const val THUMBS_FOLDER = "thumbs/"
private const val THUMBS_WIDTH = 256
private const val MAX_FILE_COUNT_IN_FOLDER = 1000