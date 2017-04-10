package edu.carleton.COMP2601.repository;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by carolyn on 2017-04-09.
 */


public class ShiftSwapRepository {

	public static final String TIME_IN = "timeIn";
	public static final String TIME_OUT = "timeOut";
	private static final String ID = "shift_id";


	private static String LAST_ID_QUERY = "select last_insert_rowid()";
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	SQLiteDataSource ds;

	public ShiftSwapRepository(String databaseUrl) {
		ds = new SQLiteDataSource();
		ds.setUrl(databaseUrl);
	}

	// find all shifts in the database
	public List<Shift> findAllShifts() throws SQLException {
		ArrayList<Shift> shifts = new ArrayList<>();
		String sql = "select shift_id, timeIn, timeOut from shifts";
		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					shifts.add(new Shift(rs.getInt(ID), new Date(rs.getLong(TIME_IN)), new Date(rs.getLong(TIME_OUT))));
				}
			}
		}
		return shifts;
	}

	public void addShift(Shift testShift) {
		String sql = "insert into shifts(timein, timeout) values (?,?) ";
		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				st.setLong(1,testShift.getStart().getTime());
				st.setLong(2, testShift.getEnd().getTime());
				st.execute();
			}
			try (Statement st = connection.createStatement()) {
				testShift.setId(st.executeQuery(LAST_ID_QUERY).getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// get a list of all employees in the database
	public List<Employee> findAllEmployees() {
		ArrayList<Employee> employees = new ArrayList<>();
		String sql = "select employee_id, name, address, isAdmin, password from employees";
		try (Connection connection = ds.getConnection()) {
			try (Statement st = connection.createStatement()) {
				ResultSet rs = st.executeQuery(sql);
				while (rs.next()) {
					employees.add(new Employee(rs.getInt("employee_id"), rs.getString("name"), rs.getString("address"), rs.getBoolean("isAdmin"), rs.getString("password")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}

	public void addEmployee(Employee employee) {
		String sql = "insert into employees(name, address, isAdmin, password) values (?,?,?,?) ";
		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				st.setString(1,employee.getName());
				st.setString(2, employee.getAddress());
				st.setBoolean(3, employee.isAdmin());
				st.setString(4, employee.getPassword());
				st.execute();
			}
			try (Statement st = connection.createStatement()) {
				employee.setId(st.executeQuery(LAST_ID_QUERY).getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// find all shifts currently assigned to an employee
	public List<Shift> findShiftsForEmployee(int employeeId) {
		ArrayList<Shift> shifts = new ArrayList<>();
		String sql = "select shifts.shift_id, timeIn, timeOut from schedule inner join shifts on shifts.shift_id = schedule.shift_id where employee_id = ?";

		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				st.setInt(1,employeeId);
				ResultSet rs = st.executeQuery();
				shifts.addAll(getShifts(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return shifts;
	}

	// get shifts from a resultSet
	private ArrayList<Shift> getShifts(ResultSet rs) throws SQLException {
		ArrayList<Shift> shiftsFound = new ArrayList<>();
		while (rs.next()) {
			shiftsFound.add(new Shift(rs.getInt(ID), new Date(rs.getLong(TIME_IN)), new Date(rs.getLong(TIME_OUT))));
		}
		return shiftsFound;
	}

	//add a shift to an employee's schedule
	public boolean addToSchedule(int employeeId, int shiftId) {
		String sql = "insert into schedule(employee_id, shift_id) values (?,?)";
		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				st.setInt(1,employeeId);
				st.setInt(2,shiftId);
				return st.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
}
