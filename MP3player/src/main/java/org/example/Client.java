package org.example;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        // Підключення до сервера
        Socket s = new Socket("localhost", 4999);

        // Створюємо потоки для надсилання та отримання даних
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);  // Для надсилання
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));  // Для отримання

        Scanner scanner = new Scanner(System.in);

        // Цикл для відправки команд на сервер
        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine();  // Читання команди з консолі

            // Відправка повідомлення на сервер
            out.println(command);

            // Отримання відповіді від сервера (шлях до аудіофайлу)
            String answer = in.readLine();  // Читання відповіді від сервера
            System.out.println("server:" + answer);
        }
    }


}
