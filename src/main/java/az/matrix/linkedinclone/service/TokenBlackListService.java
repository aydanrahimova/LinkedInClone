package az.matrix.linkedinclone.service;

public interface TokenBlackListService {
    void addToBlackList(String email);

    boolean isBlackListed(String email);

    void deleteFromBlackList(String email);
}
