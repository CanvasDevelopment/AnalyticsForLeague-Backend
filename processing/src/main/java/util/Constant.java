package util;

import org.jetbrains.annotations.Nullable;

/**
 * @author Josiah Kendall
 */
public class Constant {
    public static final String DEFAULT_HOST = "jdbc:mysql://127.0.0.1:3306/lol_analytics?useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String DEFAULT_USERNAME = "root";
    public static final String DEFAULT_PASSWORD = "Idnw2bh2";

    public static final String PRODUCTION_NA_SQL_DB_INSTANCE = "analytics-gg:us-central1:analytics-gg";
    public static final String PRODUCTION_OCE_SQL_DB_INSTANCE = "analytics-gg:australia-southeast1:oce-analytics-gg";

    public static final Object DB_NAME = "lol_analytics";
}
