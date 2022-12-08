package bridge.constant;

import java.util.Arrays;

public enum GameCommand {
    RETRY("R"),
    EXIT("Q");

    private final String character;

    GameCommand(String character) {
        this.character = character;
    }

    public static GameCommand from(String command) {
        return Arrays.stream(values())
                .filter(gameCommand -> gameCommand.hasSameCharacterWith(command))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 명령입니다"));
    }

    private boolean hasSameCharacterWith(String command) {
        return character.equals(command);
    }

    public boolean isRetry() {
        return this.equals(RETRY);
    }

    public boolean isExit() {
        return this.equals(EXIT);
    }

    public String getCharacter() {
        return character;
    }
}
