package main.java.logic;

public enum FigureColor {
    WHITE_PAWN(Group.WHITE),
    WHITE_QUEEN(Group.WHITE),
    BLACK_PAWN(Group.BLACK),
    BLACK_QUEEN(Group.BLACK),
    EMPTY_FIELD(Group.EMPTY);

    private Group group;

    public enum Group {
        WHITE,
        BLACK,
        EMPTY
    }

    public boolean isInGroup(Group group) {
        return this.group == group;
    }

    FigureColor(Group group) {
        this.group = group;
    }
}
