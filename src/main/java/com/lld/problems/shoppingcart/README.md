# Shopping Cart — EventBus Cross-Links

This problem uses the shared `com.lld.common.events.EventBus` to publish `OrderPlacedEvent` on checkout. Downstream subscribers (inventory, notifications, analytics) stay decoupled from cart logic.

## EventBus in This Problem

| Component | Role |
|-----------|------|
| `EventBus` | Interface for subscribe / publish |
| `InMemoryEventBus` | In-process implementation (sync or async) |
| `OrderPlacedEvent` | Domain event published on successful checkout |
| `Subscriber<OrderPlacedEvent>` | Callback invoked when order is placed |

## Related Pub-Sub Problems (21–25)

Study the shared EventBus infrastructure and advanced patterns in these problems:

| # | Problem | Doc | Focus |
|---|---------|-----|-------|
| 21 | Pub-Sub Messaging | [docs/21-pub-sub.md](../../../docs/21-pub-sub.md) | Core EventBus, topics, subscribers |
| 22 | Notification Service | [docs/22-notification-service.md](../../../docs/22-notification-service.md) | Multi-channel notifications via events |
| 23 | Stock Exchange | [docs/23-stock-exchange.md](../../../docs/23-stock-exchange.md) | Order book updates as events |
| 24 | Chat Application | [docs/24-chat-application.md](../../../docs/24-chat-application.md) | Room channels and broadcast |
| 25 | Event-Driven Order Pipeline | [docs/25-order-pipeline.md](../../../docs/25-order-pipeline.md) | Saga-style multi-step pipelines |

## Suggested Learning Path

1. **Problem 11** (this) — publish `OrderPlacedEvent` from a product workflow
2. **Problem 21** — understand EventBus internals and topic routing
3. **Problems 22–25** — apply pub-sub to notifications, trading, chat, and sagas

## Extension

Wire an inventory subscriber in tests that decrements a shadow stock ledger when `OrderPlacedEvent` fires — then compare with Problem 25's saga approach for failure handling.
