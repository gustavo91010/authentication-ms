package com.ajudaqui.authenticationms.controller;

import java.util.List;

import com.ajudaqui.authenticationms.response.ApiResponseList;
import com.ajudaqui.authenticationms.response.MessageResponse;
import com.ajudaqui.authenticationms.service.sqs.QueueService;
import com.ajudaqui.authenticationms.service.sqs.SqsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sqs")
public class SqsController {

  @Autowired
  private SqsService sqsProducerService;

  @Autowired
  private QueueService queueService;

  @PostMapping("/send-message/{fila}")
  public MessageResponse senMessage(@PathVariable(required = true) String fila, @RequestBody String message) {
    sqsProducerService.sendMessage(fila, message);
    return new MessageResponse("Messagem enviada com sucesso!");
  }

  @PostMapping("/queue-create")
  public MessageResponse createQueue(@RequestParam String queueName) {

    String response = queueService.createQueue(queueName);
    return new MessageResponse(response);
  }

  @GetMapping("/queue-list")
  public ApiResponseList queueList() {
    List<String> response = queueService.queueList();
    return new ApiResponseList(response);
  }

  @DeleteMapping("/queue-delete")
  public MessageResponse delete(@RequestParam String queueName) {
    String response = queueService.deleteQueue(queueName);
    return new MessageResponse(response);

  }

}
