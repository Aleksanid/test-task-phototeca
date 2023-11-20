Quick readme:

Setup:
- Java 17
- PostgreSQL 16
- Spring 3.1.5

Commands:
- /start - to start to be notified
- /reset - to reset your start time(restart cycle)

All variables controlled via application.yml

Init DB script is in [infrastructure](/infrastructure/init.sql) folder


Not finished/started:
- Logging
- Exception handling

Ideas:
- Had idea to do all calculations in App, that way you don't need to store any prices except start prices.
- Probably can do fancy query with joining users into the picture, instead of iterating over them in app, but can't wrap my head at this point
- NoSQL would have probably better perfomance if used with index by time.
