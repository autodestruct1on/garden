package gg.cristalix.growagarden.model.event;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class EventData {
    private String id;                             // Уникальный ID события
    private String name;                           // Название события
    private Instant startTime;                     // Время начала события
    private Instant endTime;                       // Время окончания события
    private Map<String, Double> globalModifiers;   // Глобальные модификаторы события
}
