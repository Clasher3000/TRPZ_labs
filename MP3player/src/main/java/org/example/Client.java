package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Підключення до сервера
        Socket socket = new Socket("localhost", 4999);

        // Створюємо потоки для надсилання та отримання даних
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  // Для надсилання
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Для отримання

        // Запускаємо окремий потік для отримання повідомлень від сервера
        Thread receiveThread = new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {  // Читання відповіді від сервера
                    System.out.println("Server: " + serverMessage);
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        });
        receiveThread.start();

        // Основний потік для введення і відправлення команд
        Scanner scanner = new Scanner(System.in);

        // Цикл для відправки команд на сервер
        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine();  // Читання команди з консолі

            // Перевірка на команду виходу
            if (command.equalsIgnoreCase("exit")) {
                out.println(command);  // Надсилаємо команду виходу на сервер
                break;
            }

            // Відправка команди на сервер
            out.println(command);
            Thread.sleep(500);
        }

        // Закриття ресурсів
        out.close();
        in.close();
        socket.close();
        System.out.println("Client disconnected.");
    }
}
