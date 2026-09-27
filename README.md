# Multi-Threaded Chat Application

A console-based multi-user chat application developed using Java Socket Programming and Multithreading.

## Project Description

This project allows multiple users to communicate with each other through a central chat server.

The server uses Java Sockets for networking and creates a separate thread for every connected client. Messages sent by one client are broadcast to all other connected clients.

## Features

- Multi-user chat
- Client-server architecture
- Java Socket Programming
- Multithreading
- Multiple clients can connect simultaneously
- Real-time message broadcasting
- Username support
- Join and leave notifications
- `/quit` command to exit the chat
- Console-based interface

## Technologies Used

- Java
- Java Socket Programming
- Multithreading
- TCP/IP Networking
- VS Code

## Project Structure

```text
MultiThreadedChatApplication
│
├── Server.java
├── ClientHandler.java
├── Client.java
├── README.md
└── .gitignore