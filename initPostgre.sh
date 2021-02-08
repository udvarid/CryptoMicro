docker kill crypto-postgres
docker rm crypto-postgres
docker run --name crypto-postgres -p 5432:5432 -e POSTGRES_PASSWORD=crypto77 -d postgress