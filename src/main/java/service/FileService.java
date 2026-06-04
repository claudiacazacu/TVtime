package service;

import model.Movie;
import model.Series;
import model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileService {
    public void readTexts(String fileName)
    {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                String text=scanner.nextLine();
                System.out.println(text);
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Couldnt find the file");
        } catch (IOException e) {
            System.out.println("something went wrong");
        }
    }
    //pt useri
    //public void readUsers(String fileName)
    public ArrayList<User> readUsers(String fileName){
        ArrayList<User> users = new ArrayList<>();
        try{
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                String username = parts[0];
                int age = Integer.parseInt(parts[1]);
                String email = parts [2];

                //System.out.println("Username: " + username + "age: " + age + "email: " + email);
                User user = new User(username, age, email);
                users.add(user);
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            System.out.println("file not found");
        }
        return users;
    }

    public ArrayList<Series> readSeries(String fileName){
        ArrayList<Series> series = new ArrayList<>();
         try{
             File file = new File(fileName);
             Scanner scanner = new Scanner(file);

             while(scanner.hasNextLine()) {
                 String line = scanner.nextLine();
                 String[] parts = line.split(",");
                 String title = parts[0];
                 String releaseDate = parts[1];
                 String genre = parts[2];
                 String description = parts[3];
                 String director = parts[4];
                 String company = parts[5];

                 /*System.out.println(
                         "Series | Title: " + title +
                                 " | Release: " + releaseDate +
                                 " | Genre: " + genre +
                                 " | Description: " + description +
                                 " | Director: " + director +
                                 " | Company: " + company
                 );*/
                 Series theSeries = new Series(title,releaseDate,genre,description,director,company);
                 series.add(theSeries);
             }
             scanner.close();
         }
         catch(FileNotFoundException e){
             System.out.println("file not found");
         }
         return series;
    }

    public ArrayList<Movie> readMovies(String fileName){
        ArrayList<Movie> movies = new ArrayList<>();
        try{
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts=line.split(",");

                String title = parts[0];
                String releaseDate = parts[1];
                String genre = parts[2];
                String description = parts [3];
                String director = parts[4];
                String company = parts[5];

                /*System.out.println(
                                "Movie | Title: " + title +
                                " | Release: " + releaseDate +
                                " | Genre: " + genre +
                                " | Description: " + description +
                                " | Director: " + director +
                                " | Company: " + company
                );*/
                Movie movie = new Movie(title, releaseDate, genre, description, director,company);
                movies.add(movie);
            }
            scanner.close();
        }
        catch(FileNotFoundException e){
            System.out.println("couldnt find the file");
        }
        return movies;

    }
}
