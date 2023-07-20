package main.service.scoreboard;

import java.util.List;

import main.model.impl.FootballMatch;

public interface FootballScoreboard extends Scoreboard {
    public long startMatch(String homeTeamName, String awayTeamName);
    public String updateScore(long matchId, int homeTeamScore, int awayTeamScore);
    public String finishMatch(long matchId);
    public List<FootballMatch> showScoreBoard();
}
