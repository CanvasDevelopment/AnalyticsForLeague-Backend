package model

data class SyncProgress(val total : Int,
                        val completed : Int) {
    override fun toString(): String {
        return "{" +
                "total : $total," +
                "completed : $completed" +
                "}"
    }
}