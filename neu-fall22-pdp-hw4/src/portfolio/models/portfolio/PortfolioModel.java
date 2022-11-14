package portfolio.models.portfolio;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import portfolio.models.entities.PortfolioFormat;
import portfolio.models.entities.PortfolioWithValue;
import portfolio.models.entities.Transaction;

/**
 * Service interface for creating and retrieving Portfolio.
 */
public interface PortfolioModel {

  Portfolio getPortfolio();

  Portfolio create(PortfolioFormat format, List<Transaction> transactions) throws Exception;

  void createAndSet(PortfolioFormat format, List<Transaction> transactions) throws Exception;

  void load(String text) throws Exception;

  void addTransactions(List<Transaction> newTransactions) throws Exception;

  PortfolioWithValue getValue(LocalDate date) throws Exception;

  double getCostBasis(LocalDate date) throws Exception;

  Map<String, PortfolioWithValue> getValues(LocalDate from, LocalDate to) throws Exception;
}
