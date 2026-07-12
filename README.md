# ReservationSystem

## Sprint 1 Demo

Video link: 

https://cdnapisec.kaltura.com/p/2229001/embedPlaykitJs/uiconf_id/45744511?iframeembed=true&entry_id=1_uhw09inm&config%5Bprovider%5D=%7B%22widgetId%22%3A%221_gpuatvr7%22%7D&config%5Bplayback%5D=%7B%22startTime%22%3A0%7D

## Project Overview

ReservationSystem is a JavaFX Maven desktop application for reserving campus spaces. The system allows users to view reservable spaces, check daily availability for a selected space and date, and create reservations.

This project was developed using object-oriented design, MVC architecture, JSON persistence, JUnit testing, GitHub issues, feature branches, pull requests, and peer reviews.

## Sprint 1 Focus

Sprint 1 focused on these user stories:

- US-1: View all reservable spaces
- US-5: View day availability for a selected space and date
- US-8: Create a reservation

## Completed Sprint 1 Features

- MVC package structure
- Space model
- Reservation model
- TimeSlot model
- JSON loading for spaces
- JSON loading and saving for reservations
- Sample spaces JSON file
- Sample reservations JSON file
- US-1 view all reservable spaces flow
- US-5 day availability flow for a selected space and date
- US-8 create reservation flow
- Reservation validation logic
- Availability service logic
- SpaceController
- AvailabilityController
- ReservationController
- US-1 integration test
- US-5 availability unit tests
- US-5 day availability integration test
- US-8 reservation validation unit tests
- US-8 create reservation integration test
- US-5 manual system test documentation

## Project Structure

Main source code:

- `src/main/java/reservationsystem/controller`
- `src/main/java/reservationsystem/model`
- `src/main/java/reservationsystem/persistence`
- `src/main/java/reservationsystem/service`
- `src/main/java/reservationsystem/view`

Data files:

- `src/main/resources/data/spaces.json`
- `src/main/resources/data/reservations.json`

Test code:

- `src/test/java/reservationsystem/integration`
- `src/test/java/reservationsystem/service`

Documentation:

- `docs/manual-tests`

## How to Run the Application

On Windows, run:

```bash
.\mvnw.cmd javafx:run
