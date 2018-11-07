package noh.jinil.boot.utils

import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader


object CommandUtils {

    @Throws(Exception::class)
    fun doProcess(cmd: String): String? {

        var commands: Array<String>?
        System.getProperty("os.name").toLowerCase().run {
            commands = when {
                contains("win") -> arrayOf("cmd.exe", "/C", cmd)
                contains("linux") -> arrayOf("/bin/bash", "-c", cmd)
                contains("mac") -> arrayOf("/bin/bash", "-c", cmd)
                else -> return null
            }
        }

        var process: Process? = null
        val processMsg = StringBuilder()
        var reader: BufferedReader? = null

        val start = System.currentTimeMillis()
        try {
            process = Runtime.getRuntime().exec(commands)
            process!!.waitFor()

            val exit = process.exitValue()
            reader = if (exit == 0) {
                BufferedReader(InputStreamReader(process.inputStream))
            } else {
                BufferedReader(InputStreamReader(process.errorStream))
            }
            while (reader.ready()) {
                processMsg.append(reader.readLine() + "\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            process?.destroy()
            reader?.close()
            val end = System.currentTimeMillis()
            logger.info("Time to command elased:" + String.format("%,d", end - start) + "ms")
        }
        return processMsg.toString()
    }
}

private val logger = LoggerFactory.getLogger(CommandUtils::class.java)