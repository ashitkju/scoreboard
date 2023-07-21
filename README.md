## Scoreboard App

Following the guidelines I created a Simple java application which is a vs code project.

3rdParty Libraries used -> Junits and Lombok

The Project mainly provides two classes which will come handy
1. FootballScoreboardImpl.java
    This class is an implementation of ScoreBoard.java and it provides several methods to perform the actions which were mentioned in the problem statement. We can implement ScoreBoard.java to create scoreboard for other games as well.

2. ScoreSortedByTotalAndTimestamp.java
    This class is a simple custom implementatio of Comparator to sort the scoreboard in a specified order. I chose to keep it separate to it gives flexibility to implement more sorting alogorithms freely.

Following TDD approach i wrote the test cases for the Service class before the actual implementation. You can run the tests to see the solution working correctly.

## Final note
As mentioned in the problem statement, i tried to keep it as simple as possible. No fancy maven/gradle or spring boot project. Just to the point implementation. Do let me know any valuable feedback on my solution.