# MunchTruck

## Table of Contents
- [UX](#ux)
  - [App Purpose](#app-purpose)
  - [App Goal](#app-goal)
  - [Developer Goals](#developer-goals)
  - [User Goals](#user-goals)
  - [Audience](#audience)
  - [Communication](#communication)
  - [Interaction & Experience Principles](#interaction--experience-principles)
- [Agile Planning](#agile-planning)
  - [User Stories](#user-stories)
  - [MoSCoW Prioritization](#moscow-prioritization)
  - [Sprint Planning](#sprint-planning)
  - [Implemented User Stories](#implemented-user-stories)
  - [Not Implemented User Stories](#not-implemented-user-stories)
  - [Kanban Board](#kanban-board)
  - [UML Diagram](#uml-diagram)
- [Design](#design)
  - [Wireframe](#wireframe)
  - [Colour Scheme](#colour-scheme)
  - [Fonts](#fonts)
- [Features](#features)
  - [Existing Features](#existing-features)
  - [Future Features](#future-features)
- [Testing](#testing)
  - [Manual Testing](#manual-testing)
  - [Bugs](#bugs)
  - [Unfixed Bugs](#unfixed-bugs)
- [Technologies](#technologies)
  - [Main Languages Used](#main-languages-used)
  - [Architecture](#architecture)
  - [Firebase Integration](#firebase-integration)
  - [Setup & Installation](#setup--installation)
- [Credits](#credits)
  - [Content](#content)
  - [Media](#media)

## UX
### App Purpose
Our vision is to make it easy for food truck owners to reach the right customers – exactly when and where their food is available. Through a digital platform, we aim to increase the visibility of food trucks and contribute to a vibrant and accessible street food culture.
The purpose of this application is to create a central platform where food truck owners can promote their business, display their menu in real time, and become visible to food lovers in their local area.
The application also aims to simplify communication between food truck owners and customers and reduce the need for external social media platforms to reach potential customers.

### App Goal
The main goal of this project is to develop a user-friendly application for food truck owners where they can:
- Log in to their account
- Create and manage their profile
- Upload and update their menu
- Make themselves visible to nearby customers

Additional goals include:
- Creating a clear and simple structure that works on both mobile and desktop
- Ensuring that the displayed information is accurate and up to date

### Developer Goals
Our goal as developers is to create a stable, scalable, and well-structured application using modern Android development practices.

We aim to:
- Apply the MVVM architecture pattern
- Use Jetpack Compose for UI development
- Integrate Firebase for authentication and data storage
- Maintain clean, readable, and well-documented code
- Collaborate efficiently using Git and agile methods

### User Goals
Food truck owners want to:
- Easily manage their profile and menu
- Reach more customers in their local area
- Update their information quickly and reliably

Food lovers want to:
- Discover nearby food trucks
- Search and filter by food type and preferences
- Compare menus and make informed decisions

### Audience
**Primary Target Audience:**  
Food truck owners who want to promote their business and reach more customers digitally.

**Secondary Target Audience:**  
Food lovers who want to discover available street food in their local area.

### Communication
The application communicates with users through clear buttons, consistent icons, and simple, easy-to-understand text.

For food lovers, the interface is designed to be visually appealing and engaging, encouraging users to explore nearby food trucks and menus. Images, colors, and layout are used to create an inviting experience.

For food truck owners, the profile and management features are designed to be clear and structured, making it easy to update information, menus, and locations.

Important actions such as logging in, saving changes, and updating data are supported by visual feedback and confirmation messages. Error messages are informative and help users understand what went wrong and how to fix it.

### Interaction & Experience Principles
The application is designed to be intuitive, simple, and easy to use for both food truck owners and food lovers.

Key principles include:
- Clear navigation and structure
- Consistent layout and color scheme
- Minimal learning curve for new users
- Responsive design for different screen sizes
- Fast access to important features

Our goal is to create a smooth and enjoyable user experience that encourages continued use of the application.

## Agile Planning
The development of MunchTruck was planned using Agile methodology. The project was structured around user stories and organized using short, focused sprints.

The MoSCoW method was used to prioritize features and ensure that the most important functionality was implemented first. Sprint planning and task distribution were carried out collaboratively within the group.

### User Stories
The project was structured using Agile principles, where all functionality was defined through user stories. Each story represents a concrete piece of functionality from the user’s perspective and guided both development and testing throughout the project.

By working with clearly defined stories, acceptance criteria, and detailed tasks, the group maintained a clear scope, steady progress, and a structured workflow from initial design to final implementation.

Below, the user stories are grouped according to their MoSCoW priority:

***Must Have***
- US 2: Edit profile (Food truck owner)
- US 3: Add menu (Food truck owner)
- US 7: See nearby food trucks (Food lover)
- US 5: Set location (Food truck owner)
- US 10: View menu (Food lover)
- US 12: View map (Food lover)

***Should Have***
- US 4: Set availability (online/offline) (Food truck owner)
- US 9: Filter by preferences (Food lover)
- US 8: Search by food type (Food lover)
- US 11: See opening hours & status (Food lover)
- US 13: Edit menu in real time (Food truck owner)
- US 15: Set opening hours (Food truck owner)
- US 16: Delete account (Food truck owner)


***Could Have***
- US 14: Upload food truck image (Food truck owner)
- US 17: Save favorite food trucks (Food lover)
- US 18: View reviews (Food lover)
- US 19: Leave a review (Food lover)

***Won’t Have***
- US 6: View statistics (Food truck owner)
- US 20: Filter by distance (Food lover)

The full backlog and detailed task breakdown are managed through our [Trello Kanban](https://trello.com/b/zNiBfOvi/androidprojekt-uppgift-kom-pa-namn) board.

### MoSCoW Prioritization
The following table explains how the MoSCoW prioritization method was applied in this project.

| Priority        | Description                                                                                                                               |
|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| **Must Have**   | Core functionality required for the application to work, such as authentication, profile management, location handling, and menu display. |
| **Should Have** | Important features that improve usability and user experience if time allows, such as filtering, availability status, and opening hours.  |
| **Could Have**  | Optional enhancements that add extra value, such as favorites and reviews.                                                                |
| **Won’t Have**  | Features that were intentionally excluded from the current version to keep focus on the core functionality of the MVP.                    |

### Sprint Planning
### Implemented User Stories
### Not Implemented User Stories

### Kanban Board
The project is organized using a [Trello Kanban](https://trello.com/b/zNiBfOvi/androidprojekt-uppgift-kom-pa-namn) board to manage and track progress throughout development.

Our workflow is structured as follows:

***Backlog → Sprint Backlog → To Do → In Progress → Done***

User stories are first placed in the backlog and then selected for each sprint. During sprint planning, selected stories are moved to the sprint backlog and broken down into smaller tasks. Tasks are then progressed through the board until they are completed.

This structure helps the team maintain transparency, organize work efficiently, and monitor progress during each sprint.

### UML Diagram
The UML diagram was created using [Lucidchart](https://www.lucidchart.com/) and illustrates the overall architecture of the MunchTruck application. It presents the main components of the system and how they are connected within the MVVM structure.

During the development process, some adjustments were made as the project evolved. Therefore, the final implementation may differ slightly from the original diagram. Smaller utility classes and minor details are not included, and the diagram should be viewed as a conceptual overview rather than a complete representation of the entire codebase.

![UML Diagram](docs/uml_diagram.png)


## Design
### Wireframe
### Colour Scheme
### Fonts

## Features
### Existing Features
### Future Features

## Testing
### Manual Testing
### Bugs
### Unfixed Bugs

## Technologies
### Main Languages Used
### Architecture
### Firebase Integration
### Setup & Installation

## Credits
### Content
### Media



