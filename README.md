# ReservationSystem

## Sprint 1 Demo

Video link: 

## Project Overview

ReservationSystem is a JavaFX Maven desktop application for reserving campus spaces. The system allows users to view reservable spaces, check daily availability, and create reservations.

This project is being developed using object-oriented design, MVC architecture, JSON persistence, JUnit testing, GitHub issues, feature branches, and pull requests.

## Sprint 1 Focus

Sprint 1 focuses on these user stories:

- US-1: View all reservable spaces
- US-5: View day availability for a selected space and date
- US-8: Create a reservation

## Completed or In-Progress Features

- MVC package structure
- Space model
- Reservation model
- TimeSlot model
- JSON loading for spaces
- Sample spaces JSON file
- Sample reservations JSON file
- US-5 availability service logic
- US-1 integration test
- US-5 availability unit tests

## Project Structure

```text
src/main/java/reservationsystem
├── controller
├── model
├── persistence
├── service
└── view

src/main/resources/data
├── spaces.json
└── reservations.json

src/test/java/reservationsystem
├── integration
└── service
