package main.service.scoreboard.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import main.model.impl.FootballMatch;
import main.service.scoreboard.FootballScoreboard;
import main.util.comparator.ScoreSortedByTotalAndTimestamp;

import static main.constants.MessageConstant.INVALID_INPUT;
import static main.constants.MessageConstant.MATCH_FINISHED;
import static main.constants.MessageConstant.MATCH_NOT_FOUND;
import static main.constants.MessageConstant.SCORE_UPDATED;;

public class FootballScoreboardImpl implements FootballScoreboard {
    private final Map<Long, FootballMatch> datastore;
    private final List<FootballMatch> history;
    private final ScoreSortedByTotalAndTimestamp sortFunction;

    public FootballScoreboardImpl(ScoreSortedByTotalAndTimestamp sortFunction) {
        datastore = new HashMap<>();
        history = new ArrayList<>();
        this.sortFunction = sortFunction;
    }

    @Override
    public long startMatch(String homeTeamName, String awayTeamName) {
        if (homeTeamName == null || awayTeamName == null || (homeTeamName != null && homeTeamName.isBlank()) ||
                (awayTeamName != null && awayTeamName.isBlank()))
                return -1;
        long uniqueId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        var newMatch = new FootballMatch(uniqueId, homeTeamName, awayTeamName, 0, 0, System.currentTimeMillis());
        datastore.put(uniqueId, newMatch);
        return uniqueId;
    }

    @Override
    public String updateScore(long matchId, int homeTeamScore, int awayTeamScore) {
        var match = datastore.get(matchId);
        if (match == null)
            return MATCH_NOT_FOUND.formatted(matchId);
        if (homeTeamScore < 0 || awayTeamScore < 0)
            return INVALID_INPUT; 
        match.setHomeTeamScore(homeTeamScore);
        match.setAwayTeamScore(awayTeamScore);
        return SCORE_UPDATED;
    }

    @Override
    public String finishMatch(long matchId) {
        var match = datastore.get(matchId);
        if (match == null)
            return MATCH_NOT_FOUND.formatted(matchId);
        history.add(match);
        datastore.remove(matchId);
        return MATCH_FINISHED;
    }

    @Override
    public List<FootballMatch> showScoreBoard() {
        return datastore.values().stream().sorted(sortFunction::compare).collect(Collectors.toList());
    }
    
}
