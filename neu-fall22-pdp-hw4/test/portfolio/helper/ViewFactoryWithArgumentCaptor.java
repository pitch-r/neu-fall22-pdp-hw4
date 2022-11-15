package portfolio.helper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import portfolio.controllers.PageController;
import portfolio.models.entities.Transaction;
import portfolio.models.portfolio.Portfolio;
import portfolio.models.portfolio.impl.InflexiblePortfolio;
import portfolio.models.entities.PortfolioWithValue;
import portfolio.views.View;
import portfolio.views.ViewFactory;
import portfolio.views.impl.InflexibleCreatePageView;
import portfolio.views.impl.InfoPageView;
import portfolio.views.impl.LoadPageView;
import portfolio.views.impl.MainPageView;
import portfolio.views.impl.PerformancePageView;

/**
 * ViewFactory using ArgumentCaptor.
 */
public class ViewFactoryWithArgumentCaptor implements ViewFactory {

  private final ArgumentCaptor<Object> argumentCaptor;

  public ViewFactoryWithArgumentCaptor(ArgumentCaptor<Object> argumentCaptor) {
    this.argumentCaptor = argumentCaptor;
  }

  @Override
  public View newInfoPageView(PortfolioWithValue portfolioWithPrice, Double costOfBasis,
      String errorMessage) {
    argumentCaptor.addArgument(portfolioWithPrice);
    argumentCaptor.addArgument(costOfBasis);
    argumentCaptor.addArgument(errorMessage);
    return new InfoPageView(portfolioWithPrice, costOfBasis, errorMessage);
  }

  @Override
  public View newInflexibleCreatePageView(Boolean isEnd, Boolean isNamed, Map<String, Integer> map,
      String errorMessage) {
    argumentCaptor.addArgument(isEnd);
    argumentCaptor.addArgument(isNamed);
    argumentCaptor.addArgument(map);
    argumentCaptor.addArgument(errorMessage);
    return new InflexibleCreatePageView(isEnd, isNamed, map, errorMessage);
  }

  @Override
  public View newFlexibleCreatePageView(Boolean isEnd, Boolean isNamed, int state,
      List<Transaction> transactions, String errorMessage) {
    return null;
  }

  @Override
  public View newLoadPageView(Portfolio portfolio, boolean showModifyMenu,String errorMessage) {
    argumentCaptor.addArgument(portfolio);
    argumentCaptor.addArgument(showModifyMenu);
    argumentCaptor.addArgument(errorMessage);
    return new LoadPageView(portfolio, showModifyMenu, errorMessage);
  }

  @Override
  public View newMainPageView(String errorMessage, boolean isInitFailed) {
    argumentCaptor.addArgument(errorMessage);
    argumentCaptor.addArgument(isInitFailed);
    return new MainPageView(errorMessage, isInitFailed);
  }

  @Override
  public View newPerformacePageView(String portfolioName,
      LocalDate startDate,
      LocalDate endDate,
      List<String> list,
      List<String> listStar,
      String scale, String errorMessage) {
    argumentCaptor.addArgument(startDate);
    argumentCaptor.addArgument(endDate);
    argumentCaptor.addArgument(list);
    argumentCaptor.addArgument(listStar);
    argumentCaptor.addArgument(scale);
    argumentCaptor.addArgument(errorMessage);
    return new PerformancePageView(portfolioName, startDate, endDate, list,
        listStar, scale, errorMessage);
  }

}