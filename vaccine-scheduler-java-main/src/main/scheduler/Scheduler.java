package scheduler;

import scheduler.db.ConnectionManager;
import scheduler.model.Caregiver;
import scheduler.model.Patient;
import scheduler.model.Vaccine;
import scheduler.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Arrays;

public class Scheduler {

    // objects to keep track of the currently logged-in user
    // Note: it is always true that at most one of currentCaregiver and currentPatient is not null
    //       since only one user can be logged-in at a time
    private static Caregiver currentCaregiver = null;
    private static Patient currentPatient = null;

    public static void main(String[] args) {
        // printing greetings text
        System.out.println();
        System.out.println("Welcome to the COVID-19 Vaccine Reservation Scheduling Application!");
        System.out.println("*** Please enter one of the following commands ***");
        System.out.println("> create_patient <username> <password>");  //TODO: implement create_patient (Part 1) [done]
        System.out.println("> create_caregiver <username> <password>");
        System.out.println("> login_patient <username> <password>");  // TODO: implement login_patient (Part 1) [done]
        System.out.println("> login_caregiver <username> <password>");
        System.out.println("> search_caregiver_schedule <date>");  // TODO: implement search_caregiver_schedule (Part 2)
        System.out.println("> reserve <date> <vaccine>");  // TODO: implement reserve (Part 2)
        System.out.println("> upload_availability <date>");
        System.out.println("> cancel <appointment_id>");  // TODO: implement cancel (extra credit)
        System.out.println("> add_doses <vaccine> <number>");
        System.out.println("> show_appointments");  // TODO: implement show_appointments (Part 2)
        System.out.println("> logout");  // TODO: implement logout (Part 2)
        System.out.println("> quit");
        System.out.println();

        // read input from user
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            String response = "";
            try {
                response = r.readLine();
            } catch (IOException e) {
                System.out.println("Please try again!");
            }
            // split the user input by spaces
            String[] tokens = response.split(" ");
            // check if input exists
            if (tokens.length == 0) {
                System.out.println("Please try again!");
                continue;
            }
            // determine which operation to perform
            String operation = tokens[0];
            if (operation.equals("create_patient")) {
                createPatient(tokens);
            } else if (operation.equals("create_caregiver")) {
                createCaregiver(tokens);
            } else if (operation.equals("login_patient")) {
                loginPatient(tokens);
            } else if (operation.equals("login_caregiver")) {
                loginCaregiver(tokens);
            } else if (operation.equals("search_caregiver_schedule")) {
                searchCaregiverSchedule(tokens);
            } else if (operation.equals("reserve")) {
                reserve(tokens);
            } else if (operation.equals("upload_availability")) {
                uploadAvailability(tokens);
            } else if (operation.equals("cancel")) {
                cancel(tokens);
            } else if (operation.equals("add_doses")) {
                addDoses(tokens);
            } else if (operation.equals("show_appointments")) {
                showAppointments(tokens);
            } else if (operation.equals("logout")) {
                logout(tokens);
            } else if (operation.equals("quit")) {
                System.out.println("Bye!");
                return;
            } else {
                System.out.println("Invalid operation name!");
            }
        }
    }

    private static void createPatient(String[] tokens) {
        // TODO: Part 1
        // create_Patient <username> <password>
        // check 1: the length for tokens need to be exactly 3 to include all information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Failed to create user: patient.");
            return;
        }
        String username = tokens[1];
        String password = tokens[2];
        // check 2: check if the username has been taken already
        if (usernameExistsPatient(username)) {
            System.out.println("Username taken, try again!");
            return;
        }
        byte[] salt = Util.generateSalt();
        byte[] hash = Util.generateHash(password, salt);
        // create the caregiver
            try {
            currentPatient = new Patient.PatientBuilder(username, salt, hash).build();
            // save to patient information to our database
            currentPatient.saveToDB();
            System.out.println("Created user: patient " + username);
        } catch (SQLException e) {
            System.out.println("Failed to create user: patient.");
            e.printStackTrace();
        }
    }

    private static boolean usernameExistsPatient(String username) {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        String selectUsername = "SELECT * FROM Patients WHERE Username = ?";
        try {
            PreparedStatement statement = con.prepareStatement(selectUsername);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            // returns false if the cursor is not before the first record or if there are no rows in the ResultSet.
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error occurred when checking username");
            e.printStackTrace();
        } finally {
            cm.closeConnection();
        }
        return true;
    }

    private static void createCaregiver(String[] tokens) {
        // create_caregiver <username> <password>
        // check 1: the length for tokens need to be exactly 3 to include all information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Failed to create user caregiver.");
            return;
        }
        String username = tokens[1];
        String password = tokens[2];
        // check 2: check if the username has been taken already
        if (usernameExistsCaregiver(username)) {
            System.out.println("Username taken, try again!");
            return;
        }
        byte[] salt = Util.generateSalt();
        byte[] hash = Util.generateHash(password, salt);
        // create the caregiver
        try {
            currentCaregiver = new Caregiver.CaregiverBuilder(username, salt, hash).build();
            // save to caregiver information to our database
            currentCaregiver.saveToDB();
            System.out.println("Created user: caregiver " + username);
        } catch (SQLException e) {
            System.out.println("Failed to create user: caregiver.");
            e.printStackTrace();
        }
    }

    private static boolean usernameExistsCaregiver(String username) {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        String selectUsername = "SELECT * FROM Caregivers WHERE Username = ?";
        try {
            PreparedStatement statement = con.prepareStatement(selectUsername);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            // returns false if the cursor is not before the first record or if there are no rows in the ResultSet.
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            System.out.println("Error occurred when checking username");
            e.printStackTrace();
        } finally {
            cm.closeConnection();
        }
        return true;
    }

    private static void loginPatient(String[] tokens) {
        // TODO: Part 1
        // login_patient <username> <password>
        // check 1: if someone's already logged-in, they need to log out first
        if (currentCaregiver != null || currentPatient != null) {
            System.out.println("User already logged in.");
            return;
        }
        // check 2: the length for tokens need to be exactly 3 to include all information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Login failed.");
            return;
        }
        String username = tokens[1];
        String password = tokens[2];

        Patient patient = null;
        try {
            patient = new Patient.PatientGetter(username, password).get();
        } catch (SQLException e) {
            System.out.println("Login failed.");
            e.printStackTrace();
        }
        // check if the login was successful
        if (patient == null) {
            System.out.println("Login failed.");
        } else {
            System.out.println("Logged in as patient: " + username);
            currentPatient = patient;
        }
    }

    private static void loginCaregiver(String[] tokens) {
        // login_caregiver <username> <password>
        // check 1: if someone's already logged-in, they need to log out first
        if (currentCaregiver != null || currentPatient != null) {
            System.out.println("User already logged in.");
            return;
        }
        // check 2: the length for tokens need to be exactly 3 to include all information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Login failed.");
            return;
        }
        String username = tokens[1];
        String password = tokens[2];

        Caregiver caregiver = null;
        try {
            caregiver = new Caregiver.CaregiverGetter(username, password).get();
        } catch (SQLException e) {
            System.out.println("Login failed.");
            e.printStackTrace();
        }
        // check if the login was successful
        if (caregiver == null) {
            System.out.println("Login failed.");
        } else {
            System.out.println("Logged in as caregiver: " + username);
            currentCaregiver = caregiver;
        }
    }

    private static void searchCaregiverSchedule(String[] tokens) {
        // TODO: Part 2
        // check 1: check if the current logged-in user is a caregiver or patient
        if ((currentCaregiver == null) && (currentPatient == null)) {
            System.out.println("Please login as a caregiver or patient first!");
            return;
        }
        // check 2: the length for tokens need to be exactly 2 to include all information (with the operation name)
        if (tokens.length != 2) {
            System.out.println("Please try again!");
            return;
        }
        //date format
        String stringDate = tokens[1];
        Date d = Date.valueOf(tokens[1]);

        //need to connect to database to find caregivers/patients
        ConnectionManager cm = new ConnectionManager();
        Connection c = cm.createConnection();

        //display all caregivers and vaccine doses sql
        String findCaregiver = "SELECT Username FROM Availabilities WHERE Time = ? ORDER BY Username ASC;";
        String findDoses = "SELECT * FROM Vaccines;";
        try {
            //caregivers
            PreparedStatement caregiverStatement = c.prepareStatement(findCaregiver);
            caregiverStatement.setDate(1, d);
            ResultSet resultSet = caregiverStatement.executeQuery();
            if (resultSet == null) {
                System.out.println("no caregiver found in database");
                return;
            }
            System.out.println("Caregivers: " );
            while (resultSet.next()) {
                String caregiverName = resultSet.getString("Username");
                System.out.println(caregiverName);
            }

            //vaccines
            PreparedStatement vaccineStatement = c.prepareStatement(findDoses);
            ResultSet resultSet2 = vaccineStatement.executeQuery();
            System.out.println("Vaccines: ");
            while (resultSet2.next()) {
                String vaccineName = resultSet2.getString("Name");
                int vaccineDoses = resultSet2.getInt("Doses");
                System.out.println("Vaccine Name: " + vaccineName + " Available Doses: " + vaccineDoses);
            }

        } catch (SQLException e) {
            System.out.println("Please try again: sql exception");
        } finally {
            cm.closeConnection();
        }
    }

    private static void reserve(String[] tokens) {
        // TODO: Part 2
        // check 1: check if the current logged-in user is a patient
        if (currentPatient == null) {
            System.out.println("Please login as a patient first!");
            return;
        }
        // check 2: the length for tokens need to be exactly 3 to include all information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Please try again!");
            return;
        }
        String stringDate = tokens[1];
        Date d = Date.valueOf(tokens[1]);
        String vaccineName = tokens[2];


        ConnectionManager cm = new ConnectionManager();
        Connection c = cm.createConnection();

        String appointment_id = d + currentPatient.getUsername();
        String findCaregiver = "SELECT Username FROM Availabilities WHERE Time = ? ORDER BY Username ASC;";
        String findVaccine = "SELECT Doses FROM Vaccines WHERE Name = ?;";
        try {
            PreparedStatement caregiver = c.prepareStatement(findCaregiver);
            caregiver.setDate(1, d);
            ResultSet resultSetA = caregiver.executeQuery();
            if (!resultSetA.next())
            {
                System.out.println("No caregiver is available!");
            }
            PreparedStatement vaccine = c.prepareStatement(findVaccine);
            vaccine.setString(1, vaccineName);
            ResultSet resultSetB = vaccine.executeQuery();
            if (!resultSetB.next())
            {
                System.out.println("Not enough available doses!");
            }

        String setAppointment = "INSERT INTO Appointments (? , ? , ? , ? , ? );";
        String removeAvail = "DELETE FROM Appointments WHERE Time = ? AND Username = ?;";

            //create appointment in db
            while (resultSetA.next()) {
                String caregiverName = resultSetA.getString("Username");

                PreparedStatement appt = c.prepareStatement(setAppointment);
                appt.setString(1, appointment_id);
                appt.setString(2, caregiverName);
                appt.setString(3, currentPatient.getUsername());
                appt.setString(4, vaccineName);
                appt.setDate(5, d);
                appt.executeUpdate();
                System.out.println("Appointment scheduled");
                System.out.println("AppointmentID: " + appointment_id);
                System.out.println("Caregiver Username: " + caregiverName);
            //delete avail
                PreparedStatement deleteAppt = c.prepareStatement(removeAvail);
                deleteAppt.setString(1, caregiverName);
                deleteAppt.setDate(2, d);
                deleteAppt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Please try again: sql exception");
        } finally {
            cm.closeConnection();
        }

    }

    private static void uploadAvailability(String[] tokens) {
        // upload_availability <date>
        // check 1: check if the current logged-in user is a caregiver
        if (currentCaregiver == null) {
            System.out.println("Please login as a caregiver first!");
            return;
        }
        // check 2: the length for tokens need to be exactly 2 to include all information (with the operation name)
        if (tokens.length != 2) {
            System.out.println("Please try again!");
            return;
        }
        String date = tokens[1];

        try {
            Date d = Date.valueOf(date);
            currentCaregiver.uploadAvailability(d);
            System.out.println("Availability uploaded!");
        } catch (IllegalArgumentException e) {
            System.out.println("Please enter a valid date!");
        } catch (SQLException e) {
            System.out.println("Error occurred when uploading availability");
            e.printStackTrace();
        }
    }

    private static void cancel(String[] tokens) {
    }

    private static void addDoses(String[] tokens) {
        // add_doses <vaccine> <number>
        // check 1: check if the current logged-in user is a caregiver
        if (currentCaregiver == null) {
            System.out.println("Please login as a caregiver first!");
            return;
        }
        // check 2: the length for tokens need to be exactly 3 to include all information (with the operation name)
        if (tokens.length != 3) {
            System.out.println("Please try again!");
            return;
        }
        String vaccineName = tokens[1];
        int doses = Integer.parseInt(tokens[2]);
        Vaccine vaccine = null;
        try {
            vaccine = new Vaccine.VaccineGetter(vaccineName).get();
        } catch (SQLException e) {
            System.out.println("Error occurred when adding doses");
            e.printStackTrace();
        }
        // check 3: if getter returns null, it means that we need to create the vaccine and insert it into the Vaccines
        //          table
        if (vaccine == null) {
            try {
                vaccine = new Vaccine.VaccineBuilder(vaccineName, doses).build();
                vaccine.saveToDB();
            } catch (SQLException e) {
                System.out.println("Error occurred when adding doses");
                e.printStackTrace();
            }
        } else {
            // if the vaccine is not null, meaning that the vaccine already exists in our table
            try {
                vaccine.increaseAvailableDoses(doses);
            } catch (SQLException e) {
                System.out.println("Error occurred when adding doses");
                e.printStackTrace();
            }
        }
        System.out.println("Doses updated!");
    }

    private static void showAppointments(String[] tokens) {
        // TODO: Part 2
        // check 1: check if the current logged-in user is a caregiver or patient
        if ((currentCaregiver == null) && (currentPatient == null)) {
            System.out.println("Please login as a caregiver or patient first!");
        }
        // check 2: the length for tokens need to be exactly 2 to include all information (with the operation name)
        if (tokens.length != 2) {
            System.out.println("Please try again!");
        }
        if (currentCaregiver != null)
        {
            ConnectionManager cm = new ConnectionManager();
            Connection c = cm.createConnection();

            String caregiverLookup = "SELECT appointment_id, vaccineName, Time, patientName FROM Appointments WHERE caregiverName = ? ORDER BY appointment_id ASC;";
            try {
                PreparedStatement caregiverSearch = c.prepareStatement(caregiverLookup);
                caregiverSearch.setString(1, currentCaregiver.getUsername());
                ResultSet caregiverResult = caregiverSearch.executeQuery();
                System.out.println("All appointments here: ");
                while (caregiverResult.next())
                {
                    String appointment_id = caregiverResult.getString("appointment_id");
                    String vaccineName = caregiverResult.getString("vaccineName");
                    Date Time = caregiverResult.getDate("Time");
                    String patientName = caregiverResult.getString("patientName");
                    System.out.println("Appointment ID: " + appointment_id + " Vaccine name: " + vaccineName + " Time: " + Time + " Patient name: " + patientName);
                }
            } catch (SQLException e) {
                System.out.println("Please try again: sql exception");
            } finally {
                cm.closeConnection();
            }

        }
        //patient
        if (currentPatient != null)
        {
            ConnectionManager cm = new ConnectionManager();
            Connection c = cm.createConnection();

            String patientLookup = "SELECT appointment_id, vaccineName, Time, caregiverName FROM Appointments WHERE patientName = ? ORDER BY appointment_id ASC;";
            try {
                PreparedStatement patientSearch = c.prepareStatement(patientLookup);
                patientSearch.setString(1, currentPatient.getUsername());
                ResultSet patientResult = patientSearch.executeQuery();
                System.out.println("All appointments here: ");
                while (patientResult.next())
                {
                    String appointment_id = patientResult.getString("appointment_id");
                    String vaccineName = patientResult.getString("vaccineName");
                    Date Time = patientResult.getDate("Time");
                    String caregiverName = patientResult.getString("caregiverName");
                    System.out.println("Appointment ID: " + appointment_id + " Vaccine name: " + vaccineName + " Time: " + Time + " Caregiver name: " + caregiverName);
                }
            } catch (SQLException e) {
                System.out.println("Please try again: sql exception");
            } finally {
                cm.closeConnection();
            }
        }

    }

    private static void logout(String[] tokens) {
        // TODO: Part 2
        if (tokens.length != 1)
        {
            System.out.println("Please try again");
        }
        if ((currentCaregiver == null) && (currentPatient == null)) {
            System.out.println("Please login as a caregiver or patient first!");
        }
        currentPatient = null;
        currentCaregiver = null;
        System.out.println("logged out");
    }
}
