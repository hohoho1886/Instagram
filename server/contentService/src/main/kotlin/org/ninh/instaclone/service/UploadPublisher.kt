package org.ninh.instaclone.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.ProjectTopicName
import com.google.pubsub.v1.PubsubMessage
import org.ninh.instaclone.dto.UploadRequestFromUser
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import org.springframework.validation.annotation.Validated
import java.util.concurrent.TimeUnit

@Service
@Validated
class UploadPublisher(
    @param:Value($$"${spring.cloud.gcp.project-id}")
    private val projectId: String,
    @param:Value($$"${spring.cloud.gcp.topic}")
    private val topic: String
) {
    private val objectMapper = jacksonObjectMapper()

    fun publish(uploadRequest: UploadRequestFromUser){
        val messageJson = objectMapper.writeValueAsString(uploadRequest)

        val topicName = ProjectTopicName.of(projectId, topic)
        val pubsubMessage = PubsubMessage.newBuilder()
            .setData(ByteString.copyFromUtf8(messageJson))
            .build()

        val publisher: Publisher = Publisher.newBuilder(topicName).build()
        try {
            val messageId = publisher.publish(pubsubMessage).get()
            println("Published message with ID: $messageId")
        } finally {
            publisher.shutdown()
            publisher.awaitTermination(1, TimeUnit.MINUTES)
        }
    }
}