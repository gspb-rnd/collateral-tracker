# Collateral Tracker

A web application for tracking collateral information with a React frontend and Java Spring Boot backend.

## Features

- Navigation bar with "New Collateral" button
- Search functionality for collateral (UI only for now)
- MongoDB integration for storing collateral information

## Technology Stack

- Frontend: React with TypeScript, Tailwind CSS, and shadcn/ui components
- Backend: Java 17, Spring Boot 2.7.18, Gradle 8.7
- Database: MongoDB

## Dependency Versions

### Backend
- Java: Compatible with Java 17-21
- Spring Boot: 2.7.18
- Spring Data MongoDB: 2.7.18
- Spring Boot Starter Web: 2.7.18
- Spring Boot Starter Validation: 2.7.18
- Gradle: 8.7

### Frontend
- React: 18.3.1
- TypeScript: 5.6.2
- Vite: 6.0.1
- Tailwind CSS: 3.4.16
- React Router DOM: 7.4.0
- React Hook Form: 7.54.2
- Zod: 3.24.2

## Project Structure

- `/frontend` - React frontend application
- `/backend` - Spring Boot backend application

## Setup Instructions

### Backend

1. Navigate to the backend directory:
   ```
   cd backend
   ```

2. Build the application:
   ```
   ./gradlew build
   ```

3. Run the application:
   ```
   ./gradlew bootRun
   ```

The backend will be available at http://localhost:8080

### Frontend

1. Navigate to the frontend directory:
   ```
   cd frontend/frontend
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Run the development server:
   ```
   npm run dev
   ```

The frontend will be available at http://localhost:5173
