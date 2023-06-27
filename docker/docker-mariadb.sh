docker run -d \
--name rr-mariadb \
-e MYSQL_ROOT_PASSWORD="restaurantreservation" \
-e MYSQL_USER="restaurantreservation" \
-e MYSQL_PASSWORD="restaurantreservation" \
-e MYSQL_DATABASE="restaurantreservation" \
-p 3306:3306 \
mariadb:latest