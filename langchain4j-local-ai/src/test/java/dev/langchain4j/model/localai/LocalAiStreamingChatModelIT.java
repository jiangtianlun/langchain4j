package dev.langchain4j.model.localai;

import static dev.langchain4j.model.output.FinishReason.STOP;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;

class LocalAiStreamingChatModelIT {

    StreamingChatModel model = LocalAiStreamingChatModel.builder()
            .baseUrl("http://localhost:8082/v1")
            .modelName("gpt-4")
            .maxTokens(3)
            .logRequests(true)
            .logResponses(true)
            .build();

    @Test
    void should_stream_answer_and_return_response() throws Exception {

        // given
        String userMessage = "hello";

        // when
        StringBuilder answerBuilder = new StringBuilder();
        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        model.chat(userMessage, new StreamingChatResponseHandler() {

            @Override
            public void onPartialResponse(String partialResponse) {
                answerBuilder.append(partialResponse);
            }

            @Override
            public void onPartialReasoningResponse(final String partialReasoningResponse) {}

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                futureResponse.complete(completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
            }
        });

        ChatResponse response = futureResponse.get(30, SECONDS);
        String streamedAnswer = answerBuilder.toString();

        // then
        assertThat(streamedAnswer).isNotBlank();

        AiMessage aiMessage = response.aiMessage();
        assertThat(aiMessage.text()).isEqualTo(streamedAnswer);

        assertThat(response.tokenUsage()).isNotNull();
        assertThat(response.finishReason()).isEqualTo(STOP);
    }
}
