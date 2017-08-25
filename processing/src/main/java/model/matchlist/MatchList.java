package model.matchlist;

import java.util.ArrayList;

/**
 * @author Josiah Kendall
 *
 * The pojo wrapper for the match list object.
 * Associated Url:
 * https://oc1.api.riotgames.com/lol/match/v3/matchlists/by-account/{accountId}
 */
public class MatchList {

    private ArrayList<MatchSummary> matches;
    private int startIndex;
    private int endIndex;
    private int totalGames;

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public void setMatches(ArrayList<MatchSummary> matches) {
        this.matches = matches;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public ArrayList<MatchSummary> getMatches() {
        return matches;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getTotalGames() {
        return totalGames;
    }
}
