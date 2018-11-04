package noh.jinil.boot.controller

import noh.jinil.boot.data.ResponseListData
import noh.jinil.boot.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File

@Controller
@RequestMapping("/")
class MediaController {

    @Value("\${static.scan.path}")
    private val staticScanPath: String? = null

    @Autowired
    private val environment: Environment? = null

    @RequestMapping("/scan/files")
    @ResponseBody
    fun getScanFiles(): ResponseListData<List<File>> {
        logger.info("getScanFiles() ->path:$staticScanPath")
        environment?.activeProfiles?.forEach {
            logger.info("->environment:$it")
        }

        return ResponseListData<List<File>>().apply {
            result = "success"
            data = FileUtils.getMediaFileList(staticScanPath)
            size = data?.size
        }
    }
}

private val logger = LoggerFactory.getLogger(MediaController::class.java)

