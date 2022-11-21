package bridge.controller;

import java.util.ArrayList;
import java.util.List;

import bridge.domain.BridgeGame;
import bridge.domain.maker.BridgeMaker;
import bridge.domain.maker.BridgeRandomNumberGenerator;
import bridge.dto.TrialResult;
import bridge.view.InputView;
import bridge.view.OutputView;

public class Controller {

    private final InputView inputView;
    private final OutputView outputView;
    private final List<TrialResult> trialResults = new ArrayList<>();
    private BridgeGame game;

    public Controller(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void play() {
        this.game = makeGame();
        int trialCount = doGame();
        outputView.printResult(trialResults, trialCount, game.isFinished());
    }

    private BridgeGame makeGame() {
        int bridgeSize = inputView.readBridgeSize();
        BridgeMaker bridgeMaker = new BridgeMaker(new BridgeRandomNumberGenerator());
        List<String> bridge = bridgeMaker.makeBridge(bridgeSize);
        return BridgeGame.from(bridge);
    }

    private int doGame() {
        if (game.isFinished()) {
            return 0;
        }
        if (!move().wasSuccessful()) {
            return retryOrExit();
        }
        return doGame();
    }

    private TrialResult move() {
        String moving = inputView.readMoving();
        TrialResult trialResult = game.move(moving);
        trialResults.add(trialResult);
        outputView.printMap(trialResults);
        return trialResult;
    }

    private int retryOrExit() {
        if (askForRetry()) {
            game.retry();
            trialResults.clear();
            return doGame() + 1;
        }
        return 0;
    }

    private boolean askForRetry() {
        String command = inputView.readGameCommand();
        return command.equals("R");
    }
}
