package org.example.warehouse;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRecord(UUID uuid, String productName, Category category, BigDecimal price) {
}
