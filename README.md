# Blackjack Simulator

This is a simple Blackjack simulator.

Current version supports player vs. dealer, random AI and strategy AI.

Strategies are defined as .csv file in the /strategies directory.

# TODOs:

- [x] Add Split plays
- [x] Add betting
- [x] Implement simulator with statistics logging
- [x] Usage of different strategies
  - [x] Play strategies
  - [x] Betting strategies
- [x] Experimentation with different deck sizes

# Some interesting preliminary results

#### Chance of winning a game while choosing random plays every time?

~26%

#### Does number of decks matter if choosing randomly?

No.

#### Detailed results

| Decks | Games     | Wins    | Losses  | Pushes | Splits | Winrate | Strategy          |
|-------|-----------|---------|---------|--------|--------|---------|-------------------|
| 1     | 1 million | 260775  | 706058  | 47118  | 13951  | 26.6%   | Random            |
| 1     | 5 million | 1290884 | 3555542 | 239946 | 86372  | 26.2%   | Random            |
| 4     | 1 million | 257785  | 711280  | 48161  | 17226  | 26.6%   | Random            |
| 4     | 5 million | 1292855 | 3553547 | 240021 | 86423  | 26.2%   | Random            |
| 4     | 1 million | 394420  | 534911  | 92844  | 22175  | 41.45%  | baseline_strategy |
| 4     | 5 million | 1971617 | 2674981 | 464458 | 111056 | 41.44%  | baseline_strategy |


Winrate calculation ignores pushes and treats splits as an additional game (player is playing two hands in a single game).
It is calculated as:
```
Winrate = (Wins + Losses + Splits) / Losses
```


