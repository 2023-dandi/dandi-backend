package dandi.dandi.member.application.dto;

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
