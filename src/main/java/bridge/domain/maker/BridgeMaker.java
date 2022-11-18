package bridge.domain.maker;

import java.util.ArrayList;
import java.util.List;

import bridge.constant.Direction;

/**
 * 다리의 길이를 입력 받아서 다리를 생성해주는 역할을 한다.
 */
public class BridgeMaker {

    private static final int MINIMUM_SIZE = 3;
    private static final int MAXIMUM_SIZE = 20;

    private final BridgeNumberGenerator bridgeNumberGenerator;

    public BridgeMaker(BridgeNumberGenerator bridgeNumberGenerator) {
        this.bridgeNumberGenerator = bridgeNumberGenerator;
    }

    /**
     * @param size 다리의 길이
     * @return 입력받은 길이에 해당하는 다리 모양. 위 칸이면 "U", 아래 칸이면 "D"로 표현해야 한다.
     */
    public List<String> makeBridge(int size) {
        validateSize(size);
        List<String> bridge = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            bridge.add(makeCapitalLetter());
        }
        return bridge;
    }

    private void validateSize(int size) {
        if (size < MINIMUM_SIZE || size > MAXIMUM_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private String makeCapitalLetter() {
        int bridgeNumber = bridgeNumberGenerator.generate();
        return Direction.from(bridgeNumber)
                .map(Direction::capitalLetter)
                .orElseThrow(() -> new IllegalStateException("다리 생성은 정해진 숫자로만 해야합니다"));
    }
}
