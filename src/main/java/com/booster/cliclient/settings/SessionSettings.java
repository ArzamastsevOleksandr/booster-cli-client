package com.booster.cliclient.settings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionSettings {

    public int notesBatchSize() {
        return 5;
    }

    public int vocabularyEntriesBatchSize() {
        return 5;
    }

}
