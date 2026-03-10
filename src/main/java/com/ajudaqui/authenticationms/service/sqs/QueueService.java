package com.ajudaqui.authenticationms.service.sqs;

import java.util.*;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.exception.BadRequestException;
import com.ajudaqui.authenticationms.exception.MessageException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

@Service
public class QueueService {
  @Autowired
  private SqsClient sqsClient;

  @Value("${aws.fila.names}")
  private String queueNames;

  @Value("${app.auth.master}")
  private String auth_master;

  public String createQueue(String authorization, String queueName) {
    checkingPermission(authorization);

    if (queueName == null || queueName.isEmpty()) {
      return "Erro: Nome da fila está vazio.";
    }
    if (queueName.length() > 80) {
      return "Erro: Nome da fila é muito longo.";
    }
    if (!queueName.matches("^[a-zA-Z0-9-_]+$")) {
      return "Erro: Nome da fila contém caracteres inválidos.";
    }
    CreateQueueRequest createStandardQueueRequest = CreateQueueRequest.builder()
        .queueName(queueName)
        .build();

    return sqsClient.createQueue(createStandardQueueRequest).queueUrl();
  }

  public String checkinfFile(String authorization, String fileName) {

    return queueList(authorization).stream()
        .filter(queleName -> queleName.equals(fileName))
        .findFirst()
        .orElse(createQueue(authorization, fileName));
  }

  @SuppressWarnings("unchecked")
  public List<String> queueList(String authorization) {

    checkingPermission(authorization);
    List<String> response = new ArrayList<>();

    ListQueuesResponse listQueues = sqsClient.listQueues();
    Optional<?> queueUrls = listQueues.getValueForField("QueueUrls", List.class);

    if (queueUrls.isPresent())
      response = (List<String>) queueUrls.get();
    return response;
  }

  public String deleteQueue(String authorization, String queueName) {
    List<String> queueList = queueList(authorization);
    for (String urlSqs : queueList) {
      if (nameFile(urlSqs).equals(queueName)) {
        DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder().queueUrl(urlSqs).build();
        sqsClient.deleteQueue(deleteQueueRequest);
        return String.format("Lista de nome: %s foi deletada com sucesso!", queueName);
      }

    }
    return String.format("Lista de nome: %s não localizada", queueName);
  }

  public Set<String> queueNameList(String authorization) {
    return queueList(authorization).stream()
        .map(url -> nameFile(url))
        .collect(Collectors.toSet());
  }

  private HashSet<String> queueLisApplication() {
    String[] queue = queueNames.split(",");
    return new HashSet<>(Arrays.asList(queue));
  }

  public String getNameFileByApplication(String awsFila) {
    checkingPermission(authorization);
    return queueLisApplication().stream()
        .filter(queueName -> queueName.contains(awsFila))
        .findFirst()
        .orElse(awsFila);
  }

  private String nameFile(String urlSqs) {
    String[] split = urlSqs.split("/");
    return split[4];
  }

  private void checkingPermission(String authorization) {
    System.out.println("auth_master " + auth_master);
    System.out.println("authorization " + authorization);
    if (!auth_master.equals(authorization))
      throw new BadRequestException("Solicitação não autorizada!");
  }
}
