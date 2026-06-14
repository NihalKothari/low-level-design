# LLD Interview Prep (Java)

25 curated Low Level Design problems for technical interviews. Each problem includes a design doc, Java skeleton, and JUnit 5 tests.

## Prerequisites

- Java 17+
- Maven 3.8+

## Run

```bash
cd lld-interview-prep
mvn test                              # all problems
mvn test -Dtest=ParkingLotTest        # single problem
mvn test -Dtest="*Cache*Test"        # pattern match
```

## Problem Index

| # | Problem | Tier | Patterns | Difficulty | Doc |
|---|---------|------|----------|------------|-----|
| 01 | Parking Lot | Classic | Strategy, Factory, State | Medium | [doc](docs/01-parking-lot.md) |
| 02 | Elevator System | Classic | State, Strategy | Hard | [doc](docs/02-elevator-system.md) |
| 03 | Library Management | Classic | Repository, Observer | Medium | [doc](docs/03-library-management.md) |
| 04 | Hotel Booking | Classic | Factory, Strategy | Medium | [doc](docs/04-hotel-booking.md) |
| 05 | Movie Ticket Booking | Classic | Lock, State | Hard | [doc](docs/05-movie-ticket-booking.md) |
| 06 | Vending Machine | Classic | State, Strategy | Medium | [doc](docs/06-vending-machine.md) |
| 07 | ATM Machine | Classic | Chain of Responsibility, State | Medium | [doc](docs/07-atm-machine.md) |
| 08 | Splitwise | Classic | Graph, Greedy | Medium | [doc](docs/08-splitwise.md) |
| 09 | Ride Hailing | Classic | Strategy, State | Hard | [doc](docs/09-ride-hailing.md) |
| 10 | Food Delivery | Product | State, Strategy | Hard | [doc](docs/10-food-delivery.md) |
| 11 | Shopping Cart & Checkout | Product | EventBus, Strategy | Hard | [doc](docs/11-shopping-cart.md) |
| 12 | Car Rental | Product | State, Strategy | Medium | [doc](docs/12-car-rental.md) |
| 13 | Meeting Scheduler | Product | Interval, Strategy | Hard | [doc](docs/13-meeting-scheduler.md) |
| 14 | Chess Game | Product | Command, Strategy | Hard | [doc](docs/14-chess-game.md) |
| 15 | Thread-safe LRU Cache | Concurrency | RWLock, LinkedHashMap | Hard | [doc](docs/15-lru-cache.md) |
| 16 | Rate Limiter | Concurrency | Token Bucket, Locks | Hard | [doc](docs/16-rate-limiter.md) |
| 17 | Connection Pool | Concurrency | Semaphore, wait/notify | Hard | [doc](docs/17-connection-pool.md) |
| 18 | Job Scheduler | Concurrency | DelayQueue, Executor | Hard | [doc](docs/18-job-scheduler.md) |
| 19 | Producer-Consumer | Concurrency | BlockingQueue | Medium | [doc](docs/19-producer-consumer.md) |
| 20 | Thread Pool Executor | Concurrency | Work Queue, Rejection | Hard | [doc](docs/20-thread-pool.md) |
| 21 | Pub-Sub Messaging | Pub-Sub | Observer, EventBus | Hard | [doc](docs/21-pub-sub.md) |
| 22 | Notification Service | Pub-Sub | EventBus, Strategy | Medium | [doc](docs/22-notification-service.md) |
| 23 | Stock Exchange | Pub-Sub | Order Book, Observer | Hard | [doc](docs/23-stock-exchange.md) |
| 24 | Chat Application | Pub-Sub | Room channels, Broadcast | Hard | [doc](docs/24-chat-application.md) |
| 25 | Event-Driven Order Pipeline | Pub-Sub | Saga, EventBus | Hard | [doc](docs/25-order-pipeline.md) |

## Package Layout

```
com.lld.common              Shared Result, EventBus, repositories
com.lld.problems.<slug>     One package per problem
  ├── model/                Entities and enums
  ├── service/              Business logic
  ├── strategy/             Pluggable algorithms (when needed)
  ├── repository/           In-memory stores
  └── demo/                 Happy-path runner
```

## Study Path (2 weeks)

**Week 1 — Classics + Concurrency**
- Days 1-2: Problems 01, 06, 07 (state patterns)
- Days 3-4: Problems 02, 05, 09 (scheduling, locking)
- Days 5-7: Problems 15-20 (concurrency deep dive)

**Week 2 — Pub-Sub + Product**
- Days 1-2: Problems 21-22 (EventBus foundation)
- Days 3-4: Problems 23-25 (event-driven systems)
- Days 5-7: Problems 10-14 (product workflows)

## Shared Components

| Component | Package | Used By |
|-----------|---------|---------|
| `Result<T>` | `com.lld.common` | All services |
| `EventBus` | `com.lld.common.events` | 11, 21-25, 22 |
| `InMemoryRepository` | `com.lld.common.repository` | 03, 04, 12 |
| `LockableResource` | `com.lld.common.concurrency` | 15-17, 20 |
| `ConcurrentTestHelper` | test `com.lld.common` | 15-20 |

## Status

All problems are scaffolded with README, class skeletons, and starter tests. Implement `// TODO` sections as you study each problem.
