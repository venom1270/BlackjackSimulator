# BlackjackSimulator

This is a simple Blackjack simulator.

Current version supports only game vs. dealer.

# TODOs:

- [x] Add Split plays
- [ ] Add betting
- [x] Implement simulator with statistics logging
- [ ] Usage of different strategies
  - [ ] Play strategies
  - [ ] Betting strategies
- [ ] Experimentation with different deck sizes

# Some interesting preliminary results

#### Chance of winning a game while choosing random plays every time?

~26%

#### Does number of decks matter if choosing randomly?

No.

#### Detailed results

| Decks | Games     | Wins    | Losses  | Pushes | Splits | Winrate |
|-------|-----------|---------|---------|--------|--------|---------|
| 1     | 1 million | 260775  | 706058  | 47118  | 13951  | 26,6%   |
| 1     | 5 million | 1290884 | 3555542 | 239946 | 86372  | 26,2%   |
| 4     | 1 million | 257785  | 711280  | 48161  | 17226  | 26,6%   |
| 4     | 5 million | 1292855 | 3553547 | 240021 | 86423  | 26,2%   |

Winrate calculation ignores pushes and treats splits as an additional game (player is playing two hands in a single game).
It is calculated as:
```
Winrate = (Wins + Losses + Splits) / Losses
```