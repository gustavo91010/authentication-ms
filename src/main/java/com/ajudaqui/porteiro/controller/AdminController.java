// package com.ajudaqui.porteiro.controller;

// import java.security.Principal;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

// import com.ajudaqui.porteiro.entity.Users;
// import com.ajudaqui.porteiro.entity.UsersAppData;
// import com.ajudaqui.porteiro.repository.UsersAppDataRepository;
// import com.ajudaqui.porteiro.repository.UsersRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.client.RestTemplate;

// import software.amazon.awssdk.services.sqs.SqsClient;
// import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
// import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;
// import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
// import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

// @Controller
// @RequestMapping("/admin")
// public class AdminController {

//   @Autowired
//   private UsersRepository usersAppDataRepository;

//   @Autowired(required = false)
//   private SqsClient sqsClient;

//   @Value("${aws.fila.names:}")
//   private String queueNames;

//   private final RestTemplate restTemplate = new RestTemplate();

//   @GetMapping("/dashboard")
//   public String dashboard(Model model, Principal principal) {
//     String email = principal.getName();

//     // Busca as aplicações onde o usuário logado é MODERADOR
//     Users userApps = usersAppDataRepository.findByEmail(email);

//     List<Map<String, Object>> apps = userApps.getUsersAppData().stream()
//         .filter(ua -> ua.getRoles().stream().anyMatch(r -> r.getName().name().equals("ROLE_MODERATOR")))
//         .map(ua -> ua.getApplications())
//         .map(app -> {
//           Map<String, Object> appMap = new HashMap<>();
//           appMap.put("id", app.getId());
//           appMap.put("name", app.getAppId());
//           appMap.put("clientId", app.getClientId());
//           appMap.put("redirectUrl", app.getRedirectUrl());
//           appMap.put("registerUrl", app.getRegisterUrl());

//           try {
//             if (app.getRegisterUrl() != null && !app.getRegisterUrl().isEmpty()) {
//               restTemplate.getForEntity(app.getRegisterUrl(), String.class);
//               appMap.put("online", true);
//             } else {
//               appMap.put("online", false);
//             }
//           } catch (Exception e) {
//             appMap.put("online", false);
//           }
//           return appMap;
//         }).collect(Collectors.toList());

//     model.addAttribute("applications", apps);

//     // Monitoramento de Filas (Geral do sistema)
//     List<Map<String, String>> queuesStatus = new ArrayList<>();
//     if (queueNames != null && !queueNames.isEmpty() && sqsClient != null) {
//       String[] names = queueNames.split(",");
//       for (String name : names) {
//         Map<String, String> status = new HashMap<>();
//         status.put("name", name.trim());
//         try {
//           String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(name.trim()).build())
//               .queueUrl();
//           GetQueueAttributesResponse attributes = sqsClient.getQueueAttributes(GetQueueAttributesRequest.builder()
//               .queueUrl(queueUrl)
//               .attributeNames(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES)
//               .build());
//           status.put("messages", attributes.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES));
//           status.put("online", "true");
//         } catch (Exception e) {
//           status.put("messages", "N/A");
//           status.put("online", "false");
//         }
//         queuesStatus.add(status);
//       }
//     }
//     model.addAttribute("queues", queuesStatus);

//     return "admin/dashboard";
//   }
// }
