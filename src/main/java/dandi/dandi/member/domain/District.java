package dandi.dandi.member.domain;

public class District {

    private final String first;
    private final String second;

    public District(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public District(String first) {
        this(first, null);
    }
}
