package dandi.dandi.member.application.port.in;

public class NicknameDuplicationCheckResponse {

    private boolean duplicated;

    public NicknameDuplicationCheckResponse() {
    }

    public NicknameDuplicationCheckResponse(boolean duplicated) {
        this.duplicated = duplicated;
    }

    public boolean isDuplicated() {
        return duplicated;
    }
}
