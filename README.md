# ChartApp

**Veritabanı bağlantılı dinamik grafik oluşturma uygulaması**

Bu proje, kullanıcıların bir MSSQL veritabanına bağlanarak `View`, `Function` veya `Stored Procedure` üzerinden veri çekip bu verilerle interaktif grafikler oluşturmasını sağlayan bir web tabanlı sistemdir.

---

## 🚀 Özellikler

### 🔐 **Authentication & Authorization**
- JWT tabanlı kullanıcı doğrulama
- `ROLE_USER` ve `ROLE_ADMIN` ayrımı
- Refresh token desteği
- Güvenli oturum yönetimi
- Role göre endpoint erişimi

### 📡 **Veritabanı Bağlantısı**
- MSSQL desteği
- Kullanıcı, bağlantı bilgilerini girerek test edebilir
- Doğrudan table/view/SP/function listesini alır

### 📈 **Grafik Oluşturma**
- Kullanıcı, veritabanından veri çekip:
  - X ve Y eksenlerini seçerek haritalama yapar
  - Line, Bar, Radar (isteğe bağlı) tipi grafik oluşturur
- Dinamik stil ayarları:
  - Renk seçimi
  - Çizgi tipi ve kalınlığı
  - Grid gösterimi
  - Grafik Ekseni Ayarları
  - Grafik indirebilme özelliği

### ⚙️ **Teknolojiler**

#### ✅ **Backend (Spring Boot)**
- Java 17
- Spring Security + JWT
- Spring Data JPA
- MSSQL (Docker üzerinden)
- Role bazlı endpoint koruması
- Exception handling (`@ControllerAdvice`)
- Custom `SuccessResponse<T>` formatı
- Logging (`Slf4j`)

#### ✅ **Frontend (React.js)**
- React 18 + TailwindCSS
- React Router DOM
- React Toastify
- Chart.js
- Axios ile token'lı istekler
- Mobil uyumlu responsive tasarım

---

## 🛠️ Kurulum

### 1. Backend (Spring Boot)

#### Gerekli Araçlar:
- Java 17
- Maven
- Docker (DB için)

#### MSSQL Container Oluştur:
```bash
docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=StrongPassword123!" \
   -p 1433:1433 --name mssql -d mcr.microsoft.com/mssql/server:2022-latest

   - Buradan sonra application.properties de belirlediğimiz db ismi ile bağlantı sağladığımız db de database oluşturacağız chartdb



## 🐳 MSSQL Veritabanı Kurulumu (Docker)

Projede kullanılan MSSQL veritabanını Docker ile hızlıca ayağa kaldırabilirsiniz. Aşağıdaki `docker-compose.yml` servisi, test amaçlı ayrı bir MSSQL instance’ı oluşturur.

### 1. `docker-compose.yml` İçeriği

```yaml
version: '3.8'

services:
  mssql_test:
    image: mcr.microsoft.com/mssql/server:2022-latest
    container_name: sqlserver-test
    platform: linux/amd64
    ports:
      - "1435:1433"
    environment:
      ACCEPT_EULA: "Y"
      SA_PASSWORD: "StrongPassword44."
      MSSQL_PID: "Developer"
    volumes:
      - mssql_test_data:/var/opt/mssql
    healthcheck:
      test: [ "CMD-SHELL", "/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P StrongPassword44. -Q 'SELECT 1'" ]
      interval: 10s
      retries: 5
      start_period: 20s
      timeout: 5s

volumes:
  mssql_test_data:


  ----- Test için db kurulumu ve veri ekleme işlemi

  CREATE DATABASE SalesDB;
GO

USE SalesDB;
GO

CREATE TABLE Orders (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    OrderDate DATE NOT NULL,
    CustomerName NVARCHAR(100) NOT NULL,
    Product NVARCHAR(100) NOT NULL,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    TotalPrice AS (Quantity * UnitPrice) PERSISTED
);
GO

