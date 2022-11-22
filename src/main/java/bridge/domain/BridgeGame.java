package bridge.domain;

import java.util.ArrayList;
import java.util.List;

import bridge.constant.Direction;
import bridge.dto.TrialResult;

public class BridgeGame {

    private final Bridge bridge;
    private Player player;
    private final List<TrialResult> trialResults = new ArrayList<>();

    private BridgeGame(Bridge bridge) {
        this.bridge = bridge;
        this.player = new Player(bridge);
    }

    static BridgeGame fromDirections(List<Direction> directions) {
        return new BridgeGame(Bridge.fromDirections(directions));
    }

    public static BridgeGame from(List<String> capitalLetters) {
        return new BridgeGame(Bridge.from(capitalLetters));
    }

    public TrialResult move(Direction direction) {
        boolean moved = player.moveNext(direction);
        TrialResult trialResult = new TrialResult(direction, moved);
        trialResults.add(trialResult);
        return trialResult;
    }

    public void retry() {
        player = new Player(bridge);
        trialResults.clear();
    }

    public boolean isFinished() {
        return player.isArrived();
    }

    public List<TrialResult> getTrialResults() {
        return new ArrayList<>(trialResults);
    }
}
