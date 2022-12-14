package portfolio.views.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import portfolio.models.entities.PortfolioWithValue;
import portfolio.models.entities.Transaction;
import portfolio.models.portfolio.BuySchedule;
import portfolio.models.portfolio.Portfolio;
import portfolio.views.View;
import portfolio.views.ViewFactory;

/**
 * This is a class that can generate different view, which implement the view factory.
 */
public class DefaultSysOutViewFactory implements ViewFactory {

  @Override
  public View newInfoPageView(PortfolioWithValue portfolioWithPrice, Double costOfBasis,
      String errorMessage) {
    return new InfoPageView(portfolioWithPrice, costOfBasis, errorMessage);
  }

  @Override
  public View newInflexibleCreatePageView(Boolean isEnd, Boolean isNamed, Map<String, Integer> map,
      String errorMessage) {
    return new InflexibleCreatePageView(isEnd, isNamed, map, errorMessage);
  }

  @Override
  public View newFlexibleCreatePageView(Boolean isEnd, Boolean isNamed, int stage,
      List<String> inputBuffer,
      List<Transaction> transactions, String errorMessage) {
    return new FlexibleCreatePageView(isEnd, isNamed, stage, inputBuffer, transactions,
        errorMessage);
  }

  @Override
  public View newLoadPageView(Portfolio portfolio, boolean showModifyMenu, String errorMessage) {
    return new LoadPageView(portfolio, showModifyMenu, errorMessage);
  }

  @Override
  public View newMainPageView(String errorMessage, boolean isInitFailed) {
    return new MainPageView(errorMessage, isInitFailed);
  }

  @Override
  public View newPerformacePageView(String portfolioName,
      LocalDate startDate,
      LocalDate endDate,
      Map<String, Integer> performance,
      String scale, boolean isFinish, String errorMessage) {
    return new PerformancePageView(portfolioName, startDate, endDate, performance, scale, isFinish,
        errorMessage);
  }

  @Override
  public View newScheduleCreatePageView(Map<String, Double> stockList, boolean isEnd,
      List<String> inputBuffer, List<Transaction> transactions, boolean addToPortfolio,
                                        String errorMessage) {
    return null;
  }

  @Override
  public View newOneTimeStrategyPageView(Map<String, Double> stockList, boolean isEnd,
      List<String> inputBuffer, String errorMessage) {
    return null;
  }

  @Override
  public View newScheduleInfoPageView(BuySchedule schedule, String errorMessage) {
    return null;
  }

}
