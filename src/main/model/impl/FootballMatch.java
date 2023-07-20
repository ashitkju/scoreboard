package main.model.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.model.Match;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class FootballMatch implements Match {
    long id;
    String homeTeamName;
    String awayTeamName;
    int homeTeamScore;
    int awayTeamScore;
    long matchCreatedTime;
}
