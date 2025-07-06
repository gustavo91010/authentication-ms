package com.ajudaqui.authenticationms.service.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsService {

  @Value("${aws.id}")
  private String awsId;

  @Value("${aws.region}")
  private String region;

  @Value("${aws.fila.names}")
  private String applicationNames;

  private final SqsClient sqsClient;
  private final QueueService queueService;

  public SqsService(SqsClient sqsClient, QueueService queueService) {
    this.sqsClient = sqsClient;
    this.queueService = queueService;
  }

  public void sendMessage(String aplpicationName, String messageBody) {

    String applicationFilaName = queueService.getNameFileByApplication(aplpicationName);
    String queueUrl = queueService.checkinfFile(applicationFilaName);
    SendMessageRequest request = SendMessageRequest.builder()
        .queueUrl(queueUrl)
        .messageBody(messageBody)
        .build();

    sqsClient.sendMessage(request);
  }
}
