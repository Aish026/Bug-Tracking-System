# Bug-Tracking-System
A simple Bug Tracking System built with Spring Boot, Spring Security, and Thymeleaf. It lets users report bugs, developers resolve them, and admins manage everything — all with role-based dashboards and stats. 

---

## ✨ What it does

- 🔑 **Secure login with roles**
  - Admins, Developers, and Users each get their own dashboard.
- 🐛 **Bug management made easy**
  - Report bugs, assign them, update their status, and track progress.
- 📊 **Dashboards with stats**
  - See total, open, and resolved bugs at a glance.
- 👥 **User settings**
  - Update your email and password (username stays fixed).

---

## 👥 Roles

- **Admin**  
  Full control. Can manage users, assign bugs, and delete if needed.
- **Developer**  
  Works on bugs assigned to them. Can also report new bugs.
- **User**  
  Can report bugs and track the ones they created.

---

## 🖥️ Dashboards

- **Admin Dashboard** → bug stats, user management, developer management.  
- **Developer Dashboard** → assigned bugs, reported bugs, progress stats.  
- **User Dashboard** → list of bugs they reported + bug stats.  

---

## 🛠️ Tech Stack

- **Spring Boot** (Web, Security, JPA, Validation)  
- **Thymeleaf** + **Bootstrap 5** for the UI  
- **H2 / MySQL / Postgres** supported via JPA  
- **Maven** build system  

---

# 🛠️ Setup & Project Details

## 🚀 Getting Started

1. Clone the repo  
   git clone https://github.com/yourusername/bug-tracker.git  
   cd bug-tracker  

2. Configure the database  
   By default this project uses an in-memory H2 database for quick local runs.  
   To use MySQL or Postgres, update `src/main/resources/application.properties` accordingly.

   Example (H2):  
   spring.datasource.url=jdbc:h2:mem:bugtracker  
   spring.datasource.driver-class-name=org.h2.Driver  
   spring.datasource.username=sa  
   spring.datasource.password=  
   spring.jpa.hibernate.ddl-auto=update  
   spring.h2.console.enabled=true  

   Example (MySQL):  
   spring.datasource.url=jdbc:mysql://localhost:3306/bugtracker  
   spring.datasource.username=root  
   spring.datasource.password=yourpassword  
   spring.jpa.hibernate.ddl-auto=update  

3. Run the app  
   mvn spring-boot:run  
   The app will be available at: http://localhost:8080  

4. Default admin (auto-created)  
   username: admin  
   password: admin123  

---

## 📂 Project Structure

src/main/java/com/bugtracker/  
├── controller/   # Web controllers (handle requests and views)  
├── dto/          # Data Transfer Objects (e.g., UserSettingsDTO, BugStatsDTO)  
├── model/        # Entities (User, Bug, Developer)  
├── repository/   # JPA Repositories (UserRepository, BugRepository, etc.)  
├── service/      # Business logic & UserDetailsService implementations  
├── SecurityConfig.java  
└── BugTrackerApplication.java  

Other useful locations:  
- src/main/resources/templates/ — Thymeleaf templates (views)  
- src/main/resources/static/ — CSS, JS, images  
- src/main/resources/application.properties — application configuration  

---

## ✅ Notes & Conventions

- Usernames are immutable after registration (displayed read-only in settings).  
- Role-based access:  
  - /admin/** → Admin only  
  - /developer/** → Developer only  
  - /bugs/**, /dashboard, /my-tickets → authenticated roles  
- DTOs are used for forms (e.g., UserSettingsDTO) to avoid binding entities directly in views.  
- Use `@AuthenticationPrincipal` or `Authentication` in controllers to get the current user and populate DTOs.  

---

## 📜 License

This project is open-sourced under the MIT License.