INSERT INTO Orders (OrderDate, CustomerName, Product, Quantity, UnitPrice)
VALUES
('2025-01-05', 'Alice', 'Laptop', 1, 1200.00),
('2025-01-07', 'Bob', 'Mouse', 5, 20.00),
('2025-01-10', 'Charlie', 'Keyboard', 3, 45.50),
('2025-01-15', 'Alice', 'Monitor', 2, 300.00),
('2025-01-20', 'David', 'Laptop', 1, 1100.00),
('2025-01-22', 'Eve', 'Mouse', 10, 18.00),
('2025-02-01', 'Bob', 'Desk Chair', 1, 150.00),
('2025-02-05', 'Charlie', 'USB Cable', 7, 8.00),
('2025-02-10', 'Alice', 'Webcam', 1, 75.00),
('2025-02-15', 'David', 'Laptop Bag', 1, 60.00),
('2025-02-20', 'Eve', 'Monitor', 1, 320.00),
('2025-02-22', 'Bob', 'Mouse Pad', 3, 12.00),
('2025-03-01', 'Charlie', 'Headset', 2, 85.00),
('2025-03-05', 'Alice', 'Laptop Stand', 1, 45.00),
('2025-03-10', 'David', 'Keyboard', 1, 50.00),
('2025-03-15', 'Eve', 'Monitor Stand', 1, 40.00),
('2025-03-20', 'Bob', 'HDMI Cable', 4, 15.00),
('2025-03-22', 'Charlie', 'Webcam', 1, 80.00),
('2025-04-01', 'Alice', 'External HDD', 2, 110.00),
('2025-04-05', 'David', 'Mouse', 6, 22.00);
GO



## 📡 API Endpointler ve İşlevleri

### 1. Authentication & Authorization

| HTTP Metodu | Endpoint                   | Açıklama                                                     | Rol Gereksinimi        | Token Gerekliliği  |
|-------------|----------------------------|--------------------------------------------------------------|-----------------------|--------------------|
| POST        | `/api/auth/login`           | Kullanıcı giriş yapar, JWT ve refresh token döner            | Herkes (giriş yapmamış) | Hayır             |
| POST        | `/api/auth/register`        | Yeni kullanıcı kaydı oluşturur                                | Herkes                | Hayır             |
| POST        | `/api/auth/logout`          | Kullanıcı çıkış yapar, refresh token silinir                 | Giriş yapmış kullanıcı | Evet              |
| POST        | `/api/auth/refresh-token`   | Refresh token ile yeni JWT token alır                        | Giriş yapmış kullanıcı | Evet (refresh token) |

---

### 2. Kullanıcı Bilgileri

| HTTP Metodu | Endpoint                    | Açıklama                                                     | Rol Gereksinimi        | Token Gerekliliği  |
|-------------|-----------------------------|--------------------------------------------------------------|-----------------------|--------------------|
| GET         | `/api/users/me`              | Mevcut giriş yapan kullanıcının bilgilerini döner            | `USER` veya `ADMIN`    | Evet              |
| GET         | `/api/users`                 | Tüm kullanıcıların listesini döner                            | Sadece `ADMIN`         | Evet              |
| GET         | `/api/users/search?name=`   | Kullanıcı adında arama yapar                                 | Sadece `ADMIN`         | Evet              |

---

### 3. Chart (Grafik) İşlemleri

| HTTP Metodu | Endpoint                    | Açıklama                                                     | Rol Gereksinimi        | Token Gerekliliği  |
|-------------|-----------------------------|--------------------------------------------------------------|-----------------------|--------------------|
| POST        | `/api/chart/listObjects`     | Bağlanılan veritabanındaki View, Stored Procedure, Function gibi veri objelerini listeler | `USER` veya `ADMIN`    | Evet              |
| POST        | `/api/chart/fetch`           | Seçilen veri objesine göre veriyi çeker ve grafik için hazırlar | `USER` veya `ADMIN`    | Evet              |

---

## 🔐 Güvenlik

- `/api/chart/**` ve `/api/users/**` endpointleri JWT token ile korunmaktadır.
- Token olmadan veya geçersiz token ile erişim engellenir.
- Role bazlı erişim kontrolü uygulanmıştır; `USER` ve `ADMIN` rolleri farklı yetkilere sahiptir.
- `/api/auth/**` endpointleri giriş ve kayıt için herkese açıktır.

---

Bu endpointler ile kullanıcılar:
- Sisteme güvenli şekilde giriş yapabilir,
- Kendi bilgilerini görebilir,
- (Admin iseler) diğer kullanıcıları listeleyebilir ve arama yapabilir,
- Veritabanındaki veri objelerini listeleyip,
- Yetkileri dahilinde veri çekip grafik oluşturabilir.

Token ve rol kontrolü sayesinde sistem güvenliği üst seviyededir.


