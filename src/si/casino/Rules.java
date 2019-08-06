package si.casino;

public class Rules {

    public final boolean DEALER_STANDS_ON_SOFT_17;
    public final boolean DOUBLE_AFTER_SPLIT;
    // TODO: public final int RESPLIT_LIMIT;
    // TODO: public final boolean RESPLIT_ACES;
    public final boolean HIT_SPLIT_ACES;
    // TODO: public final boolean LOSE_ONLY_ORIGINAL_BET_AGAINST_BJ;
    // TODO: public final boolean ALLOW_SURRENDER;
    // TODO: public final float BLACKJACK_PAYOUT;

    public Rules() {
        DEALER_STANDS_ON_SOFT_17 = true;
        DOUBLE_AFTER_SPLIT = true;
        // TODO: RESPLIT_LIMIT;
        // TODO: RESPLIT_ACES = true;
        HIT_SPLIT_ACES = false;
        // TODO: LOSE_ONLY_ORIGINAL_BET_AGAINST_BJ;
        // TODO: ALLOW_SURRENDER;
        // TODO: BLACKJACK_PAYOUT;
    }

    public Rules(
            boolean dealerStandsOnSoft17,
            boolean doubleAfterSplit,
            boolean hitSplitAces
    ) {
        DEALER_STANDS_ON_SOFT_17 = dealerStandsOnSoft17;
        DOUBLE_AFTER_SPLIT = doubleAfterSplit;
        //RESPLIT_ACES = resplitAces;
        HIT_SPLIT_ACES = hitSplitAces;
    }

}
