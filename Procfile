web: java $JAVA_OPTS -Dserver.port=$PORT -DAPP_ENV=prod -DDATASOURCE_URL=$SPRING_DATASOURCE_URL -DDATABASE_USERNAME=$SPRING_DATASOURCE_USER -DDATABASE_PASSWORD=$SPRING_DATASOURCE_PASSWORD -DSTART_EMBEDDED_BROKER=true -DACTIVEMQ_HOST_NAME=localhost -DACTIVEMQ_TCP_PORT=61618 -DACTIVEMQ_STOMP_PORT=61613 -jar target/*.jar