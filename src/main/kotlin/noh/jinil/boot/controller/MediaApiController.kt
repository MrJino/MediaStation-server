package noh.jinil.boot.controller

import noh.jinil.boot.data.MediaData
import noh.jinil.boot.data.ScanFileData
import noh.jinil.boot.response.ResponseData
import noh.jinil.boot.service.MediaService
import noh.jinil.boot.utils.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.annotation.PostConstruct

@Controller
@RequestMapping("/api/media")
class MediaApiController {

    @Value("\${static.path}")
    private val staticPath: String? = null

    private lateinit var staticDataPath: String
    private lateinit var staticToolPath: String
    private lateinit var staticScanPath: String

    @Autowired
    private val environment: Environment? = null

    @Autowired
    private val mediaService: MediaService? = null

    @Suppress("unused")
    @PostConstruct
    fun init() {
        /*
        environment?.activeProfiles?.forEach {
            logger.info("!!!Environment---------------------$it")
        }
        */

        staticScanPath = "$staticPath/scan"
        staticDataPath = "$staticPath/data"
        staticToolPath = "$staticPath/tool"

        mediaService?.run {
            scanPath = staticScanPath
            dataPath = staticDataPath
            toolPath = staticToolPath
        }
    }

    @GetMapping("/scan/files")
    @ResponseBody
    fun getScanFiles(@RequestHeader(value = "X-Authorization-Firebase") xAuth: String): ResponseData<List<ScanFileData>> {
        logger.info("getScanFiles()")
        logger.info("->xAuth:$xAuth")

        val dataList = ArrayList<ScanFileData>()
        FileUtils.getMediaFileList(staticScanPath)?.forEach { file ->
            dataList.add(ScanFileData().apply {
                name = file.name
                uri = "/scan/${file.name}"
            })
        }

        return ResponseData.createSuccess(dataList)
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

        return ResponseData.createSuccess(dataList)
    }
}

private val logger = LoggerFactory.getLogger(MediaApiController::class.java)

