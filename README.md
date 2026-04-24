# Hotel-Booking-System
Kasymov Aikol SCA-24B

# 🏨 Grand Hotel Management System

**Grand Hotel** — это полнофункциональное веб-приложение для управления гостиничным бизнесом. Система разделена на клиентскую часть (Frontend) и серверную часть (REST API), обеспечивая бесшовный опыт взаимодействия для трех типов пользователей: Клиентов, Менеджеров и Администраторов.

### 🚀 Key Features

* **For Users:** Browse premium rooms in real-time, check availability, and submit booking requests. View personal booking history and track status updates (Pending/Approved).
* **For Managers:** A dedicated dashboard to oversee all hotel operations. Managers can view every booking in the system and approve them with a single click.
* **For Admins:** Full control over the hotel’s inventory. Admins can add new rooms (defining type, capacity, and price) and manage user permissions by dynamically changing roles.

### 🛠 Tech Stack

* **Backend:** Java 21, Spring Boot 3.4, Spring Security (JWT), Spring Data JPA.
* **Database:** PostgreSQL for reliable data storage and Redis for caching.
* **Frontend:** Modern HTML5, CSS3 (Flexbox/Grid), and Vanilla JavaScript for dynamic DOM manipulation and asynchronous API calls.

### 🔐 Security & Roles

The system uses **JWT (JSON Web Tokens)** for secure authentication. Access to specific endpoints is strictly controlled via Spring Security's `@PreAuthorize` annotations:
* `ROLE_USER`: Can book rooms and view own history.
* `ROLE_MANAGER`: Can view and approve all bookings.
* `ROLE_ADMIN`: Can manage room assets and user roles.

### 📋 Installation

1. Clone the repository.
2. Configure your `application.properties` for PostgreSQL.
3. Run the Spring Boot application.
4. Open `index.html` via Live Server or any web browser.

---
