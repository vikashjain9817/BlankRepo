package com.capgemini.main;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.capgemini.beans.Customer;
import com.capgemini.beans.Wallet;
import com.capgemini.exception.DupicateMobileNumberException;
import com.capgemini.exception.InsufficientBalanceException;
import com.capgemini.exception.MobilenumberIsNotFoundException;
import com.capgemini.repo.WalletRepoInterface;
import com.capgemini.repo.WalletRepositoryImp;
import com.capgemini.service.WalletService;
import com.capgemini.service.WalletServiceImp;

public class MobileWallet {

	static Scanner sc = new Scanner(System.in);
	static WalletRepoInterface walletRepo = new WalletRepositoryImp();
	static WalletService walletService = new WalletServiceImp(walletRepo);
	
	public static void main(String[] args) throws InsufficientBalanceException, DupicateMobileNumberException, MobilenumberIsNotFoundException {
		
		while(true)
		{
			System.out.println("1) To Add New Customer");
			System.out.println("2) To Fund Transfer");
			System.out.println("3) To Deposit Amount");
			System.out.println("4) To WithDraw Amount");
			System.out.println("5) To Show BAlance");
			System.out.println("6) exit");
			int choice = sc.nextInt();
			switch(choice)
			{
			case 1: getDetails();break;
			case 2: fundTransfer();break;
			case 3: depositAmount();break;
			case 4: WithDrawAmount();break;
			case 5: show();break;
			case 6: System.exit(0);
			default: System.out.println("Wrong choice!");break;
			}
		}
	}

	private static void show() {
		System.out.println("enter mobile number");
		String mobileNo = sc.next();
		while(!validateMobileNo(mobileNo)) {
			System.out.println("mobile number is not valid::Try again");
			mobileNo = sc.next();
		}
		Customer c = null;
		try {
			c = walletService.showBalance(mobileNo);
			System.out.println("Mobile No : "+c.getMobileNo() + " balance is : "+c.getWallet().getBalance());
		}catch(Exception e) {System.out.println("not found");}
		
		
	}

	private static void WithDrawAmount() {
		System.out.println("enter mobile number");
		String mobileNo = sc.next();
		while(!validateMobileNo(mobileNo)) {
			System.out.println("mobile number is not valid::Try again");
			mobileNo = sc.next();
		}
		System.out.println("enter balance");
		BigDecimal amount = new BigDecimal(sc.nextInt());
		Customer c = null;
		try {
			c = walletService.withDrawAmount(mobileNo, amount);
			System.out.println("mobile number is :"+ c.getMobileNo() + " balance is : "+c.getWallet().getBalance());
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}

	private static void depositAmount() {
		System.out.println("enter mobile number");
		String mobileNo = sc.next();
		while(!validateMobileNo(mobileNo)) {
			System.out.println("mobile number is not valid::Try again");
			mobileNo = sc.next();
		}
		System.out.println("enter balance");
		BigDecimal amount = new BigDecimal(sc.nextInt());
		try {
			walletService.depositAmount(mobileNo, amount);
			System.out.println("Amount deposited successfully");
			
		}catch(Exception e)
		{
			System.out.println("mobile number not found");
		}
		
	}

	
	private static void fundTransfer() throws InsufficientBalanceException, MobilenumberIsNotFoundException {
		System.out.println("Enter source mobile No");
		String sourceMobileNo = sc.next();
		while(!validateMobileNo(sourceMobileNo)) {
			System.out.println("mobile number is not valid::Try again");
			sourceMobileNo = sc.next();
		}
		System.out.println("Enter target mobile No");
		String targetMobileNo = sc.next();
		while(!validateMobileNo(targetMobileNo)) {
			System.out.println("mobile number is not valid::Try again");
			targetMobileNo = sc.next();
		}
		System.out.println("enter amount to be transferred");
		BigDecimal amount = new BigDecimal(sc.nextInt());
		try {
			walletService.fundTransfer(sourceMobileNo, targetMobileNo, amount);
		}catch(Exception e) {System.out.println(e);}
	}

	
	private static void getDetails() throws DupicateMobileNumberException {
		System.out.println("enter mobileno");
		String mobileNo = sc.next();
		while(!validateMobileNo(mobileNo)) {
			System.out.println("mobile number is not valid::Try again");
			mobileNo = sc.next();
		}
		System.out.println("enter name");
		String name = sc.next();
		System.out.println("enter balance");
		BigDecimal amount = new BigDecimal(sc.nextInt());
		Wallet wallet = new Wallet(amount);
		try {
			System.out.println(walletService.createAccount(name, mobileNo, wallet));
		}catch(DupicateMobileNumberException e) {
			System.out.println("Duplicate mobile number");
		}
	}
	
	private static boolean validateMobileNo(String mobileNo)
	{
		Pattern pattern = Pattern.compile("[6789]{1}[0-9]{9}");
		Matcher match = pattern.matcher(mobileNo);
		if(match.matches())
			return true;
		else
			return false;
	}
}
