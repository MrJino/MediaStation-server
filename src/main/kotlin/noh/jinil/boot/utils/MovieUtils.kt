package noh.jinil.boot.utils

import noh.jinil.boot.domain.define.DBField
import noh.jinil.boot.parser.VideoMetadataReader
import java.io.File
import java.lang.Exception
import org.slf4j.LoggerFactory




object MovieUtils {
    fun isMovieFormat(filePath: String?): Boolean {
        filePath?.toLowerCase()?.let {
            return it.endsWith("mov") ||
                    it.endsWith("avi") ||
                    it.endsWith("mkv") ||
                    it.endsWith("3gp") ||
                    it.endsWith("webm") ||
                    it.endsWith("ts") ||
                    it.endsWith("wmv") ||
                    it.endsWith("mp4")
        }
    }

    fun extractThumb(sourceFile: File, thumbsFile: File, metaMap: HashMap<String, String>, scaledW: Int, toolPath: String?): Boolean {
        logger.debug("extractThumb()")

        var originW = metaMap[DBField.KEY_WIDTH]?.toInt()
        var originH = metaMap[DBField.KEY_HEIGHT]?.toInt()

        if (originW == null || originH == null) {
            return false
        }

        // swap
        when (metaMap[DBField.KEY_ORIENTATION]) {
            "90", "270" -> {
                val temp = originW
                originW = originH
                originH = temp
            }
        }

        // calculate scaled size
        val scaledH = originH * scaledW / originW

        // make directory if not exists
        if (!thumbsFile.parentFile.exists()) {
            if (!thumbsFile.parentFile.mkdirs())
                return false
        }

        // extract thumb image
        try {
            val command = getFFMPEGPath(toolPath)
            val options = " -i \"" + sourceFile.path + "\" -ss 00:00:01 -vframes 1 " + " -s " + scaledW + "x" + scaledH + " \"" + thumbsFile.path + "\""
            CommandUtils.doProcess(command + options)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun readMetaData(file: File, toolPath: String?): HashMap<String, String>? {
        logger.debug("readMetaData()")
        val result = try {
            val command = getFFMPEGPath(toolPath)
            val options = " -i \"${file.path}\""
            //logger.debug("->$command$options")
            CommandUtils.doProcess(command + options)
        } catch (e: Exception) {
            e.printStackTrace()
            "nothing"
        }

        val metaMap = HashMap<String, String>()
        VideoMetadataReader.parseMetadata(result).tagList.forEach { tag ->
            //logger.debug("->tag:$tag")
            when (tag.tagName) {
                "width" -> metaMap[DBField.KEY_WIDTH] = tag.tagDescription
                "height" -> metaMap[DBField.KEY_HEIGHT] = tag.tagDescription
                "date" -> metaMap[DBField.KEY_SHOOT_DATE] = tag.tagDescription
                "time" -> metaMap[DBField.KEY_SHOOT_TIME] = tag.tagDescription
                "entity" -> metaMap[DBField.KEY_MODEL] = tag.tagDescription
                "make" -> metaMap[DBField.KEY_MAKE] = tag.tagDescription
                "rotate" -> metaMap[DBField.KEY_ORIENTATION] = tag.tagDescription
            }
        }

        // width/height
        if (metaMap[DBField.KEY_WIDTH] == null || metaMap[DBField.KEY_HEIGHT] == null) {
            logger.error("width or height is null!!")
            return null
        }

        // Orientation
        if (!metaMap.containsKey(DBField.KEY_ORIENTATION)) {
            metaMap[DBField.KEY_ORIENTATION] = "0"
        }

        // Data/Time
        if (metaMap[DBField.KEY_SHOOT_DATE] == null) {
            metaMap[DBField.KEY_SHOOT_DATE] = "0000-00-00"
            metaMap[DBField.KEY_SHOOT_TIME] = "00:00:00"
        }

        // DEVICE CAMERA
        val cameraBuilder = StringBuilder()
        if (metaMap.containsKey(DBField.KEY_MAKE)) {
            cameraBuilder.append(metaMap[DBField.KEY_MAKE] + " ")
        }
        if (metaMap.containsKey(DBField.KEY_MODEL)) {
            cameraBuilder.append(metaMap[DBField.KEY_MODEL])
        }
        metaMap[DBField.KEY_CAMERA] = cameraBuilder.toString()

        /*
        for (key in metaMap.keys) {
            logger.debug("->" + key + ":" + metaMap[key])
        }
        */

        return metaMap
    }

    private fun getFFMPEGPath(toolPath: String?): String {
        File("${toolPath}ffmpeg/ffmpeg").run {
            return if (exists()) {
                path
            } else {
                "ffmpeg"
            }
        }
    }
}

private val logger = LoggerFactory.getLogger(MovieUtils::class.java)