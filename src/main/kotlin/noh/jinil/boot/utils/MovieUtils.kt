package noh.jinil.boot.utils

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
}