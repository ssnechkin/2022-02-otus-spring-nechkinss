package ru.rncb.dpec.dto.out.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rncb.dpec.dto.out.enums.NotificationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private NotificationType type;
    private String message;

    public Notification setType(NotificationType type) {
        this.type = type;
        return this;
    }

    public Notification setMessage(String message) {
        this.message = message;
        return this;
    }
}
