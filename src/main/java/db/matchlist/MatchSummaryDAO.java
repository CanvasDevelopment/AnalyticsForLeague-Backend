package db.matchlist;

import model.matchlist.MatchSummary;

/**
 * @author Josiah Kendall
 */
public interface MatchSummaryDAO {

    int saveMatchSummary(MatchSummary matchSummary);
    MatchSummary getMatchSummary(long gameId);

}
