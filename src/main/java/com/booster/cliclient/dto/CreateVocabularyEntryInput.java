package com.booster.cliclient.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;

// todo: consider creating a separate 'api' project that contains all API contracts
@Getter
@Setter
@Accessors(chain = true)
public class CreateVocabularyEntryInput {
    private String name;
    private String description;
    private Collection<String> synonyms;
}
