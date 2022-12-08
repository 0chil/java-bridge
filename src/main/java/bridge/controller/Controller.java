package bridge.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import bridge.BridgeMaker;
import bridge.BridgeRandomNumberGenerator;
import bridge.constant.Direction;
import bridge.constant.GameCommand;
import bridge.domain.BridgeGame;
import bridge.dto.TrialResult;
import bridge.view.InputView;
import bridge.view.OutputView;

public class Controller {
    private static final InputView INPUT_VIEW = new InputView();
    private static final OutputView OUTPUT_VIEW = new OutputView();
    private final List<TrialResult> trialResults = new ArrayList<>();

    public void run() {
        BridgeGame game = makeGame();
        doGame(game);
        OUTPUT_VIEW.printResult(trialResults, game.getTrialCount(), game.isFinished());
    }

    public BridgeGame makeGame() {
        return repeat(() -> BridgeGame.from(makeBridge()));
    }

    private List<String> makeBridge() {
        BridgeMaker bridgeMaker = new BridgeMaker(new BridgeRandomNumberGenerator());
        return bridgeMaker.makeBridge(INPUT_VIEW.readBridgeSize());
    }

    private void doGame(final BridgeGame game) {
        while (!game.isFinished()) {
            if (move(game).wasSuccessful()) {
                continue;
            }
            if (retryOrExit(game).isExit()) {
                break;
            }
        }
    }

    private GameCommand retryOrExit(final BridgeGame game) {
        GameCommand command = askCommand();
        if (command.isRetry()) {
            game.retry();
            trialResults.clear();
        }
        return command;
    }

    private TrialResult move(final BridgeGame game) {
        Direction direction = askDirection();
        boolean moved = game.move(direction);
        TrialResult trialResult = new TrialResult(direction, moved);

        trialResults.add(trialResult);
        OUTPUT_VIEW.printMap(trialResults);
        return trialResult;
    }

    private Direction askDirection() {
        return repeat(INPUT_VIEW::readDirection);
    }

    private GameCommand askCommand() {
        return repeat(INPUT_VIEW::readGameCommand);
    }

    private <T> T repeat(Supplier<T> inputSupplier) {
        try {
            return inputSupplier.get();
        } catch (IllegalArgumentException e) {
            OUTPUT_VIEW.printException(e.getMessage());
            return repeat(inputSupplier);
        }
    }
}
