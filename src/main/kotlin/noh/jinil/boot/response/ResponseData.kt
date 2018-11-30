package noh.jinil.boot.response

class ResponseData<T> {
    var message: String? = null
    var code: Int? = null
    var data: T? = null

    companion object {
        private fun <T> create(dataT: T?, resCode: Int?, resMessage: String?): ResponseData<T> {
            return ResponseData<T>().apply {
                code = resCode
                message = resMessage
                data = dataT
            }
        }

        fun <T> createSuccess(dataT: T): ResponseData<T> {
            return create(dataT, ResponseCode.RESPONSE_CODE_OK, "")
        }

        fun <T> createFailed(resCode: Int?, resMessage: String?): ResponseData<T> {
            return create(null, resCode, resMessage)
        }
    }
}