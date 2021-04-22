package api

class FaceRegServerData(code: String, faceRegData: FaceRegData, status: String) {

    private var code = code
    private var data = faceRegData
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
    fun getData (): FaceRegData {
        return data
    }
    fun setData (faceRegData: FaceRegData){
        this.data = faceRegData
    }
}