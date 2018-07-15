package model.response_beans

class SyncProgress {
    var total : Int = 0
    var completed : Int = 0

    override fun toString(): String {
        return "{" +
                "total : $total," +
                "completed : $completed" +
                "}"
    }
}