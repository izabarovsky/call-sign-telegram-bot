# call sign telegram bot

Developed for K2 community

Main functions:
 - accept user data, like unofficial callsign, official callsign, QTH   
 - save data to storage
 - implement search 

# Stack

Simple project based on **java17**, **maven**, **spring-boot**  
Data storage is **postgres**  
Telegram API client is **telegrambots** 

### Example leave chat

```
BotApiMethod<?> leaveChat = LeaveChat.builder()
   .chatId(chatId)
   .build();
```

### RadioId integration
 Example request

`` ``

### Run postgres on localhost

Start db in docker  
`docker run -d --name=postgreDb -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:14.4`  
     
### Flyway

Clean schema  
`mvn flyway:clean -Dflyway.url=jdbc:postgresql://localhost:5432/postgres -Dflyway.user=postgres -Dflyway.password=postgres`  

Migrate  
`mvn flyway:migrate -Dflyway.url=jdbc:postgresql://localhost:5432/postgres -Dflyway.user=postgres -Dflyway.password=postgres`  

