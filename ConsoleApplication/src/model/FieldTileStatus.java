package model;

/**
 * Arne Cools
 * 27/10/2021
 */
public enum FieldTileStatus {
    FREE(' '),
    FOOD('*'),
    WALL('■'),
    LIVINGPLAYER('P'),
    DEADPLAYER('X'),
    LIVINGGHOST('G'),
    DEADGHOST('D');

    private FieldTileStatus(char value) {
        this.value = value;
    }

    private char value;

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
