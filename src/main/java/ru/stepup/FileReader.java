package ru.stepup;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
public class FileReader {
    @Value("${file.path}")
    private String filePath;

    private long userIdCounter = 1;
    private long loginIdCounter = 1;

    private List<String> lines = new ArrayList<>();
    @Getter
    private List<User> users=new ArrayList<>();
    @Getter
    private List<Login> logins=new ArrayList<>();

    public void readFile() {
        try {
            File folder = new File(filePath);
            if (folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        lines.add(line);
                    }
                    scanner.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readUsers() {
        for (String line : lines) {
            String[] data = line.split(";");
            users.add(new User(userIdCounter++, data[0], data[1]));
        }
    }

    public void readLogins() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        for (String line : lines) {
            String[] data = line.split(";");
            Date sqlDate;
            try {
                Date parsedDate = format.parse(data[2]);
                sqlDate = new Date(parsedDate.getTime());
            } catch (ParseException e) {
//                e.printStackTrace();
                sqlDate = null;
            }
            User user = users.stream().filter(u -> u.getUsername().equals(data[0])).findFirst().get();
            logins.add(new Login(loginIdCounter++, user.getId(), data[3],sqlDate));
        }
    }
}