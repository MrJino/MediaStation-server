package noh.jinil.boot.response

class ResponseData<T> {
    var message: String? = null
    var code: Int? = null
    var data: T? = null

    companion object {
        fun <T> create(dataT: T): ResponseData<T> {
            return ResponseData<T>().apply {
                code = ResponseCode.RESPONSE_CODE_SUCCESS
                message = null
                data = dataT
            }
        }
    }
}