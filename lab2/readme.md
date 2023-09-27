After add certificate to ban http and not safe access, we can get worker api easily by localhost://port/workers. So Here just show what it would be before add security permit to https.

![alt](https://github.com/LiaoYihong-1/SOA/tree/dev/lab2/note.png)

But it's ok to get info with https in postman(don't know why i can do it).

![alt](https://github.com/LiaoYihong-1/SOA/tree/dev/lab2/note1.png)



24.09

Problem with ssl sign.

I used openssl to create certification. Configuration like:

server.port=9000
server.ssl.key-store=classpath:certificate1.p12
server.ssl.key-store-password=123456
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=alias

Now we can access operation only by "https://localhost:9000/company/workers/2"."http://localhost:9000/company/workers/2" will returns bad request because it's not safe.

Now problem is that. I have test that without ssl i can normally call any api on spring in JAX-RS(2-nd server). But with ssl i can't call it. I created keystore to access spring api but always get this error:

"javax.net.ssl.SSLException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty"

Basically backend left the problem: Доступ к обоим сервисам должен быть реализован с по протоколу https с самоподписанным сертификатом сервера. Доступ к сервисам посредством http без шифрования должен быть запрещён.

On JAX-RS side didn't finish it.

#### script to create .p12 sign

  openssl genrsa -out private.key 2048

  openssl req -new -key private.key -out certificate.csr

  openssl x509 -req -in certificate.csr -signkey private.key -out certificate.crt

openssl pkcs12 -export -in certificate.crt -inkey private.key -out certificate.p12 -name your_alias

### script to create database:

CREATE TABLE Organization (
                              id SERIAL PRIMARY KEY,
                              full_name VARCHAR(758) NOT NULL CHECK (*LENGTH*(full_name) <= 758) UNIQUE,
                              annual_turnover BIGINT NOT NULL CHECK (annual_turnover > 0)
);
CREATE TYPE Position AS ENUM (
    'MANAGER',
    'HUMAN_RESOURCES',
    'HEAD_OF_DEPARTMENT',
    'DEVELOPER',
    'COOK'
);
CREATE TABLE Worker (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL CHECK (name <> ''),
                        coordinates_x BIGINT NOT NULL,
                        coordinates_y DOUBLE PRECISION NOT NULL CHECK (coordinates_y > -561),
                        creation_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT *NOW*(),
                        salary FLOAT NOT NULL CHECK (salary > 0),
                        start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                        end_date DATE,
                        position "Position" NOT NULL,
                        organization_id INT NOT NULL,
                        FOREIGN KEY (organization_id) REFERENCES Organization(id)
);
