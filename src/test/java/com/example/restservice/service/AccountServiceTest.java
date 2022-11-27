package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.ase.restservice.exception.AccountAlreadyExistsException;
import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.service.AccountService;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public final class AccountServiceTest {
  @Mock
  private AccountRepository mockAccountRepository;
  @InjectMocks
  private AccountService accountService;
  private Account newAccount;
  private final String userId = "test-account";

  // Create account
  // Update Account
  // deleteAccountById
  // getAccountById
  // listAllAccounts
  // updateAccountBalance
  @BeforeEach
  // Generate fake stock data, fake asset data
  public void setup() {
    newAccount = new Account(userId, 100.0F, 100F);
  }
  @AfterEach
  public void cleanUp() {

  }

  @DisplayName("unit test for successful createAccount()")
  @Test
  public void createAccountSuccess() throws AccountAlreadyExistsException {
    doReturn(Optional.empty()).when(mockAccountRepository).findById("test-account");
    doReturn(newAccount).when(mockAccountRepository).save(newAccount);
    Account res = accountService.createAccount(newAccount);
    assertEquals(res, newAccount);
  }
  @DisplayName("unit test for attempting to create an account with an existing user id")
  @Test
  public void createDuplicateAccount() throws AccountAlreadyExistsException {
    doReturn(Optional.of(newAccount)).when(mockAccountRepository).findById("test-account");
    Exception exception = assertThrows(AccountAlreadyExistsException.class, () -> {
      accountService.createAccount(newAccount);
    });
  }

  @DisplayName("unit test for successful updateAccount")
  @Test
  public void updateAccountSuccess() throws AccountNotFoundException {
    Account changedAccount = new Account(userId, newAccount.getBalance() + 10F,
        newAccount.getStartingBalance());
    doReturn(changedAccount).when(mockAccountRepository).save(changedAccount);
    doReturn(Optional.of(newAccount)).when(mockAccountRepository).findById(userId);
    Account res = accountService.updateAccount(changedAccount);
    assertEquals(res, changedAccount);
  }

  @DisplayName("unit test for attempt at updateAccount for account that does not exist")
  @Test
  public void updateAccountDoesNotExist() throws AccountNotFoundException {
    doReturn(Optional.empty()).when(mockAccountRepository).findById(userId);

    Exception exception = assertThrows(AccountNotFoundException.class, () -> {
      accountService.updateAccount(newAccount);
    });
    String expectedMessage = "com.ase.restservice.exception.AccountNotFoundException: Account not"
        + " found for accountId :: test-account";
    String actualMessage = exception.getMessage();
    assertEquals(actualMessage, expectedMessage);
  }

  @DisplayName("unit test for successful deleteAccount")
  @Test
  public void deleteAccountSuccess() throws AccountNotFoundException {
    doReturn(Optional.of(newAccount)).when(mockAccountRepository).findById(userId);
    accountService.deleteAccountById(userId);
  }

  @DisplayName("unit test for deleteAccount when account does not exist")
  @Test
  public void deleteAccountDoesNotExist() throws AccountNotFoundException {
    doReturn(Optional.empty()).when(mockAccountRepository).findById(userId);
    Exception exception = assertThrows(AccountNotFoundException.class, () -> {
      accountService.deleteAccountById(userId);
    });
    String expectedMessage = "com.ase.restservice.exception.AccountNotFoundException: Account not"
        + " found for accountId :: test-account";
    String actualMessage = exception.getMessage();
    assertEquals(actualMessage, expectedMessage);
  }

  @DisplayName("unit test for successful getAccountById")
  @Test
  public void getAccountByIdSuccess() throws AccountNotFoundException {
    doReturn(Optional.of(newAccount)).when(mockAccountRepository).findById(userId);
    accountService.getAccountById(userId);
  }

  @DisplayName("unit test for getAccountById when id does not exist")
  @Test
  public void getAccountByIdAccountDoesNotExist() {
    doReturn(Optional.empty()).when(mockAccountRepository).findById(userId);
    Exception exception = assertThrows(AccountNotFoundException.class, () -> {
      accountService.getAccountById(userId);
    });
    String expectedMessage = "Account not found for accountId :: test-account";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @DisplayName("unit test for successful updateAccountBalance")
  @Test
  public void updateAccountBalanceSuccess() throws AccountNotFoundException {
    Float updateAmount = 10F;
    Account changedAccount = new Account(userId, newAccount.getBalance() + updateAmount,
        newAccount.getStartingBalance());
    doReturn(Optional.of(newAccount)).when(mockAccountRepository).findById(userId);
    doReturn(changedAccount).when(mockAccountRepository).save(any());

    Account res = accountService.updateAccountBalance(userId, updateAmount);
    assertEquals(changedAccount, res);
  }

  @DisplayName("unit test for updateAccountBalance on account that does not exist")
  @Test
  public void updateAccountBalanceDoesNotExist() throws AccountNotFoundException {
    doReturn(Optional.empty()).when(mockAccountRepository).findById(userId);

    Exception exception = assertThrows(AccountNotFoundException.class, () -> {
      accountService.updateAccountBalance(userId, 10F);
    });
    String expectedMessage = "Account not found for accountId :: test-account";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);

  }


}
