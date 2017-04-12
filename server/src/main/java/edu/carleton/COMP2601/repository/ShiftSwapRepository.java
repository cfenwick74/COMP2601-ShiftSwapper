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
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * ShiftSwapRepository.java - contains all database-related functionality
 */
public class ShiftSwapRepository {

	public static final String TIME_IN = "timeIn";
	public static final String TIME_OUT = "timeOut";
	private static final String ID = "shift_id";

	private static ShiftSwapRepository instance = new ShiftSwapRepository();

	private static String LAST_ID_QUERY = "select last_insert_rowid()";
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	SQLiteDataSource ds;

	public static ShiftSwapRepository getInstance(){ return instance;}

	private ShiftSwapRepository() {
		String path = System.getProperty("user.dir");
		ds = new SQLiteDataSource();
		System.out.println(path);
		ds.setUrl("jdbc:sqlite:"+path+"\\server\\src\\test\\resources\\project.db");
	}
	public ShiftSwapRepository(String newDb) {

		ds = new SQLiteDataSource();

		ds.setUrl(newDb);
		System.out.println("DATABASE = " + newDb);
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

	/**
	 *	 queries the db,
	 *	 	if user is an admin - returns 1
	 *	 	if user is an employee - returns 0
	 *	 	if user doesn't exist - return -1
 	 */
	public int login(int id, String pass){

		String sql = "SELECT isAdmin FROM employees WHERE employee_id = ? AND password = ?";
		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				st.setInt(1, id);
				st.setString(2, pass);
				ResultSet rs = st.executeQuery();

				boolean isAdmin = ("true".equalsIgnoreCase(rs.getString("isAdmin")));
				if (isAdmin)
					return 1;
				else
					return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

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
	public List<ShiftChangeRequest> findRequestedShiftChangesForEmployee(int employeeId){
		ArrayList<ShiftChangeRequest> shifts = new ArrayList<>();
		String sql = "SELECT a.shift_id, a.timeIn, a.timeOut, b.shift_id, b.timeIn, b.timeOut, shiftchange.change_id FROM shifts a, shifts b, schedule requestor_schedule, schedule requestee_schedule, shiftchange " +
				"WHERE requestor_schedule.schedule_id = shiftchange.requestor_schedule_id " +
				"AND requestee_schedule.schedule_id = shiftchange.requestee_schedule_id " +
				"AND a.shift_id = requestor_schedule.shift_id " +
				"AND b.shift_id = requestee_schedule.shift_id " +
				"and requestee_schedule.employee_id = ?";

		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				st.setInt(1,employeeId);
				ResultSet rs = st.executeQuery();
				shifts.addAll(getRequestedShifts(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return shifts;

	}

	public List<Employee> findEledgibleEmployeesForShift(int e_id, Date date){
		String sql = "SELECT * FROM employees WHERE employee_id NOT IN (SELECT employee_id FROM schedule where shift_id IN (SELECT shift_id FROM shifts WHERE date = ?)) AND employee_id != ?";

		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				st.setString(1,df.format(date));
				st.setInt(2,e_id);
				ResultSet rs = st.executeQuery();
				return getEmployees(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//gets shiftchangerequests from a result set
	private ArrayList<ShiftChangeRequest> getRequestedShifts(ResultSet rs) throws SQLException {
		ArrayList<ShiftChangeRequest> shiftsFound = new ArrayList<>();
		while (rs.next()) {
			shiftsFound.add(new ShiftChangeRequest(	rs.getInt(1),
													new Shift(rs.getInt(1), new Date(rs.getLong(2)), new Date(rs.getLong(3))),
													rs.getInt(4),
													new Shift(rs.getInt(4), new Date(rs.getLong(5)), new Date(rs.getLong(6))), rs.getInt(7)));
		}
		return shiftsFound;
	}

	// get shifts from a resultSet
	private ArrayList<Shift> getShifts(ResultSet rs) throws SQLException {
		ArrayList<Shift> shiftsFound = new ArrayList<>();
		while (rs.next()) {
			shiftsFound.add(new Shift(rs.getInt(ID), new Date(rs.getLong(TIME_IN)), new Date(rs.getLong(TIME_OUT))));
		}
		return shiftsFound;
	}

	// get employees from a resultSet
	private ArrayList<Employee> getEmployees(ResultSet rs) throws SQLException {
		ArrayList<Employee> employees = new ArrayList<>();
		while (rs.next()) {
			employees.add(new Employee(rs.getInt("employee_id"), rs.getString("name"),rs.getString("address"), rs.getBoolean("isAdmin"), rs.getString("Password")));
		}
		return employees;
	}

	// get admin list of all shifts
	public ArrayList<ScheduledShift> getMasterSchedule() {
		ArrayList<ScheduledShift> shifts = new ArrayList<>();
		String sql = "select s.shift_id, s.timeIn, s.timeOut, e.employee_id, e.name from shifts s left outer join SCHEDULE sc " +
				"on s.shift_id = sc.shift_id left outer join EMPLOYEES e on e.employee_id = sc.employee_id";
		try (Connection connection = ds.getConnection()) {
			try (Statement st = connection.createStatement()) {
				ResultSet rs = st.executeQuery(sql);
				ScheduledShift current = null;
				if (rs.next()) {
					current = new ScheduledShift();
					shifts.add(current);
					current.setShift(new Shift(rs.getInt(ID), new Date(rs.getLong(TIME_IN)), new Date(rs.getLong(TIME_OUT))));
				}
				do {
					if (rs.getInt("shift_id") != current.getShift().getId()) {
						current = new ScheduledShift();
						shifts.add(current);
						current.setShift(new Shift(rs.getInt(ID), new Date(rs.getLong(TIME_IN)), new Date(rs.getLong(TIME_OUT))));
					}
					if (rs.getInt("employee_id") != 0) {
						Employee employee = new Employee();
						employee.setId(rs.getInt("employee_id"));
						employee.setName(rs.getString("name"));
						current.getScheduledEmployees().add(employee);
					}

				} while (rs.next());

	}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return shifts;
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

	public void addToRequestShiftChange(int requesting_employee_id, ArrayList<Employee> result, int shift_id) {
		String sql = "INSERT INTO shiftchange(requestor_schedule_id, requestee_employee_id, requestee_schedule_id) values ((SELECT schedule_id FROM schedule WHERE shift_id = ? AND employee_id = ?),?,NULL)";
		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				for(Employee e: result) {
					st.setInt(1, shift_id);
					st.setInt(2, requesting_employee_id);
					st.setInt(3, e.getID());
					st.execute();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addToRequestShiftChange(int requesting_employee_id, int requesting_shift_id, int requestee_employee_id, int requestee_shift_id) {
		String sql = "INSERT INTO shiftchange(requestor_schedule_id, requestee_employee_id, requestee_schedule_id) values ((SELECT schedule_id FROM schedule WHERE shift_id = ? AND employee_id = ?),?,(SELECT schedule_id FROM schedule WHERE shift_id = ? AND employee_id = ?))";
		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
					st.setInt(1, requesting_shift_id);
					st.setInt(2, requesting_employee_id);
					st.setInt(3, requesting_employee_id);
					st.setInt(4, requestee_shift_id);
					st.setInt(5, requestee_employee_id);
					st.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void removeFromSchedule(int employee_id, int shift_id) {
		String sql = "delete from schedule  where employee_id = ? and shift_id = ?";
		try (Connection connection = ds.getConnection()) {
			try (PreparedStatement st = connection.prepareStatement(sql)) {
				st.setInt(1,employee_id);
				st.setInt(2,shift_id);
				st.execute();
				connection.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
