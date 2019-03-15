package com.capgemini.service;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.capgemini.beans.Customer;
import com.capgemini.beans.Wallet;
import com.capgemini.exception.DupicateMobileNumberException;
import com.capgemini.exception.InsufficientBalanceException;
import com.capgemini.exception.MobilenumberIsNotFoundException;
import com.capgemini.repo.WalletRepoInterface;

public class WalletServiceImp implements WalletService {
	
	
	WalletRepoInterface walletRepo;
	
	public WalletServiceImp(WalletRepoInterface walletRepo)
	{
		this.walletRepo = walletRepo;
	}
	
	
	@Override
	public Customer createAccount(String name, String mobileNo, Wallet wallet) throws DupicateMobileNumberException  // This Method creates the new wallet account.
	{
		
		Customer customer = new Customer(name, mobileNo, wallet); // Create The object of Customer and pass the value in constructor.
		
		if(walletRepo.findOne(mobileNo) == null) {
			walletRepo.save(customer);   
			return customer;
		}
		throw new DupicateMobileNumberException("Dupicate Mobile Number");
	}
	
	
	@Override
	public Customer showBalance(String mobileNo) throws MobilenumberIsNotFoundException  // This method takes mobile no as argument and return the balance of that customer.
	{	
		Customer c = walletRepo.findOne(mobileNo);
		if(c != null)
			return c;
		throw new MobilenumberIsNotFoundException("mobile number is not found");
	}
	
	
	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) throws InsufficientBalanceException // This method takes the mobileNo of source and destination and withdraw the amount to destination account. 
, MobilenumberIsNotFoundException
	{
		Customer sCus;
		Customer rCus;
		sCus = walletRepo.findOne(sourceMobileNo);
		rCus = walletRepo.findOne(targetMobileNo);
		if(sCus != null && rCus != null) {
			double senderAmount = sCus.getWallet().getBalance().doubleValue();
			double recieverAmount = rCus.getWallet().getBalance().doubleValue();
			
			if((senderAmount - amount.doubleValue()) > 0){
				senderAmount =senderAmount - amount.doubleValue();
				recieverAmount =recieverAmount + amount.doubleValue();
				
				BigDecimal sBalance = new BigDecimal(senderAmount);
				Wallet sW = new Wallet(sBalance);
				try {
					walletRepo.update(sCus.getMobileNo(), sW);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
				
				BigDecimal rBalance = new BigDecimal(recieverAmount);
				Wallet rW = new Wallet(rBalance);
				try {
					walletRepo.update(rCus.getMobileNo(), rW);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
				System.out.println("Fund of Rs."+amount+" transferred successfully! from "+sCus.getName()+" to "+rCus.getName());
				return sCus;
			}
			else
			{
				throw new InsufficientBalanceException("Insufficient balance");
				//System.err.println("Invalid amount! As transfer amount is greater than your account balance.");
			}
			
		}
		else
			throw new MobilenumberIsNotFoundException("mobile number is not found");
	}
	
	
	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) throws MobilenumberIsNotFoundException // This method takes mobileNo and amount and deposit the amount to the given mobileNo account.
	{
		Customer cus;
		cus = walletRepo.findOne(mobileNo);
		if(cus != null) {
			double updatedAmount = cus.getWallet().getBalance().doubleValue();
			updatedAmount = updatedAmount + amount.doubleValue();
			//System.out.println(updatedAmount);
			BigDecimal balance = new BigDecimal(updatedAmount);
			Wallet w = new Wallet(balance);
			try {
				walletRepo.update(mobileNo, w);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			return cus;
		}
		else
			throw new MobilenumberIsNotFoundException("mobile number is not found");
		
	}
	
	@Override
	public Customer withDrawAmount(String mobileNo, BigDecimal amount) throws InsufficientBalanceException // This method is used to withdraw the amount from the given mobileNo Account.
, MobilenumberIsNotFoundException
	{
		Customer cus;
		cus = walletRepo.findOne(mobileNo);
		if(cus != null) {
			double initialAmount = cus.getWallet().getBalance().doubleValue();
			if(!(initialAmount - amount.doubleValue() > 0)){
				throw new InsufficientBalanceException("Insufficient balance");
			}
			else{
				initialAmount = initialAmount - amount.doubleValue();
				
				BigDecimal balance = new BigDecimal(initialAmount);
				Wallet w = new Wallet(balance);
				cus.setWallet(w);
				System.out.println("Amount Rs."+amount+" withdrawed successfully");
				return cus;
			}
		}
		else
			throw new MobilenumberIsNotFoundException("mobile number is not found");
	
	}
}
