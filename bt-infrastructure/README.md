# Локальное окружение для приложения для бронирования столиков

**Postgres: image: postgres:15**
1. Host port - 5432, Container port - 5432
2. Данные БД сохраняются между перезапусками контейнера
```yaml
    volumes:
      - db-data:/var/lib/postgresql/data
```
3. Каждые 10 секунд выполняется проверка работы сервера

**Kafka: image: apache/kafka:4.2.0**
Настройки:
1. Используется KRaft
2. Host port - 9092, Container port - 29092
```yaml
      # Уникальный идентификатор брокера в кластере
      KAFKA_NODE_ID: "1"
      # Kafka работает в режиме KRaft: как брокер и как контроллер
      KAFKA_PROCESS_ROLES: "broker,controller"
      # Настройки listener
      KAFKA_LISTENERS: "PLAINTEXT://0.0.0.0:29092,PLAINTEXT_HOST://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093"
      KAFKA_ADVERTISED_LISTENERS: "PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092"
      # Протокол безопастности: без шифрования
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: "PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT,CONTROLLER:PLAINTEXT"
      KAFKA_CONTROLLER_LISTENER_NAMES: "CONTROLLER"
      KAFKA_CONTROLLER_QUORUM_VOTERS: "1@kafka:9092"
      KAFKA_INTER_BROKER_LISTENER_NAME: "PLAINTEXT"
      # Количество реплик
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: "1"
      # Количество реплик транзакционного лога
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: "1"
      # Минимальное число реплик, которое должно быть онлайн
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: "1"
      # Задержка перед началом балансировки consumer group
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: "0"
      CLUSTER_ID: "MkU3OEVBNTcwNTJENDM2Qk"
```


**Kafka UI: image: provectuslabs/kafka-ui:v0.7.2**
Настройки:
1. Не запускается раньше Kafka
2. Используется динамическая конфигурация
3. 
