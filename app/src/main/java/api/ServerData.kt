package api

class ServerData(code: String, data: DataX, status: String) {

    private var code = code
    private var data = data
    private var status = status
    // get and set code
    fun getCode (): String {
        return code
    }
    fun setCode (code: String){
        this.code = code
    }

    // get and set status
    fun getStatus (): String {
        return status
    }
    fun setStatus (status: String){
        this.status = status
    }

    // get and set token
    fun getData (): DataX {
        return data
    }
    fun setData (data: DataX){
        this.data = data
    }
}