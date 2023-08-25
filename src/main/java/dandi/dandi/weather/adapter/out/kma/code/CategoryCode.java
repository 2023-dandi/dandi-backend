package dandi.dandi.weather.adapter.out.kma.code;

public enum CategoryCode {

    POP,
    PTY,
    PCP,
    REH,
    SNO,
    SKY,
    TMP,
    TMN,
    TMX,
    UUU,
    VVV,
    WAV,
    VEC,
    WSD,
    ;

    public boolean isSameCategory(String category) {
        return this.name().equals(category);
    }
}
