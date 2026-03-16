package com.ajudaqui.authenticationms.service.sqs;

import com.ajudaqui.authenticationms.dto.ApplicationSqsMessage;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsService {

  private static final Logger logger = LoggerFactory.getLogger(SqsService.class);
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

  public void sendMessage(String authorization, ApplicationSqsMessage message) {
    System.out.println("authorization "+authorization);
    JsonObject payload = message.fromJson();

    // String applicationFilaName = queueService.getNameFileByApplication(payload.get("name").getAsString());
    String applicationFilaName = queueService.getNameFileByApplication("bill-manager-register-a5979793-2ccd-4adf-b1f8-276e104eb954");
    System.out.println();
    System.out.println("applicationFilaName "+applicationFilaName);
    System.out.println();
    String queueUrl = queueService.checkinfFile(authorization, applicationFilaName);
    System.out.println("------------------------------------");
    System.out.println("payload");
    System.out.println(payload.toString());
    System.out.println("------------------------------------");
    SendMessageRequest request = SendMessageRequest.builder()
        .queueUrl(queueUrl)
        // .messageBody(sqsMessage.get("payload").toString())
        .messageBody(payload.toString())
        .build();

    System.out.println(payload);
    sqsClient.sendMessage(request);
    logger.info("Mensagem enviada para a fila: {}", applicationFilaName);
  }
}
