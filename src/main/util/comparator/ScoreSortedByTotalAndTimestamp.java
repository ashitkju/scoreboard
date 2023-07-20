package main.util.comparator;

import java.util.Comparator;

import main.model.impl.FootballMatch;

public class ScoreSortedByTotalAndTimestamp implements Comparator<FootballMatch> {

    @Override
    public int compare(FootballMatch o1, FootballMatch o2) {
        int scoreDiff = (o2.getHomeTeamScore() + o2.getAwayTeamScore()) - (o1.getHomeTeamScore() + o1.getAwayTeamScore());
        if (scoreDiff == 0) {
            return o2.getMatchCreatedTime() - o1.getMatchCreatedTime() > 0 ? 1 : -1;
        }
        return scoreDiff;
    }

    
}
