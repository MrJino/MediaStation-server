package noh.jinil.boot.utils

import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

object FileUtils {
    fun getMediaFileList(dirPath: String?): List<File>? {
        logger.info("getMediaFileList() ->$dirPath")
        if (dirPath == null) {
            return null
        }

        File(dirPath).listFiles { pathname ->
            MovieUtils.isMovieFormat(pathname.name)
        }?.run {
            Arrays.sort(this)
            forEach { file ->
                logger.debug("->file:${file.name}")
            }
            return this.toList()
        }

        return null
    }

    fun findContentsCountViaFolder(path: String, maxValue: Int): Int {
        var countViaFolder = 0
        while (true) {
            val file = File(path + String.format("%06d", countViaFolder))
            if (!file.exists()) {
                return countViaFolder
            }

            file.list().run {
                if (isEmpty() || size < maxValue) {
                    return countViaFolder + size
                }
            }
            countViaFolder += maxValue
        }
    }

    fun checkDuplicateName(dataPath: String?, folder: String, name: String): String {
        var rename = name
        while (File(dataPath+folder+rename).exists()) {
            rename = "_$rename"
        }
        return folder+rename
    }

    fun changeExtName(name: String, ext: String): String {
        return name.substring(0, name.lastIndexOf(".") + 1) + ext
    }

    fun move(srcFile: File, destFile: File): Boolean {
        if (srcFile == destFile) {
            logger.debug("->this is same file copy request, ignore")
            return true
        }

        if (!destFile.parentFile.exists()) {
            if (!destFile.parentFile.mkdirs())
                return false
        }

        if (!srcFile.exists())
            return false

        srcFile.renameTo(destFile)
        return true
    }
}

private val logger = LoggerFactory.getLogger(FileUtils::class.java)