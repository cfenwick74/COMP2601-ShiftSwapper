package edu.carleton.COMP2601.repository;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 */
public class ShiftSwapRepositoryTest {

	private ShiftSwapRepository shiftSwapRepo;

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {

		File database = new File(folder.newFolder(), "test.db");
		FileUtils.copyFile(new File("./src/test/resources/project.db"), database);
		URI relativeURI = new File("/").toURI().relativize(database.toURI());
		shiftSwapRepo = new ShiftSwapRepository("jdbc:sqlite:" + database.getPath());
	}

	@Test
	public void employeeCanGetaListOfShifts() throws Exception {
		assertTrue("should be some shifts", shiftSwapRepo.findAllShifts().size() > 0);
	}

	@Test
	public void canAddAShift() throws Exception {
		Shift testShift = new Shift(new Date(), new Date());
		shiftSwapRepo.addShift(testShift);
		List<Shift> allShifts = shiftSwapRepo.findAllShifts();
		assertTrue("new shift should be in db", allShifts.contains(testShift));
	}

	@Test
	public void canAddAnEmployee() throws Exception {
		Employee testEmployee = new Employee("Ted", "this Address", false, "rosebud");
		shiftSwapRepo.addEmployee(testEmployee);
		List<Employee> allEmployees = shiftSwapRepo.findAllEmployees();
		assertTrue("new shift should be in db", allEmployees.contains(testEmployee));
	}

	@Test
	public void canGetAllEmployees() throws Exception {
		assertTrue("should be some employees", shiftSwapRepo.findAllEmployees().size() > 0);
	}

	@Test
	public void canGetAllShiftsForAnEmployee() throws Exception {
		assertTrue("should be some shifts assigned to an employee", shiftSwapRepo.findShiftsForEmployee(2).size() > 0);
	}

	@Test
	public void canAssignShiftsToAnEmployee() throws Exception {
		shiftSwapRepo.addToSchedule(1, 4);
	}

	@Test
	public void testGetMasterSchedule() throws Exception {
		List<ScheduledShift> returnVal = shiftSwapRepo.getMasterSchedule();
		assertTrue("should have something in the master schedule", returnVal.size() > 0);

	}

	@Test
	public void testGetStuff() throws Exception {
		List<ShiftChangeRequest> returnVal = shiftSwapRepo.findRequestedShiftChangesForEmployee(2);
		assertTrue("should have something in the master schedule", returnVal.size() > 0);

	}

}