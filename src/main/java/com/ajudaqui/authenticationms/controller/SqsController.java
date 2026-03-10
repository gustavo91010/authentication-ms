package com.ajudaqui.authenticationms.controller;

import java.util.List;

import com.ajudaqui.authenticationms.dto.ApplicationSqsMessage;
import com.ajudaqui.authenticationms.response.ApiResponseList;
import com.ajudaqui.authenticationms.response.MessageResponse;
import com.ajudaqui.authenticationms.service.sqs.QueueService;
import com.ajudaqui.authenticationms.service.sqs.SqsService;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sqs")
public class SqsController {

  @Autowired
  private SqsService sqsProducerService;

  @Autowired
  private QueueService queueService;

  @PostMapping("/send-message/{fila}")
  public MessageResponse senMessage(@RequestHeader("Authorization") String authorization,
      @RequestBody ApplicationSqsMessage sqsMessage) {
    sqsProducerService.sendMessage(authorization, sqsMessage);
    return new MessageResponse("Messagem enviada com sucesso!");
  }

  @PostMapping("/queue-create")
  public MessageResponse createQueue(@RequestHeader("Authorization") String authorization,
      @RequestParam String queueName) {
    String response = queueService.createQueue(authorization, queueName);
    return new MessageResponse(response);
  }

  @GetMapping("/queue-list")
  public ApiResponseList queueList(@RequestHeader("Authorization") String authorization) {
    List<String> response = queueService.queueList(authorization);
    return new ApiResponseList(response);
  }

  @DeleteMapping("/queue-delete")
  public MessageResponse delete(@RequestHeader("Authorization") String authorization, @RequestParam String queueName) {
    String response = queueService.deleteQueue(authorization, queueName);
    return new MessageResponse(response);
  }

}
