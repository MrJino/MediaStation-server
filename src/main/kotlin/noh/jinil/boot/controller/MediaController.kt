package noh.jinil.boot.controller

import noh.jinil.boot.data.MediaData
import noh.jinil.boot.response.ResponseData
import noh.jinil.boot.data.ScanFileData
import noh.jinil.boot.service.MediaService
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
import javax.annotation.PostConstruct

@Controller
@RequestMapping("/api")
class MediaApiController {

    @Value("\${static.data.path}")
    private val staticDataPath: String? = null

    @Value("\${static.tool.path}")
    private val staticToolPath: String? = null

    @Value("\${static.scan.path}")
    private val staticScanPath: String? = null

    @Autowired
    private val environment: Environment? = null

    @Autowired
    private val mediaService: MediaService? = null

    @Suppress("unused")
    @PostConstruct
    fun init() {
        environment?.activeProfiles?.forEach {
            logger.info("!!!Environment:$it")
        }

        mediaService?.run {
            scanPath = staticScanPath
            dataPath = staticDataPath
            toolPath = staticToolPath
        }
    }

    @GetMapping("/scan/files")
    @ResponseBody
    fun getScanFiles(): ResponseData<List<ScanFileData>> {
        logger.info("getScanFiles()")

        val dataList = ArrayList<ScanFileData>()
        FileUtils.getMediaFileList(staticScanPath)?.forEach { file ->
            dataList.add(ScanFileData().apply {
                name = file.name
            })
        }

        return ResponseData.create(dataList)
    }

    @PostMapping("/scan/files")
    @ResponseBody
    fun doScanFiles(): ResponseData<MediaData> {
        logger.info("doScanFiles()")

        val dataList = MediaData().apply {
            imageList = ArrayList()
            videoList = ArrayList()
        }

        mediaService?.scanFiles()

        return ResponseData.create(dataList)
    }
}

private val logger = LoggerFactory.getLogger(MediaApiController::class.java)

