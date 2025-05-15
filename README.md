# SimpleJavaBlog

A command-line interface (CLI) application for managing simple blog posts. This project was created to practice Java, Maven, and SQLite.

## Features

*   Create new blog posts with a title and content.
*   List all existing blog posts, showing their ID, title, and creation date.
*   View the full details of a specific post by its ID.
*   Data is persisted in an SQLite database (`blog.db`).

## Prerequisites

*   JDK 21 or newer installed.
*   Apache Maven installed.

## Getting Started

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/pygosclune/SimpleJavaBlog.git
    cd SimpleJavaBlog
    ```

2.  **Build the project:**
    ```bash
    mvn compile
    ```

3.  **Run the application:**
    ```bash
    mvn exec:java -Dexec.mainClass="org.example.blog.App"
    ```

## How to Use

Once the application is running, follow instructions displayed on CLI
