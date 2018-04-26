package database.sql_builder

/**
 * @author Josiah Kendall
 */
class Builder() {

    private var selector = "*"
    private var orderBy = "id"
    private var where = ""
    private var default = selector
    private var limit = ""
    private var tableName = ""

    /**
     * The select that we want to retrieve from the database.
     * @param stat The name of the select column as it appears in the database
     * @return an instance of the builder that it was called upon, with the select set.
     */
    fun select(stat : String) : Builder {
        this.selector = stat
        return this
    }

    fun tableName(tableName : String) : Builder {
        this.tableName = tableName
        return this
    }

    /**
     * Return the max for the given select.
     * @return an instance of the builder that it was called upon, with the select set.
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

    fun orderBy(orderBy : String) : Builder {
        this.orderBy =orderBy
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
                "where $where " +
                "Order By $orderBy DESC " +
                limit

    }
}