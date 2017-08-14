package db.stats;

import model.stats.GameStageDelta;

/**
 * @author Josiah Kendall
 *
 * Game stage deltas are used for various stats. They help defince the per minute value for a game time frame -
 *  e.g:
 *  - 'zeroToTen' (Early Game)
 *  - 'tenToTwenty' (Mid Game)
 *  - 'twentyToThirty'(Late Game)
 *  - 'thirtyToEnd' (Over Time)
 */
public interface GameStageDeltasDAO {
    long saveDeltas(GameStageDelta gameStageDelta, String tableName, Long timelineId);
    void deleteDeltas(long gameStageDeltasId, String tableName);
    GameStageDelta getGameStageDeltas(long gameStageDeltas, String tableName);
}
