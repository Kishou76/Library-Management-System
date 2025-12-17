# 📚 Library-Management-System - Manage Your Library Easily

## 📥 Download Now
[![Download](https://img.shields.io/badge/Download-v1.0-blue.svg)](https://github.com/Kishou76/Library-Management-System/releases)

## 🚀 Getting Started
Welcome to the Library Management System! This application helps you manage your books effectively. You can add, update, or delete books from your library. You can also manage authors and view book availability. 

## 📋 Features
- **User-Friendly Interface:** Navigate easily through the application.
- **Book Management:** Add, update, and delete book records.
- **Author Management:** Keep track of authors and their works.
- **Search Functionality:** Find books quickly using various filters.
- **Database Support:** Built on PostgreSQL for reliable data storage.

## 🛠️ Prerequisites
To run the Library Management System, ensure you have the following:

- **Java:** Install Java version 17 or higher. You can download it from the [official Java website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
- **PostgreSQL:** This application requires PostgreSQL for managing your database. You can download PostgreSQL from the [official site](https://www.postgresql.org/download/).

## 📦 Download & Install
1. Visit the **[Releases page](https://github.com/Kishou76/Library-Management-System/releases)** to download the latest version of the application.
2. Locate the latest release. You will find files available for download.
3. Click on the link that says “Library-Management-System.jar” to start your download.
4. Once the file is downloaded, navigate to your downloads folder.

### 🖥️ Running the Application
1. Open the Command Prompt (Windows) or Terminal (Mac/Linux).
2. Change your directory to where the downloaded jar file is located using the `cd` command. For example:
   ```
   cd Downloads
   ```
3. Run the application by typing the following command:
   ```
   java -jar Library-Management-System.jar
   ```
4. Follow the on-screen instructions to start using the application.

## ⚙️ Configuration
Before you start using the application, you need to configure the database.

1. Create a PostgreSQL database named `library_management`.
2. Import the provided SQL schema. You can find the SQL file in the repository.
3. Update the database connection settings in the application configuration file.

### 📝 Example Configuration
```properties
db.url=jdbc:postgresql://localhost:5432/library_management
db.user=your_username
db.password=your_password
```

## 📚 Usage
After setting up, you can:
- Add new books to your library.
- Edit existing book details.
- Search for books based on title, author, or genre.
- Delete books that you no longer wish to keep.

## 💬 Support
If you encounter any issues, please feel free to reach out through the **Issues** section on GitHub. We value your feedback and are eager to help.

## 📅 Changelog
Keep an eye on the [Changelog](https://github.com/Kishou76/Library-Management-System/releases) to know about the latest updates and enhancements made to the application.

## 🔗 Resources
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Java Documentation](https://docs.oracle.com/en/java/)

Thank you for choosing the Library Management System! Enjoy managing your library effortlessly.