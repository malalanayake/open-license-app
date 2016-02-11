
FILE_NAME=open-license-app.properties

APP_ENV=""
APP_ENV=`cat ${FILE_NAME} | grep "APP_ENV" | cut -d'=' -f2`
echo "APP ENV:${APP_ENV}"

server_port=""
server_port=`cat ${FILE_NAME} | grep "server_port" | cut -d'=' -f2`
echo "SERVER PORT:${server_port}"

datasource_url=""
datasource_url=`cat ${FILE_NAME} | grep "datasource_url" | cut -d'=' -f2`
echo "DB USER:${datasource_url}"

database_username=""
database_username=`cat ${FILE_NAME} | grep "database_username" | cut -d'=' -f2`
echo "DB USER:${database_username}"

database_password=""
database_password=`cat ${FILE_NAME} | grep "database_password" | cut -d'=' -f2`
echo "DB PASS:${database_password}"


java -Dserver.port=${server_port} -DAPP_ENV=${APP_ENV} -Ddatabase.username=${database_username} -Ddatabase.password=${database_password} -Ddatasource.url=${datasource_url} -jar open-license-app-1.0.0-SNAPSHOT.jar
