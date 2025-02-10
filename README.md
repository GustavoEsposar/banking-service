# banking-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Execução

A seguir os passo necessários para inicializar todo o ambiente dos microsserviços:

1. No diretório **banking-validation**
    - docker-compose up  

2. No diretório **banking-service**
    - docker-compose up (inicia o quarkus:dev e o PostgreSQL);
    - docker run -d --name prometheus \
  -p 9090:9090 \
  -v $(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml \
  prom/prometheus (inicia container prometheus);

    - docker run -d --name grafana -p 3000:3000 grafana/grafana (inicia container grafana);
