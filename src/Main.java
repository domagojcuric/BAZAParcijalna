import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        DataSource dataSource = createDataSource();


        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspješno spojeni na bazu podataka!");

            Scanner scanner = new Scanner(System.in);
            int opcija;

            do {
                System.out.println("\n=== IZBORNIK ===");
                System.out.println("1 - Unesi novog polaznika");
                System.out.println("2 - Unesi novi program obrazovanja");
                System.out.println("3 - upiši polaznika na program obrazovanja ");
                System.out.println("4 - prebaci polaznika iz jednog u drugi program obrazovanja");
                System.out.println("5 - pregled programa obrazovanja");
                System.out.print("Odaberi opciju: ");
                opcija = scanner.nextInt();
                scanner.nextLine();

                switch (opcija) {
                    case 1:
                        dodajPolaznika(connection);
                        break;
                    case 2:
                        dodajProgramObrazovanja(connection);
                        break;
                    case 3:
                        upisiPolaznika(connection);
                        break;
                    case 4:
                        prebaciPolaznika(connection);
                        break;
                    case 5:
                        System.out.println("Kraj programa.");
                        break;
                    default:
                        System.out.println("Nepoznata opcija. Pokušaj ponovno.");
                }

            } while (opcija != 5);

        } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja na bazu podataka:");
            e.printStackTrace();
        }
    }

    private static void dodajPolaznika(Connection conn) {
        Scanner input = new Scanner(System.in);
        String polaznikIme = input.nextLine();
        System.out.println("Unesi ime novog polaznika:");
        String polaznikPrezime = input.nextLine();
        System.out.println("Unesi prezime novog polaznika:");


        String sql = "INSERT INTO Polaznik (Ime,Prezime) VALUES(?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, polaznikIme);
            stmt.setString(2, polaznikPrezime);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Polaznik je uspjesno dodana");
            } else {
                System.out.println("Dodavanje polaznika nije uspjelo");
            }
        } catch (SQLException e) {
            System.out.println("Greška prilikom dodavanja polaznika");
        }
    }

    private static void dodajProgramObrazovanja(Connection conn) {
        Scanner input = new Scanner(System.in);
        String program = input.nextLine();
        System.out.println("Unesi novi program obrazovanja:");
        int csvet = input.nextInt();
        System.out.println("Unesi CSVET bodove:");


        String sql = "INSERT INTO ProgramObrazovanja (Naziv,CSVET) VALUES(?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, program);
            stmt.setInt(2, csvet);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Program obrazovanja je uspjesno dodana");
            } else {
                System.out.println("Dodavanje programa obrazovanja nije uspjelo");
            }
        } catch (SQLException e) {
            System.out.println("Greška prilikom dodavanja programa obrazovanja");
        }
    }

    public static void upisiPolaznika(Connection conn) {
        Scanner input = new Scanner(System.in);
        System.out.print("Unesite ID polaznika: ");
        int idPolaznika = input.nextInt();
        System.out.print("Unesite ID programa obrazovanja: ");
        int idObrazovanja = input.nextInt();


        String sql = "INSERT INTO Upisi (IDPolaznik, IDProgramObrazovanja) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPolaznika);
            stmt.setInt(2, idObrazovanja);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Program obrazovanja je uspjesno dodana");
            } else {
                System.out.println("Dodavanje programa obrazovanja nije uspjelo");
            }
        } catch (SQLException e) {
            System.out.println("Greška prilikom dodavanja programa obrazovanja");
        }

    }

    public static void prebaciPolaznika(Connection conn){
        Scanner input = new Scanner(System.in);
        System.out.print("Unesite ID polaznika kojeg želite prebaciti: ");
        int idPolaznik = input.nextInt();
        System.out.print("Unesite novi ID programa obrazovanja gdje zelite da se polaznik prebaci: ");
        int idObrazovanja = input.nextInt();

        String sql = "UPDATE Upis SET IDProgramObrazovanja = ? WHERE IDPolaznik = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPolaznik);
            stmt.setInt(2, idObrazovanja);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Program obrazovanja je uspjesno promjenjen");
            } else {
                System.out.println("Promjena programa obrazovanja nije uspjela");
            }
        } catch (SQLException e) {
            System.out.println("Greška prilikom Promjene programa obrazovanja");
        }
    }

    public static void prikaziPolaznike(Connection conn){
        Scanner input = new Scanner(System.in);
        System.out.print("Unesite ID programa obrazovanja: ");
        int idObrazovanja = input.nextInt();

        String sql = """
                SELECT p.Ime, p.Prezime, pr.Naziv, pr.CSVET
                FROM Upis u
                JOIN Polaznik p ON u.IDPolaznik = p.PolaznikID
                JOIN ProgramObrazovanja pr ON u.IDProgramObrazovanja = pr.ProgramObrazovanjaID
                WHERE pr.ProgramObrazovanjaID = ?
                """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String ime = rs.getString("Ime");
                String prezime = rs.getString("Prezime");
                String program = rs.getString("Naziv");
                int csvet = rs.getInt("CSVET");
                System.out.printf("Ime: %s | Prezime: %s%n | Program: %s | CSVET: %d ", ime, prezime,program,csvet);
            }

        } catch (SQLException e) {
            System.err.println("Greška prilikom dohvaćanja podataka:");
            e.printStackTrace();
        }
    }


    // Metoda za stvaranje DataSource objekta
    private static DataSource createDataSource() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("JavaAdv");
        dataSource.setUser("sa");
        dataSource.setPassword("SQL");
        dataSource.setEncrypt(false);
        return dataSource;
    }
}