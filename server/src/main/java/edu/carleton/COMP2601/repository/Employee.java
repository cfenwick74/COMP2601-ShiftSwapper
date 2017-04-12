package edu.carleton.COMP2601.repository;

import java.io.Serializable;
import java.util.HashMap;

/**
 * COMP2601 Final project: ShiftSwapper
 * Carolyn Fenwick - 100956658
 * Pierre Seguin - 100859121
 * April 12, 2017
 *
 * Employee.java - represents an Employee entity
 */
public class Employee implements Serializable {
private int id;
	private String name;
	private String address;
	private boolean isAdmin;
	private String password;

	public Employee(int id, String name, String address, boolean isAdmin, String password) {
		this(name, address, isAdmin, password);
		this.id = id;
	}

	public Employee(String name, String address, boolean isAdmin, String password) {
		this.name = name;
		this.address = address;
		this.isAdmin = isAdmin;
		this.password = password;
	}

	public Employee(HashMap<String, Serializable> map) {

		this.name = map.containsKey("name")? map.get("name").toString():"";
		this.address = map.containsKey("address")? map.get("address").toString():"";
		this.isAdmin = map.containsKey("admin") && Boolean.parseBoolean(map.get("admin").toString());
		this.password = map.containsKey("password")? map.get("password").toString(): "";
		this.id = map.containsKey("ID")? Integer.parseInt(map.get("ID").toString()): -1;
	}

	public Employee() {
		
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public String getAddress() {
		return address;
	}

	public String getPassword() {
		return password;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Employee employee = (Employee) o;

		if (id != employee.id) return false;
		if (isAdmin != employee.isAdmin) return false;
		if (name != null ? !name.equals(employee.name) : employee.name != null) return false;
		if (address != null ? !address.equals(employee.address) : employee.address != null)
			return false;
		return password != null ? password.equals(employee.password) : employee.password == null;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (isAdmin ? 1 : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}

	public void setName(String name) {
		this.name = name;
	}
}

