package com.capgemini.service;

import java.math.BigDecimal;

import com.capgemini.beans.Customer;
import com.capgemini.beans.Wallet;
import com.capgemini.exception.DupicateMobileNumberException;
import com.capgemini.exception.InsufficientBalanceException;
import com.capgemini.exception.MobilenumberIsNotFoundException;

public interface WalletService {


	Customer showBalance(String mobileNo) throws MobilenumberIsNotFoundException;

	Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) throws InsufficientBalanceException, MobilenumberIsNotFoundException;

	Customer depositAmount(String mobileNo, BigDecimal amount) throws MobilenumberIsNotFoundException;

	Customer withDrawAmount(String mobileNo, BigDecimal amount) throws InsufficientBalanceException, MobilenumberIsNotFoundException;

	Customer createAccount(String name, String mobileNo, Wallet wallet) throws DupicateMobileNumberException;

}