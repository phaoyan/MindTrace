package pers.juumii.MindTrace.model.data;

public enum Level implements Comparable<Level>{
    LOWEST, LOWER, MIDDLE, HIGHER, HIGHEST;
    public static Level getByString(String str){
        return switch (str){
            case "lowest" -> LOWEST;
            case "lower" -> LOWER;
            case "middle" -> MIDDLE;
            case "higher" -> HIGHER;
            case "highest" -> HIGHEST;
            default -> null;
        };
    }

    public static Level getByInt(int nextInt) {
        assert nextInt >= 0 && nextInt <=4;
        return switch (nextInt){
            case 0 -> LOWEST;
            case 1 -> LOWER;
            case 2 -> MIDDLE;
            case 3 -> HIGHER;
            case 4 -> HIGHEST;
            default -> null;
        };
    }

    public static Level getByPercents(int percent){
        return percent == 100? HIGHEST : getByInt(percent / 20);
    }
}
