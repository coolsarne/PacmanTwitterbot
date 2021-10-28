package model;

/**
 * Arne Cools
 * 27/10/2021
 */
public enum FieldCellStatus {
    VWALL('|'), HWALL('―'), FREE(' '), FOOD('*'), PLAYER('P'), TLCORNER('⌜'), TRCORNER('⌝'), BLCORNER('⌞'), BRCORNER('⌟'), WALL('■');

    private FieldCellStatus(char value){
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
