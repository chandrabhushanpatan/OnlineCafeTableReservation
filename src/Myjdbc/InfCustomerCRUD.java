package Myjdbc;

import java.util.List;

import model.Customer;

public interface InfCustomerCRUD {
	public boolean insert(Customer customer);

	public Customer search(int id);
	
	public List all();
	
	public boolean update(Customer customer);
	
	public boolean delete(int id);
}
