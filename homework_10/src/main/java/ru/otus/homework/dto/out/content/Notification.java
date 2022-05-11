package ru.otus.homework.dto.out.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.homework.dto.out.enums.NotificationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private NotificationType type;
    private String message;
}
