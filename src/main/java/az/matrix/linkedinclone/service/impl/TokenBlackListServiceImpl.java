package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.service.TokenBlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TokenBlackListServiceImpl implements TokenBlackListService {
    private final Set<String> blackListedTokens = ConcurrentHashMap.newKeySet();

    @Override
    public void addToBlackList(String email) {
        log.info("User added to black list");
        blackListedTokens.add(email);
    }

    @Override
    public boolean isBlackListed(String email) {
        return blackListedTokens.contains(email);
    }

    @Override
    public void deleteFromBlackList(String email) {
        log.info("User deleted from black list");
        blackListedTokens.remove(email);
    }
}
