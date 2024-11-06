package dev.ai4j.openai4j;

import java.util.Collections;

class ApiKeyHeaderInjector extends GenericHeaderInjector {

    ApiKeyHeaderInjector(String apiKey) {
        super(Collections.singletonMap("api-key", apiKey));
    }
}
