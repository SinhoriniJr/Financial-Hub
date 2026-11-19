package com.financialhub.notificationservice.interfaces.controller;

import com.financialhub.notificationservice.application.usecase.ListNotificationsUseCase;
import com.financialhub.notificationservice.interfaces.dto.response.NotificationResponse;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final ListNotificationsUseCase listNotifications;
    public NotificationController(ListNotificationsUseCase listNotifications) { this.listNotifications = listNotifications; }

    @GetMapping
    public List<NotificationResponse> list(Principal principal,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit,
            @RequestParam(defaultValue = "0") @Min(0) int offset) {
        return listNotifications.execute(UUID.fromString(principal.getName()), limit, offset).stream()
                .map(n -> new NotificationResponse(n.getId(), n.getType(), n.getTitle(), n.getMessage(), n.getCreatedAt(), n.getStatus().name(), n.getDeliveredAt()))
                .toList();
    }
}
