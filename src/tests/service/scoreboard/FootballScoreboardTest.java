package tests.service.scoreboard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.model.impl.FootballMatch;
import main.service.scoreboard.FootballScoreboard;
import main.service.scoreboard.impl.FootballScoreboardImpl;
import main.util.comparator.ScoreSortedByTotalAndTimestamp;

import static main.constants.MessageConstant.MATCH_NOT_FOUND;

public class FootballScoreboardTest {
    public FootballScoreboard footballScoreboard;

    @Before
    public void init() {
        footballScoreboard = new FootballScoreboardImpl(new ScoreSortedByTotalAndTimestamp());
    }

    @Test
    public void startMatch() throws IllegalArgumentException, IllegalAccessException, InterruptedException {
        footballScoreboard.startMatch("MEXICO", "CANNADA");
        footballScoreboard.startMatch("SPAIN", "BRAZIL");
        footballScoreboard.startMatch("GERMANY", "FRANCE");
        footballScoreboard.startMatch("URUGUAY", "ITALY");
        footballScoreboard.startMatch("ARGENTINA", "AUSTRALIA");

        var datastore = Arrays.stream(FootballScoreboardImpl.class.getDeclaredFields())
            .filter(field -> field.getName().equals("datastore")).findFirst().get();
        datastore.setAccessible(true);
        Assert.assertEquals(5, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).size());
    }

        @Test
    public void startMatch_negative_scenario() throws IllegalArgumentException, IllegalAccessException, InterruptedException {
        footballScoreboard.startMatch("", "");
        footballScoreboard.startMatch("   ", "BRAZIL");
        footballScoreboard.startMatch("GERMANY", "  ");
        footballScoreboard.startMatch(null, "ITALY");
        footballScoreboard.startMatch("ARGENTINA", null);

        var datastore = Arrays.stream(FootballScoreboardImpl.class.getDeclaredFields())
            .filter(field -> field.getName().equals("datastore")).findFirst().get();
        datastore.setAccessible(true);
        Assert.assertEquals(0, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).size());
    }

    @Test
    public void updateScore() throws IllegalArgumentException, IllegalAccessException {
        var m = footballScoreboard.startMatch("MEXICO", "CANNADA");
        footballScoreboard.updateScore(m, 1, 5);
        var datastore = Arrays.stream(FootballScoreboardImpl.class.getDeclaredFields())
            .filter(field -> field.getName().equals("datastore")).findFirst().get();
        datastore.setAccessible(true);
        Assert.assertEquals(1, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).get(m).getHomeTeamScore());
        Assert.assertEquals(5, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).get(m).getAwayTeamScore());
        footballScoreboard.updateScore(m, 10, 15);
        Assert.assertEquals(10, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).get(m).getHomeTeamScore());
        Assert.assertEquals(15, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).get(m).getAwayTeamScore());
    }
    
        @Test
    public void updateScore_negative_scenario() throws IllegalArgumentException, IllegalAccessException {
        var m = footballScoreboard.startMatch("MEXICO", "CANNADA");
        footballScoreboard.updateScore(m, 5, 10);
        footballScoreboard.updateScore(m, -1, 0);
        var datastore = Arrays.stream(FootballScoreboardImpl.class.getDeclaredFields())
            .filter(field -> field.getName().equals("datastore")).findFirst().get();
        datastore.setAccessible(true);
        Assert.assertEquals(5, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).get(m).getHomeTeamScore());
        Assert.assertEquals(10, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).get(m).getAwayTeamScore());
    }

    @Test
    public void finishMatch() throws IllegalArgumentException, IllegalAccessException {
        var m = footballScoreboard.startMatch("MEXICO", "CANNADA");
        var n = footballScoreboard.startMatch("INDIA", "USA");
        var datastore = Arrays.stream(FootballScoreboardImpl.class.getDeclaredFields())
            .filter(field -> field.getName().equals("datastore")).findFirst().get();
        var history = Arrays.stream(FootballScoreboardImpl.class.getDeclaredFields())
            .filter(field -> field.getName().equals("history")).findFirst().get();
        datastore.setAccessible(true);
        history.setAccessible(true);
        Assert.assertEquals(2, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).size());
        Assert.assertEquals(0, ((List<FootballMatch>) history.get(footballScoreboard)).size());
        footballScoreboard.finishMatch(m);
        Assert.assertEquals(1, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).size());
        Assert.assertEquals(1, ((List<FootballMatch>) history.get(footballScoreboard)).size());
        footballScoreboard.finishMatch(n);
        Assert.assertEquals(0, ((Map<Long, FootballMatch>) datastore.get(footballScoreboard)).size());
        Assert.assertEquals(2, ((List<FootballMatch>) history.get(footballScoreboard)).size());
    }

    @Test
    public void finishMatch_negative_scenario() throws IllegalArgumentException, IllegalAccessException {
        var m = footballScoreboard.startMatch("MEXICO", "CANNADA");
        Assert.assertEquals(MATCH_NOT_FOUND.formatted(m), footballScoreboard.finishMatch(0));
    }

    @Test
    public void showScoreBoard() throws InterruptedException {
        var m = footballScoreboard.startMatch("MEXICO", "CANNADA");
        Thread.sleep(100);
        var s = footballScoreboard.startMatch("SPAIN", "BRAZIL");
        Thread.sleep(100);
        var g = footballScoreboard.startMatch("GERMANY", "FRANCE");
        Thread.sleep(100);
        var u = footballScoreboard.startMatch("URUGUAY", "ITALY");
        Thread.sleep(100);
        var a = footballScoreboard.startMatch("ARGENTINA", "AUSTRALIA");
        Thread.sleep(100);
        var i = footballScoreboard.startMatch("INDIA", "JAPAN");
        footballScoreboard.updateScore(m, 0, 5);
        footballScoreboard.updateScore(s, 10, 2);
        footballScoreboard.updateScore(g, 2, 2);
        footballScoreboard.updateScore(u, 6, 6);
        footballScoreboard.updateScore(a, 3, 1);
        footballScoreboard.updateScore(i, 8, 4);


        //footballScoreboard.showScoreBoard().forEach(System.out::println);
        
        
        var ids = footballScoreboard.showScoreBoard().stream().map(ob -> ob.getId()).collect(Collectors.toList()).toArray(Long[]::new);
        Assert.assertArrayEquals(List.of(i,u,s,m,a,g).toArray(Long[]::new), ids);
    }
}
