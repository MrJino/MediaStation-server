package noh.jinil.boot.utils

import java.io.File
import java.util.*

object FileUtils {
    fun getMediaFileList(dirPath: String?): List<File>? {
        if (dirPath == null) {
            return null
        }

        File(dirPath).listFiles { pathname ->
            MovieUtils.isMovieFormat(pathname.name)
        }?.run {
            Arrays.sort(this)
            return this.toList()
        }

        return null
    }
}