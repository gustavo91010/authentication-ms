package com.ajudaqui.porteiro.controller;

import java.util.List;

import com.ajudaqui.porteiro.dto.ApplicationSqsMessage;
import com.ajudaqui.porteiro.response.ApiResponseList;
import com.ajudaqui.porteiro.response.MessageResponse;
import com.ajudaqui.porteiro.service.sqs.QueueService;
import com.ajudaqui.porteiro.service.sqs.SqsService;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sqs")
@RegisterReflectionForBinding({ApplicationSqsMessage.class, MessageResponse.class, ApiResponseList.class})
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
