package usecase.trading;

import data.trading.InMemoryTradingDataAccess;
import entity.OrderRecord;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTradingDataAccessTest {

    @Test
    void savedOrdersAreRetainedAndScopedToTheirUser() {
        InMemoryTradingDataAccess dataAccess = new InMemoryTradingDataAccess();
        OrderRecord aliceOrder = new OrderRecord(
                Instant.parse("2026-01-01T00:00:00Z"),
                "alice", "AAPL", "BUY", 2, 100.0, 200.0);
        OrderRecord bobOrder = new OrderRecord(
                Instant.parse("2026-01-01T00:01:00Z"),
                "bob", "MSFT", "BUY", 1, 200.0, 200.0);

        dataAccess.saveOrder(aliceOrder);
        dataAccess.saveOrder(bobOrder);

        List<OrderRecord> aliceOrders = dataAccess.findOrdersByUser("alice");

        assertEquals(List.of(aliceOrder), aliceOrders);
        assertEquals(List.of(bobOrder), dataAccess.findOrdersByUser("bob"));
    }
}