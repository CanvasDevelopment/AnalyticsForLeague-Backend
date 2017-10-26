package database.sql_builder

/**
 * @author Josiah Kendall
 */
class Builder() {

    private var selector = "*"
    private var where = ""
    private var default = selector
    private var limit = ""
    private var tableName = ""

    /**
     * The stat that we want to retrieve from the database.
     * @param stat The name of the stat column as it appears in the database
     * @return an instance of the builder that it was called upon, with the stat set.
     */
    fun stat(stat : String) : Builder {
        this.selector = stat
        return this
    }

    fun tableName(tableName : String) : Builder {
        this.tableName = tableName
        return this
    }

    /**
     * Return the max for the given stat.
     * @return an instance of the builder that it was called upon, with the stat set.
     */
    fun max() : Builder {
        default = "max($selector)"
        return this
    }

    fun min() : Builder {
        default = "min($selector)"
        return this
    }

    fun avg() : Builder {
        default = "avg($selector)"
        return this
    }

    fun total() : Builder {
        default = "sum($selector)"
        return this
    }

    fun games(games : Int) : Builder {
        limit = "LIMIT $games"
        return this
    }

    fun where(whereClause : String) : Builder {
        where = whereClause
        return this
    }

    fun toSql() : String {
        return "SELECT $selector FROM $tableName " +
                "$where " +
                limit

    }
}