package dev.ai4j.openai4j.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.*;

@JsonDeserialize(builder = JsonObjectSchema.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JsonObjectSchema extends JsonSchemaElement {

    @JsonProperty
    private final String description;
    @JsonProperty
    private final Map<String, JsonSchemaElement> properties;
    @JsonProperty
    private final List<String> required;
    @JsonProperty("additionalProperties")
    private final Boolean additionalProperties;

    public JsonObjectSchema(Builder builder) {
        super("object");
        this.description = builder.description;
        this.properties = new LinkedHashMap<>(builder.properties);
        this.required = new ArrayList<>(builder.required);
        this.additionalProperties = builder.additionalProperties;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        return another instanceof JsonObjectSchema
                && equalTo((JsonObjectSchema) another);
    }

    private boolean equalTo(JsonObjectSchema another) {
        return Objects.equals(description, another.description)
                && Objects.equals(properties, another.properties)
                && Objects.equals(required, another.required)
                && Objects.equals(additionalProperties, another.additionalProperties);
    }

    @Override
    public int hashCode() {
        int h = 5381;
        h += (h << 5) + Objects.hashCode(description);
        h += (h << 5) + Objects.hashCode(properties);
        h += (h << 5) + Objects.hashCode(required);
        h += (h << 5) + Objects.hashCode(additionalProperties);
        return h;
    }

    @Override
    public String toString() {
        return "JsonObjectSchema{" +
                "description=" + description +
                ", properties=" + properties +
                ", required=" + required +
                ", additionalProperties=" + additionalProperties +
                "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Builder {

        private String description;
        private Map<String, JsonSchemaElement> properties = new LinkedHashMap<>();
        private List<String> required = new ArrayList<>();
        private Boolean additionalProperties;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder properties(Map<String, JsonSchemaElement> properties) {
            this.properties = properties;
            return this;
        }

        public Builder required(List<String> required) {
            this.required = required;
            return this;
        }

        public Builder additionalProperties(Boolean additionalProperties) {
            this.additionalProperties = additionalProperties;
            return this;
        }

        public JsonObjectSchema build() {
            return new JsonObjectSchema(this);
        }
    }
}
