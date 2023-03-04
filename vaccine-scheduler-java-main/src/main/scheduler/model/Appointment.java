package scheduler.model;

import scheduler.db.ConnectionManager;
import scheduler.util.Util;

import java.sql.*;
import java.util.Arrays;

public class Appointment {
    private final int appointmentID;
    private final String vaccineName;
    private final Date Time;
    private final String caregiverName;
    private final String patientName;

    private Appointment(AppointmentBuilder builder) {
        this.appointmentID = builder.appointmentID;
        this.vaccineName = builder.vaccineName;
        this.Time = builder.Time;
        this.caregiverName = builder.caregiverName;
        this.patientName = builder.patientName;
    }

    private Appointment(AppointmentGetter getter) {
        this.appointmentID= getter.appointmentID;
        this.vaccineName = getter.vaccineName;
        this.Time = getter.Time;
        this.caregiverName = getter.caregiverName;
        this.patientName = getter.patientName;
    }

    // Getters
    public int getAppointmentID () {
        return appointmentID;
    }

    public String vaccineName () {
        return vaccineName;
    }
    public Date Time () {
        return Time;
    }
    public String caregiverName () {
        return caregiverName;
    }

    public String patientName () {
        return patientName;
    }

    public void saveToDB() throws SQLException {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();

        String addAppt = "INSERT INTO Appointment VALUES (? , ?, ?, ?, ?)";
        try {
            PreparedStatement statement = con.prepareStatement(addAppt);
            statement.setInt(1, this.appointmentID);
            statement.setString(2, this.caregiverName);
            statement.setString(3, this.vaccineName);
            statement.setString(4, this.patientName);
            statement.setDate(5, this.Time);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException();
        } finally {
            cm.closeConnection();
        }
    }

    public static class AppointmentBuilder {
        private final int appointmentID;
        private final String vaccineName;
        private final Date Time;
        private final String caregiverName;
        private final String patientName;

        public AppointmentBuilder(int appointmentID, String vaccineName, Date Time, String caregiverName, String patientName) {
            this.appointmentID = appointmentID;
            this.vaccineName = vaccineName;
            this.Time = Time;
            this.caregiverName = caregiverName;
            this.patientName = patientName;
        }

        public Appointment build() {
            return new Appointment(this);
        }
    }

    public static class AppointmentGetter {
        private final int appointmentID;
        private final String vaccineName;
        private final Date Time;
        private final String caregiverName;
        private final String patientName;

        public AppointmentGetter(int appointmentID, String vaccineName, Date Time, String caregiverName, String patientName) {
            this.appointmentID = appointmentID;
            this.vaccineName = vaccineName;
            this.Time = Time;
            this.caregiverName = caregiverName;
            this.patientName = patientName;
        }


    }
}
