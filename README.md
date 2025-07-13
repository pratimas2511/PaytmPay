# 💸 PaytmPay – Wallet & Payment Microservices Backend

A production-grade distributed backend system for a mini Paytm-style application, built using Spring Boot, MySQL, and Docker. Supports user authentication, wallet operations, and transactions — organized as scalable microservices.

---

## 🔧 Tech Stack

- **Spring Boot ** (Auth, Wallet, Transaction Services)
- **MySQL 8.3** (Dockerized)
- **Docker Compose**
- **GitHub Actions CI** (build, test with MySQL)
- **JWT Authentication**
- **REST APIs**

---

## 📁 Microservices

| Service           | Port | Description                        |
|------------------|------|------------------------------------|
| **Auth Service** | 8080 | Handles user registration, login, and token generation |
| **Wallet Service** | 8083 | Manages user wallet and balance updates |
| **Transaction Service** | 8084 | Handles money transfer between wallets |

Each service connects to its **own MySQL database**.

---

## 🚀 Run Locally with Docker

```bash
# clone repository
git clone https://github.com/your-username/PaytmPay.git
cd PaytmPay

# build all jars
mvn clean package -DskipTests

# run full stack
docker compose up --build
