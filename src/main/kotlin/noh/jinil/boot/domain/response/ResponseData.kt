package noh.jinil.boot.domain.response

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
            return create(dataT, ResponseCode.OK, "")
        }

        fun <T> createFailed(resCode: Int?, resMessage: String?): ResponseData<T> {
            return create(null, resCode, resMessage)
        }

        fun <T> createExtra(resCode: Int?, resMessage: String?): ResponseData<T> {
            return create(null, resCode, resMessage)
        }
    }
}