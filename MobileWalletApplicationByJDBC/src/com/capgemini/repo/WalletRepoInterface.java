package com.capgemini.repo;


import java.sql.SQLException;

import com.capgemini.beans.Customer;
import com.capgemini.beans.Wallet;

public interface WalletRepoInterface {

	boolean save(Customer Customer);

	Customer findOne(String mobileNo);

	boolean update(String mobileNo, Wallet wallet) throws SQLException;
	
	

}
