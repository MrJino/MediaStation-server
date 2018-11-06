package noh.jinil.boot.controller

import noh.jinil.boot.data.MediaData
import noh.jinil.boot.data.ResponseData
import noh.jinil.boot.data.ScanFileData
import noh.jinil.boot.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/api")
class MediaApiController {

    @Value("\${static.scan.path}")
    private val staticScanPath: String? = null

    @Value("\${static.scan.url}")
    private val staticScanUrl: String? = null

    @Autowired
    private val environment: Environment? = null

    @GetMapping("/scan/files")
    @ResponseBody
    fun getScanFiles(): ResponseData<List<ScanFileData>> {
        logger.info("getScanFiles() ->path:$staticScanPath")
        environment?.activeProfiles?.forEach {
            logger.info("->environment:$it")
        }

        val dataList = ArrayList<ScanFileData>()
        FileUtils.getMediaFileList(staticScanPath)?.forEach { file ->
            dataList.add(ScanFileData().apply {
                name = file.name
                url = staticScanUrl + file.name
            })
        }

        return ResponseData.create(dataList)
    }

    @PostMapping("/scan/files")
    @ResponseBody
    fun requestScanFiles(): ResponseData<List<MediaData>> {
        logger.info("requestScanFiles()")

        val dataList = ArrayList<MediaData>()

        return ResponseData.create(dataList)
    }
}

private val logger = LoggerFactory.getLogger(MediaApiController::class.java)

