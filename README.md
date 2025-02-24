# LinkedIn Clone

## Overview

This project is a LinkedIn clone designed to replicate the core functionalities of the professional networking
platform.  
It allows users to manage their profiles, create posts, comment on posts, connect with other users, follow
organizations, and explore job opportunities.  
The platform also includes advanced features such as reacting to posts and comments, creating organizations, and
applying for jobs.  
Below is a detailed breakdown of all the features implemented in this project.

## Features

- **User Profile Management**: Users can create and update their profiles, showcasing their skills, experience, and
  education.
- **Posts and Comments**: Users can create posts, comment on others' posts, and engage with the community. Additionally,
  posts and comments can be added to organization pages.
- **Reaction**: Users can react to posts and comments. Reactions can also be added as an organization page, allowing
  companies to engage with the community.
- **Connections**: Users can build their professional network by connecting with other users.
- **Organization Page Management**:
    - Users can create and manage organization pages, allowing companies to establish their presence on the platform.
    - The organization creator can add other admins to manage the page.
    - Users can follow organization pages to stay updated with the latest posts and job listings.
- **Job Listings**: Companies can post job opportunities for users to explore.
- **Job Applications**:
    - Users can apply to job listings using their profiles.
    - Users can view job listings using personalized filters, such as location, job type, industry, experience level,
      skills, working time (e.g., part-time, full-time), job title, and organization, to find relevant opportunities.
    - Employees can view applications, after which users receive a notification that their application has been viewed.
    - Employees can accept or reject applications, and users receive a notification via email regarding the decision.

## Technologies Used

- **Backend**: Java, Spring Boot
- **Authentication**: JWT (JSON Web Tokens) for user authentication
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA
- **Security**: Spring Security
- **API Documentation**: Swagger
- **Email Service**: Spring Mail for sending email notifications
- **Scheduler**: Spring Scheduler for handling scheduled tasks
- **Build Tool**: Gradle
- **Version Control**: Git, GitHub



